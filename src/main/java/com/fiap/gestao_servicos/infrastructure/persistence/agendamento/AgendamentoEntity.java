package com.fiap.gestao_servicos.infrastructure.persistence.agendamento;

import com.fiap.gestao_servicos.core.domain.AgendamentoStatus;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.estabelecimento.EstabelecimentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"}, justification = "Entidade JPA com relacionamentos gerenciados pelo ORM")
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalEntity profissional;

    @ManyToOne(optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoEntity servico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estabelecimento_id", nullable = false)
    private EstabelecimentoEntity estabelecimento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteEntity cliente;

    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;

    @Column(nullable = false)
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgendamentoStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfissionalEntity getProfissional() {
        return profissional;
    }

    public void setProfissional(ProfissionalEntity profissional) {
        this.profissional = profissional;
    }

    public ServicoEntity getServico() {
        return servico;
    }

    public void setServico(ServicoEntity servico) {
        this.servico = servico;
    }

    public EstabelecimentoEntity getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(EstabelecimentoEntity estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public AgendamentoStatus getStatus() {
        return status;
    }

    public void setStatus(AgendamentoStatus status) {
        this.status = status;
    }
}

