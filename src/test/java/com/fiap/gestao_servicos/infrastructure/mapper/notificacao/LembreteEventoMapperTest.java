package com.fiap.gestao_servicos.infrastructure.mapper.notificacao;

import com.fiap.gestao_servicos.core.domain.LembreteDestinatario;
import com.fiap.gestao_servicos.core.domain.LembreteEvento;
import com.fiap.gestao_servicos.core.domain.LembreteTipo;
import com.fiap.gestao_servicos.core.domain.LembreteStatus;
import com.fiap.gestao_servicos.infrastructure.persistence.agendamento.AgendamentoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.cliente.ClienteEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.profissional.ProfissionalEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.notificacao.LembreteEventoEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LembreteEventoMapperTest {

    @Test
    void deverMapearEntityParaDominioComClienteEmailQuandoDestinatarioCliente() {
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(1L);
        
        ClienteEntity cliente = new ClienteEntity();
        cliente.setEmail("cliente@teste.com");
        agendamento.setCliente(cliente);

        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(10L);
        entity.setAgendamento(agendamento);
        entity.setTipo(LembreteTipo.T_24_HORAS);
        entity.setDestinatario(LembreteDestinatario.CLIENTE);
        entity.setStatus(LembreteStatus.PENDENTE);
        entity.setMensagem("Você tem um agendamento amanhã");
        LocalDateTime agora = LocalDateTime.now();
        entity.setCriadoEm(agora);
        entity.setEnviadoEm(null);
        entity.setErro(null);

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(10L, domain.getId());
        assertEquals(1L, domain.getAgendamentoId());
        assertEquals(LembreteTipo.T_24_HORAS, domain.getTipo());
        assertEquals(LembreteDestinatario.CLIENTE, domain.getDestinatario());
        assertEquals(LembreteStatus.PENDENTE, domain.getStatus());
        assertEquals("Você tem um agendamento amanhã", domain.getMensagem());
        assertEquals("cliente@teste.com", domain.getDestinoEmail());
        assertEquals(agora, domain.getCriadoEm());
        assertNull(domain.getEnviadoEm());
        assertNull(domain.getErro());
    }

    @Test
    void deverMapearEntityParaDominioComProfissionalEmailQuandoDestinatarioProfissional() {
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(2L);
        
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setEmail("profissional@teste.com");
        agendamento.setProfissional(profissional);

        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(11L);
        entity.setAgendamento(agendamento);
        entity.setTipo(LembreteTipo.T_2_HORAS);
        entity.setDestinatario(LembreteDestinatario.PROFISSIONAL);
        entity.setStatus(LembreteStatus.ENVIADO);
        entity.setMensagem("Você tem um atendimento em 2 horas");
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime enviado = agora.minusMinutes(10);
        entity.setCriadoEm(agora);
        entity.setEnviadoEm(enviado);
        entity.setErro(null);

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(11L, domain.getId());
        assertEquals(2L, domain.getAgendamentoId());
        assertEquals(LembreteTipo.T_2_HORAS, domain.getTipo());
        assertEquals(LembreteDestinatario.PROFISSIONAL, domain.getDestinatario());
        assertEquals(LembreteStatus.ENVIADO, domain.getStatus());
        assertEquals("profissional@teste.com", domain.getDestinoEmail());
        assertEquals(enviado, domain.getEnviadoEm());
    }

    @Test
    void deverOmitirEmailQuandoClienteNulo() {
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(3L);
        agendamento.setCliente(null);

        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(12L);
        entity.setAgendamento(agendamento);
        entity.setTipo(LembreteTipo.T_30_MINUTOS);
        entity.setDestinatario(LembreteDestinatario.CLIENTE);
        entity.setStatus(LembreteStatus.PENDENTE);
        entity.setMensagem("Lembrete");
        entity.setCriadoEm(LocalDateTime.now());

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(12L, domain.getId());
        assertEquals(3L, domain.getAgendamentoId());
        assertNull(domain.getDestinoEmail());
    }

    @Test
    void deverOmitirEmailQuandoProfissionalNulo() {
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(4L);
        agendamento.setProfissional(null);

        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(13L);
        entity.setAgendamento(agendamento);
        entity.setTipo(LembreteTipo.T_24_HORAS);
        entity.setDestinatario(LembreteDestinatario.PROFISSIONAL);
        entity.setStatus(LembreteStatus.PENDENTE);
        entity.setMensagem("Lembrete");
        entity.setCriadoEm(LocalDateTime.now());

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(13L, domain.getId());
        assertEquals(4L, domain.getAgendamentoId());
        assertNull(domain.getDestinoEmail());
    }

    @Test
    void deverOmitirEmailQuandoAgendamentoNulo() {
        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(14L);
        entity.setAgendamento(null);
        entity.setTipo(LembreteTipo.T_24_HORAS);
        entity.setDestinatario(LembreteDestinatario.CLIENTE);
        entity.setStatus(LembreteStatus.PENDENTE);
        entity.setMensagem("Lembrete");
        entity.setCriadoEm(LocalDateTime.now());

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(14L, domain.getId());
        assertNull(domain.getAgendamentoId());
        assertNull(domain.getDestinoEmail());
    }

    @Test
    void deverMapearDominioParaEntityComTodasAsCampos() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime enviado = agora.minusMinutes(5);

        LembreteEvento domain = new LembreteEvento(
                15L,
                5L,
                LembreteTipo.T_2_HORAS,
                LembreteDestinatario.CLIENTE,
                LembreteStatus.ENVIADO,
                "Mensagem de teste",
                "teste@teste.com",
                agora,
                enviado,
                null
        );

        LembreteEventoEntity entity = LembreteEventoMapper.toEntity(domain);

        assertEquals(15L, entity.getId());
        assertEquals(5L, entity.getAgendamento().getId());
        assertEquals(LembreteTipo.T_2_HORAS, entity.getTipo());
        assertEquals(LembreteDestinatario.CLIENTE, entity.getDestinatario());
        assertEquals(LembreteStatus.ENVIADO, entity.getStatus());
        assertEquals("Mensagem de teste", entity.getMensagem());
        assertEquals(agora, entity.getCriadoEm());
        assertEquals(enviado, entity.getEnviadoEm());
        assertNull(entity.getErro());
    }

    @Test
    void deverMapearDominioParaEntitySemIdQuandoIdNulo() {
        LocalDateTime agora = LocalDateTime.now();

        LembreteEvento domain = new LembreteEvento(
                null,
                6L,
                LembreteTipo.T_30_MINUTOS,
                LembreteDestinatario.PROFISSIONAL,
                LembreteStatus.PENDENTE,
                "Novo lembrete",
                "novo@teste.com",
                agora,
                null,
                null
        );

        LembreteEventoEntity entity = LembreteEventoMapper.toEntity(domain);

        assertNull(entity.getId());
        assertEquals(6L, entity.getAgendamento().getId());
        assertEquals(LembreteTipo.T_30_MINUTOS, entity.getTipo());
    }

    @Test
    void deverMapearDominioParaEntityComErro() {
        LocalDateTime agora = LocalDateTime.now();

        LembreteEvento domain = new LembreteEvento(
                16L,
                7L,
                LembreteTipo.T_24_HORAS,
                LembreteDestinatario.CLIENTE,
                LembreteStatus.FALHA,
                "Mensagem não enviada",
                "falha@teste.com",
                agora,
                null,
                "Timeout ao conectar com servidor SMTP"
        );

        LembreteEventoEntity entity = LembreteEventoMapper.toEntity(domain);

        assertEquals(16L, entity.getId());
        assertEquals(LembreteStatus.FALHA, entity.getStatus());
        assertEquals("Timeout ao conectar com servidor SMTP", entity.getErro());
        assertNull(entity.getEnviadoEm());
    }

    @Test
    void deverOmitirEmailClienteQuandoClienteExisteMasEmailNulo() {
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(8L);
        
        ClienteEntity cliente = new ClienteEntity();
        cliente.setEmail(null);
        agendamento.setCliente(cliente);

        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(17L);
        entity.setAgendamento(agendamento);
        entity.setTipo(LembreteTipo.T_24_HORAS);
        entity.setDestinatario(LembreteDestinatario.CLIENTE);
        entity.setStatus(LembreteStatus.PENDENTE);
        entity.setMensagem("Lembrete");
        entity.setCriadoEm(LocalDateTime.now());

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(17L, domain.getId());
        assertEquals(8L, domain.getAgendamentoId());
        assertNull(domain.getDestinoEmail());
    }

    @Test
    void deverOmitirEmailProfissionalQuandoProfissionalExisteMasEmailNulo() {
        AgendamentoEntity agendamento = new AgendamentoEntity();
        agendamento.setId(9L);
        
        ProfissionalEntity profissional = new ProfissionalEntity();
        profissional.setEmail(null);
        agendamento.setProfissional(profissional);

        LembreteEventoEntity entity = new LembreteEventoEntity();
        entity.setId(18L);
        entity.setAgendamento(agendamento);
        entity.setTipo(LembreteTipo.T_2_HORAS);
        entity.setDestinatario(LembreteDestinatario.PROFISSIONAL);
        entity.setStatus(LembreteStatus.PENDENTE);
        entity.setMensagem("Lembrete");
        entity.setCriadoEm(LocalDateTime.now());

        LembreteEvento domain = LembreteEventoMapper.toDomain(entity);

        assertEquals(18L, domain.getId());
        assertEquals(9L, domain.getAgendamentoId());
        assertNull(domain.getDestinoEmail());
    }
}
