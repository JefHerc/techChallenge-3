package com.fiap.gestao_servicos.infrastructure.controller.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cnpj;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Endereco;
import com.fiap.gestao_servicos.core.domain.Estabelecimento;
import com.fiap.gestao_servicos.core.domain.ExpedienteProfissional;
import com.fiap.gestao_servicos.core.domain.HorarioFuncionamento;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.domain.Sexo;
import com.fiap.gestao_servicos.core.usecase.profissional.CreateProfissionalNoEstabelecimentoUseCase;
import com.fiap.gestao_servicos.core.usecase.estabelecimento.FindEstabelecimentoByIdUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.DeleteProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.UpdateProfissionalUseCase;
import com.fiap.gestao_servicos.core.usecase.profissional.VerifyProfissionalVinculoUseCase;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

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
        FindEstabelecimentoByIdUseCase findUseCase = mock(FindEstabelecimentoByIdUseCase.class);
        CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase = mock(CreateProfissionalNoEstabelecimentoUseCase.class);
        VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase = mock(VerifyProfissionalVinculoUseCase.class);
        UpdateProfissionalUseCase updateProfissionalUseCase = mock(UpdateProfissionalUseCase.class);
        DeleteProfissionalUseCase deleteProfissionalUseCase = mock(DeleteProfissionalUseCase.class);
        ProfissionalController controller = new ProfissionalController(
                findUseCase,
                createProfissionalNoEstabelecimentoUseCase,
                verifyProfissionalVinculoUseCase,
                updateProfissionalUseCase,
                deleteProfissionalUseCase
        );

        Long estabelecimentoId = 1L;
        Profissional profissionalCriado = criarProfissional(
                10L,
                "Maria",
                "maria@teste.com",
                "11987654321",
                Sexo.FEMININO,
                new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), LocalTime.of(12, 0), LocalTime.of(13, 0))
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
        FindEstabelecimentoByIdUseCase findUseCase = mock(FindEstabelecimentoByIdUseCase.class);
        CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase = mock(CreateProfissionalNoEstabelecimentoUseCase.class);
        VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase = mock(VerifyProfissionalVinculoUseCase.class);
        UpdateProfissionalUseCase updateProfissionalUseCase = mock(UpdateProfissionalUseCase.class);
        DeleteProfissionalUseCase deleteProfissionalUseCase = mock(DeleteProfissionalUseCase.class);
        ProfissionalController controller = new ProfissionalController(
                findUseCase,
                createProfissionalNoEstabelecimentoUseCase,
                verifyProfissionalVinculoUseCase,
                updateProfissionalUseCase,
                deleteProfissionalUseCase
        );

        Long estabelecimentoId = 1L;
        Long profissionalId = 5L;
        Estabelecimento estabelecimento = criarEstabelecimento(
                List.of(criarProfissional(
                        profissionalId,
                        "Joao",
                        "joao@teste.com",
                        "11912345678",
                        Sexo.MASCULINO,
                        new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), null, null)
                )),
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );

        when(findUseCase.findById(estabelecimentoId)).thenReturn(estabelecimento);
        org.mockito.Mockito.doThrow(new IllegalArgumentException(
                "Expediente do profissional no dia MONDAY deve estar dentro do horario de funcionamento do estabelecimento"
        )).when(verifyProfissionalVinculoUseCase).verify(any(Estabelecimento.class), any(Profissional.class));

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
        verify(updateProfissionalUseCase, never()).update(eq(profissionalId), any(Profissional.class));
    }

    @Test
    void deveDeletarProfissionalSemAtualizarEstabelecimento() {
        FindEstabelecimentoByIdUseCase findUseCase = mock(FindEstabelecimentoByIdUseCase.class);
        CreateProfissionalNoEstabelecimentoUseCase createProfissionalNoEstabelecimentoUseCase = mock(CreateProfissionalNoEstabelecimentoUseCase.class);
        VerifyProfissionalVinculoUseCase verifyProfissionalVinculoUseCase = mock(VerifyProfissionalVinculoUseCase.class);
        UpdateProfissionalUseCase updateProfissionalUseCase = mock(UpdateProfissionalUseCase.class);
        DeleteProfissionalUseCase deleteProfissionalUseCase = mock(DeleteProfissionalUseCase.class);
        ProfissionalController controller = new ProfissionalController(
                findUseCase,
                createProfissionalNoEstabelecimentoUseCase,
                verifyProfissionalVinculoUseCase,
                updateProfissionalUseCase,
                deleteProfissionalUseCase
        );

        Long estabelecimentoId = 1L;
        Long profissionalId = 5L;
        Estabelecimento estabelecimento = criarEstabelecimento(
                List.of(criarProfissional(
                        profissionalId,
                        "Joao",
                        "joao@teste.com",
                        "11912345678",
                        Sexo.MASCULINO,
                        new ExpedienteProfissional(null, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0), null, null)
                )),
                List.of(new HorarioFuncionamento(null, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(18, 0), false))
        );

        when(findUseCase.findById(estabelecimentoId)).thenReturn(estabelecimento);

        controller.deletar(estabelecimentoId, profissionalId);

        verify(deleteProfissionalUseCase).deleteById(profissionalId);
                verify(createProfissionalNoEstabelecimentoUseCase, never()).create(eq(estabelecimentoId), any(Profissional.class));
    }

    private static Estabelecimento criarEstabelecimento(List<Profissional> profissionais, List<HorarioFuncionamento> horarios) {
        return new Estabelecimento(
                1L,
                "Salao",
                new Endereco("Rua A", "10", null, "Centro", "Sao Paulo", "SP", "01001000"),
                profissionais,
                List.of(),
                new Cnpj("11222333000181"),
                List.of(),
                horarios
        );
    }

    private static Profissional criarProfissional(Long id,
                                                  String nome,
                                                  String email,
                                                  String celular,
                                                  Sexo sexo,
                                                  ExpedienteProfissional expediente) {
        return new Profissional(
                id,
                nome,
                new Cpf("52998224725"),
                new Celular(celular),
                new Email(email),
                null,
                "Descricao",
                List.of(expediente),
                sexo,
                List.of()
        );
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

        dto.setExpedientes(List.of(expediente));
        dto.setServicosProfissional(List.of());
        return dto;
    }
}


