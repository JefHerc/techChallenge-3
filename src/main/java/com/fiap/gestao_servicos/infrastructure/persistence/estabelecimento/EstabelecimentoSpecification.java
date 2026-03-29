package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.avaliacao.AvaliacaoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstabelecimentoSpecification {

    private EstabelecimentoSpecification() {}

    public static Specification<EstabelecimentoEntity> comFiltros(EstabelecimentoFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.getNome() != null && !f.getNome().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("nome")),
                        "%" + f.getNome().toLowerCase() + "%"));
            }

            if (f.getCidade() != null && !f.getCidade().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("endereco").get("cidade")),
                        f.getCidade().toLowerCase()));
            }

            if (f.getEstado() != null && !f.getEstado().isBlank()) {
                predicates.add(cb.equal(
                        cb.lower(root.get("endereco").get("estado")),
                        f.getEstado().toLowerCase()));
            }

            if (f.getBairro() != null && !f.getBairro().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("endereco").get("bairro")),
                        "%" + f.getBairro().toLowerCase() + "%"));
            }

            boolean needsSpJoin = f.getServicoNome() != null
                    || f.getPrecoMin() != null
                    || f.getPrecoMax() != null;

            if (needsSpJoin) {
                Subquery<Long> servicoSubq = query.subquery(Long.class);
                Root<ServicoProfissionalEntity> sp = servicoSubq.from(ServicoProfissionalEntity.class);
                Root<ProfissionalEntity> p = servicoSubq.from(ProfissionalEntity.class);
                Root<ServicoEntity> s = servicoSubq.from(ServicoEntity.class);

                List<Predicate> spPredicates = new ArrayList<>();
                spPredicates.add(cb.equal(p.get("id"), sp.get("profissional").get("id")));
                spPredicates.add(cb.equal(s.get("id"), sp.get("servico").get("id")));
                spPredicates.add(cb.equal(p.get("estabelecimento").get("id"), root.get("id")));

                if (f.getServicoNome() != null && !f.getServicoNome().isBlank()) {
                    spPredicates.add(cb.like(
                            cb.lower(s.get("nome")),
                            "%" + f.getServicoNome().toLowerCase() + "%"));
                }

                if (f.getPrecoMin() != null) {
                    spPredicates.add(cb.greaterThanOrEqualTo(sp.get("valor"), f.getPrecoMin()));
                }

                if (f.getPrecoMax() != null) {
                    spPredicates.add(cb.lessThanOrEqualTo(sp.get("valor"), f.getPrecoMax()));
                }

                servicoSubq.select(cb.literal(1L))
                        .where(spPredicates.toArray(new Predicate[0]));
                predicates.add(cb.exists(servicoSubq));
            }

            if (f.getDataDisponivel() != null) {
                LocalDateTime inicio = f.getDataDisponivel().atStartOfDay();
                LocalDateTime fim    = inicio.plusDays(1);

                Subquery<Long> profDisponivelSubq = query.subquery(Long.class);
                Root<ProfissionalEntity> p = profDisponivelSubq.from(ProfissionalEntity.class);
                profDisponivelSubq.select(cb.literal(1L));

                Subquery<Long> agConflitoSubq = query.subquery(Long.class);
                Root<AgendamentoEntity> ag = agConflitoSubq.from(AgendamentoEntity.class);
                agConflitoSubq.select(cb.literal(1L)).where(
                    cb.equal(ag.get("profissional").get("id"), p.get("id")),
                    cb.equal(ag.get("status"), AgendamentoStatus.AGENDADO),
                    cb.lessThan(ag.get("dataHoraInicio"), fim),
                    cb.greaterThan(ag.get("dataHoraFim"), inicio)
                );

                profDisponivelSubq.where(
                    cb.equal(p.get("estabelecimento").get("id"), root.get("id")),
                    cb.not(cb.exists(agConflitoSubq))
                );

                predicates.add(cb.exists(profDisponivelSubq));
            }

            if (f.getNotaMinima() != null) {
                Subquery<Double> notaSubq = query.subquery(Double.class);
                Root<AvaliacaoEntity> av = notaSubq.from(AvaliacaoEntity.class);
                notaSubq.select(cb.avg(av.get("notaEstabelecimento")))
                        .where(
                                cb.equal(av.get("agendamento").get("estabelecimento"), root),
                                cb.equal(av.get("agendamento").get("status"), AgendamentoStatus.CONCLUIDO)
                        );
                predicates.add(cb.greaterThanOrEqualTo(notaSubq, f.getNotaMinima()));
            }

            if (f.getProfissionalNotaMinima() != null) {
                Subquery<Long> profSubq = query.subquery(Long.class);
                Root<AvaliacaoEntity> av2 = profSubq.from(AvaliacaoEntity.class);
                profSubq.select(cb.literal(1L));
                profSubq.groupBy(av2.get("agendamento").get("profissional"));
                profSubq.where(
                    cb.equal(av2.get("agendamento").get("profissional").get("estabelecimento").get("id"), root.get("id")),
                        cb.equal(av2.get("agendamento").get("status"), AgendamentoStatus.CONCLUIDO),
                        cb.greaterThanOrEqualTo(cb.avg(av2.get("notaProfissional")), f.getProfissionalNotaMinima())
                );

                predicates.add(cb.exists(profSubq));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


