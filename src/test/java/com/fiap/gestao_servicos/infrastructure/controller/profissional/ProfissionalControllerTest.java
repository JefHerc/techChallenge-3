package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindAllProfissionaisUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.FindProfissionalByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalNoEstabelecimentoUseCase;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfissionalControllerTest {

    @Test
    void deveCriarProfissionalQuandoExpedienteEstiverDentroDoHorarioDeFuncionamento() {
        CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase = mock(CreateProfissionalNoEstabelecimentoUseCase.class);
        FindAllProfissionaisUseCase findAllProfissionaisUseCase = mock(FindAllProfissionaisUseCase.class);
        FindProfissionalByIdUseCase findProfissionalByIdUseCase = mock(FindProfissionalByIdUseCase.class);
        UpdateProfissionalNoEstabelecimentoUseCase updateProfissionalNoEstabelecimentoUseCase = mock(UpdateProfissionalNoEstabelecimentoUseCase.class);
        DeleteProfissionalNoEstabelecimentoUseCase deleteProfissionalNoEstabelecimentoUseCase = mock(DeleteProfissionalNoEstabelecimentoUseCase.class);
        ProfissionalController controller = new ProfissionalController(
                createProfissionalNoEstabelecimentoUseCase,
                findAllProfissionaisUseCase,
                findProfissionalByIdUseCase,
                updateProfissionalNoEstabelecimentoUseCase,
                deleteProfissionalNoEstabelecimentoUseCase
        );

        Long estabelecimentoId = 1L;
        Profissional profissionalCriado = new Profissional(
                10L,
                "Maria",
                new Cpf("52998224725"),
                new Celular("11987654321"),
                new Email("maria@teste.com"),
                null,
                "Descricao",
                java.util.List.of(new ExpedienteProfissional(
                        null,
                        DayOfWeek.MONDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0)
                )),
                Sexo.FEMININO,
                java.util.List.of()
        );
        when(createProfissionalNoEstabelecimentoUseCase.create(eq(estabelecimentoId), any(Profissional.class))).thenReturn(profissionalCriado);

        ProfissionalResponseDto response = controller.criar(estabelecimentoId, criarProfissionalDto(
                "Maria",
                "52998224725",
                "11987654321",
                "maria@teste.com",
                "FEMININO",
                "MONDAY",
                "09:00",
                "17:00",
                "12:00",
                "13:00"
        )).getBody();

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Maria", response.getNome());
        verify(createProfissionalNoEstabelecimentoUseCase).create(eq(estabelecimentoId), any(Profissional.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarProfissionalComExpedienteForaDoHorarioDeFuncionamento() {
        CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase = mock(CreateProfissionalNoEstabelecimentoUseCase.class);
        FindAllProfissionaisUseCase findAllProfissionaisUseCase = mock(FindAllProfissionaisUseCase.class);
        FindProfissionalByIdUseCase findProfissionalByIdUseCase = mock(FindProfissionalByIdUseCase.class);
        UpdateProfissionalNoEstabelecimentoUseCase updateProfissionalNoEstabelecimentoUseCase = mock(UpdateProfissionalNoEstabelecimentoUseCase.class);
        DeleteProfissionalNoEstabelecimentoUseCase deleteProfissionalNoEstabelecimentoUseCase = mock(DeleteProfissionalNoEstabelecimentoUseCase.class);
        ProfissionalController controller = new ProfissionalController(
                createProfissionalNoEstabelecimentoUseCase,
                findAllProfissionaisUseCase,
                findProfissionalByIdUseCase,
                updateProfissionalNoEstabelecimentoUseCase,
                deleteProfissionalNoEstabelecimentoUseCase
        );

        Long estabelecimentoId = 1L;
        Long profissionalId = 5L;

        org.mockito.Mockito.doThrow(new IllegalArgumentException(
                "Expediente do profissional no dia MONDAY deve estar dentro do horario de funcionamento do estabelecimento"
        )).when(updateProfissionalNoEstabelecimentoUseCase).update(eq(estabelecimentoId), eq(profissionalId), any(Profissional.class));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.atualizar(estabelecimentoId, profissionalId, criarProfissionalDto(
                        "Joao",
                        "52998224725",
                        "11912345678",
                        "joao@teste.com",
                        "MASCULINO",
                        "MONDAY",
                        "07:00",
                        "17:00",
                        null,
                        null
                ))
        );

        assertEquals(
                "Expediente do profissional no dia MONDAY deve estar dentro do horario de funcionamento do estabelecimento",
                ex.getMessage()
        );
        verify(updateProfissionalNoEstabelecimentoUseCase).update(eq(estabelecimentoId), eq(profissionalId), any(Profissional.class));
    }

    @Test
    void deveDeletarProfissionalSemAtualizarEstabelecimento() {
        CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase = mock(CreateProfissionalNoEstabelecimentoUseCase.class);
        FindAllProfissionaisUseCase findAllProfissionaisUseCase = mock(FindAllProfissionaisUseCase.class);
        FindProfissionalByIdUseCase findProfissionalByIdUseCase = mock(FindProfissionalByIdUseCase.class);
        UpdateProfissionalNoEstabelecimentoUseCase updateProfissionalNoEstabelecimentoUseCase = mock(UpdateProfissionalNoEstabelecimentoUseCase.class);
        DeleteProfissionalNoEstabelecimentoUseCase deleteProfissionalNoEstabelecimentoUseCase = mock(DeleteProfissionalNoEstabelecimentoUseCase.class);
        ProfissionalController controller = new ProfissionalController(
                createProfissionalNoEstabelecimentoUseCase,
                findAllProfissionaisUseCase,
                findProfissionalByIdUseCase,
                updateProfissionalNoEstabelecimentoUseCase,
                deleteProfissionalNoEstabelecimentoUseCase
        );

        Long estabelecimentoId = 1L;
        Long profissionalId = 5L;

        controller.deletar(estabelecimentoId, profissionalId);

        verify(deleteProfissionalNoEstabelecimentoUseCase).deleteById(estabelecimentoId, profissionalId);
        verify(createProfissionalNoEstabelecimentoUseCase, never()).create(eq(estabelecimentoId), any(Profissional.class));
    }

    private static ProfissionalDto criarProfissionalDto(String nome,
                                                        String cpf,
                                                        String celular,
                                                        String email,
                                                        String sexo,
                                                        String diaSemana,
                                                        String inicioTurno,
                                                        String fimTurno,
                                                        String inicioIntervalo,
                                                        String fimIntervalo) {
        ProfissionalDto dto = new ProfissionalDto();
        dto.setNome(nome);
        dto.setCpf(cpf);
        dto.setCelular(celular);
        dto.setEmail(email);
        dto.setDescricao("Descricao");
        dto.setSexo(sexo);

        ExpedienteProfissionalDto expediente = new ExpedienteProfissionalDto();
        expediente.setDiaSemana(diaSemana);
        expediente.setInicioTurno(inicioTurno);
        expediente.setFimTurno(fimTurno);
        expediente.setInicioIntervalo(inicioIntervalo);
        expediente.setFimIntervalo(fimIntervalo);

        dto.setExpedientes(java.util.List.of(expediente));
        dto.setServicosProfissional(java.util.List.of());
        return dto;
    }
}


