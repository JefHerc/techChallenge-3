package com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento;

import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import com.fiap.gestao_servicos.core.domain.EstabelecimentoFilter;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.avaliacao.AvaliacaoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ServicoProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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

            // ── Filtros simples no EstabelecimentoEntity ──────────────────────────────

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

            // ── Filtros via JOIN em profissionais → servico_profissional → servico ───
            // (nome do serviço e faixa de preço usam o mesmo join chain)

            boolean needsProfJoin = f.getServicoNome() != null
                    || f.getPrecoMin() != null
                    || f.getPrecoMax() != null
                    || f.getDataDisponivel() != null
                    || f.getProfissionalNotaMinima() != null;

            Join<EstabelecimentoEntity, ProfissionalEntity> profJoin = needsProfJoin
                    ? root.join("profissionais", JoinType.INNER)
                    : null;
            if (needsProfJoin) {
                query.distinct(true);
            }

            // Serviço oferecido e faixa de preço
            boolean needsSpJoin = f.getServicoNome() != null
                    || f.getPrecoMin() != null
                    || f.getPrecoMax() != null;

            if (needsSpJoin) {
                Join<ProfissionalEntity, ServicoProfissionalEntity> spJoin =
                        profJoin.join("servicoProfissional", JoinType.INNER);

                if (f.getServicoNome() != null && !f.getServicoNome().isBlank()) {
                    Join<ServicoProfissionalEntity, ServicoEntity> sJoin =
                            spJoin.join("servico", JoinType.INNER);
                    predicates.add(cb.like(
                            cb.lower(sJoin.get("nome")),
                            "%" + f.getServicoNome().toLowerCase() + "%"));
                }

                if (f.getPrecoMin() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(spJoin.get("valor"), f.getPrecoMin()));
                }

                if (f.getPrecoMax() != null) {
                    predicates.add(cb.lessThanOrEqualTo(spJoin.get("valor"), f.getPrecoMax()));
                }
            }

            // ── Disponibilidade ───────────────────────────────────────────────────────
            // Filtra estabelecimentos com ao menos um profissional livre no dia informado.
            // "Livre" = não possui agendamento com status AGENDADO cuja janela
            //           (dataHoraInicio < fim_do_dia AND dataHoraFim > inicio_do_dia).

            if (f.getDataDisponivel() != null) {
                LocalDateTime inicio = f.getDataDisponivel().atStartOfDay();
                LocalDateTime fim    = inicio.plusDays(1);

                Subquery<Integer> agSubq = query.subquery(Integer.class);
                Root<AgendamentoEntity> ag = agSubq.from(AgendamentoEntity.class);
                agSubq.select(cb.literal(1)).where(
                        cb.equal(ag.get("profissional"), profJoin),
                        cb.equal(ag.get("status"), AgendamentoStatus.AGENDADO),
                        cb.lessThan(ag.get("dataHoraInicio"), fim),
                        cb.greaterThan(ag.get("dataHoraFim"), inicio)
                );
                // O profissional não possui agendamento AGENDADO conflitante nesse dia
                predicates.add(cb.not(cb.exists(agSubq)));
            }

            // ── Nota mínima ───────────────────────────────────────────────────────────
            // AVG(avaliacao.notaEstabelecimento) dos agendamentos vinculados >= notaMinima

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

            // ── Nota mínima por profissional ──────────────────────────────────────────
            // Existe pelo menos um profissional no estabelecimento cuja média de
            // avaliacao.notaProfissional (dos agendamentos vinculados) >= profissionalNotaMinima
            if (f.getProfissionalNotaMinima() != null) {
                Subquery<Long> profSubq = query.subquery(Long.class);
                Root<AvaliacaoEntity> av2 = profSubq.from(AvaliacaoEntity.class);
                profSubq.select(cb.literal(1L));
                profSubq.groupBy(av2.get("agendamento").get("profissional"));
                profSubq.where(
                        cb.equal(av2.get("agendamento").get("profissional"), profJoin),
                        cb.equal(av2.get("agendamento").get("status"), AgendamentoStatus.CONCLUIDO),
                        cb.greaterThanOrEqualTo(cb.avg(av2.get("notaProfissional")), f.getProfissionalNotaMinima())
                );

                predicates.add(cb.exists(profSubq));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


