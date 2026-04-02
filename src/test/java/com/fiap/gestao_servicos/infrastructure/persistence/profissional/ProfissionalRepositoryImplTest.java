package com.fiap.gestao_servicos.infrastructure.persistence.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.core.domain.Servico;
import com.fiap.gestao_servicos.core.domain.ServicoEnum;
import com.fiap.gestao_servicos.core.domain.ServicoProfissional;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoEntity;
import com.fiap.gestao_servicos.infrastructure.persistence.servico.ServicoRepositoryJpa;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfissionalRepositoryImplTest {

    @Test
    void devePersistirExpedientesEServicosAoAtualizarProfissional() {
        ProfissionalRepositoryJpa profissionalRepositoryJpa = mock(ProfissionalRepositoryJpa.class);
        ServicoRepositoryJpa servicoRepositoryJpa = mock(ServicoRepositoryJpa.class);
        ProfissionalRepositoryImpl repository = new ProfissionalRepositoryImpl(profissionalRepositoryJpa, servicoRepositoryJpa);

        ProfissionalEntity existente = new ProfissionalEntity();
        existente.setId(10L);
        existente.setNome("Profissional Base");
        existente.setCpf("52284927041");
        existente.setCelular("19966660006");
        existente.setEmail("base@teste.com");
        existente.setExpedientes(List.of());
        existente.setServicosProfissional(List.of());

        ServicoEntity servicoEntity = new ServicoEntity();
        servicoEntity.setId(16L);
        servicoEntity.setNome("CORTE");
        servicoEntity.setDuracaoMedia(Duration.ofMinutes(45));

        when(profissionalRepositoryJpa.findById(10L)).thenReturn(Optional.of(existente));
        when(servicoRepositoryJpa.getReferenceById(16L)).thenReturn(servicoEntity);
        when(profissionalRepositoryJpa.save(any(ProfissionalEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Profissional profissionalAtualizado = new Profissional(
                10L,
                "Henrique Luz",
                new Cpf("52284927041"),
                new Celular("19966660006"),
                new Email("henrique@e3.com"),
                "https://cdn.exemplo.com/fotos/roberval.jpg",
                "Corte moderno e barba",
                List.of(new ExpedienteProfissional(
                        null,
                        DayOfWeek.TUESDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(18, 0),
                        null,
                        null
                )),
                Sexo.MASCULINO,
                List.of(new ServicoProfissional(
                        null,
                        new Servico(16L, ServicoEnum.CORTE, Duration.ofMinutes(45)),
                        null,
                        new BigDecimal("95.60")
                ))
        );

        Profissional atualizado = repository.update(10L, profissionalAtualizado);

        ArgumentCaptor<ProfissionalEntity> captor = ArgumentCaptor.forClass(ProfissionalEntity.class);
        verify(profissionalRepositoryJpa).save(captor.capture());
        ProfissionalEntity salvo = captor.getValue();

        assertNotNull(atualizado);
        assertEquals(1, salvo.getExpedientes().size());
        assertEquals("TUESDAY", salvo.getExpedientes().get(0).getDiaSemana());
        assertEquals(1, salvo.getServicosProfissional().size());
        assertEquals(16L, salvo.getServicosProfissional().get(0).getServico().getId());
        assertEquals(new BigDecimal("95.60"), salvo.getServicosProfissional().get(0).getValor());
    }
}
