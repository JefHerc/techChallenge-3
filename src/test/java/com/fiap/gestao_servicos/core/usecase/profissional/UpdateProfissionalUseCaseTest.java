package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProfissionalUseCaseTest {

    @Test
    void deveLancarExcecaoQuandoProfissionalNaoForEncontrado() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.update(10L, profissional("Novo", "52998224725", "11988887777", "novo@teste.com")));
        verify(repository, never()).update(anyLong(), any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfForDuplicado() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "atual@teste.com");
        Profissional novo = profissional("Novo", "06854809096", "11988887777", "atual@teste.com");

        when(repository.findById(1L)).thenReturn(Optional.of(atual));
        when(repository.existsByCpf("06854809096")).thenReturn(true);

        DuplicateDataException exception = assertThrows(DuplicateDataException.class, () -> useCase.update(1L, novo));

        assertEquals("Já existe um profissional com o mesmo CPF.", exception.getMessage());
        verify(repository, never()).update(1L, novo);
    }

    @Test
    void deveLancarExcecaoQuandoCelularForDuplicado() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "atual@teste.com");
        Profissional novo = profissional("Novo", "52998224725", "11999990000", "atual@teste.com");

        when(repository.findById(2L)).thenReturn(Optional.of(atual));
        when(repository.existsByCelular("11999990000")).thenReturn(true);

        DuplicateDataException exception = assertThrows(DuplicateDataException.class, () -> useCase.update(2L, novo));

        assertEquals("Já existe um profissional com o mesmo celular.", exception.getMessage());
        verify(repository, never()).update(2L, novo);
    }

    @Test
    void deveLancarExcecaoQuandoEmailForDuplicado() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "atual@teste.com");
        Profissional novo = profissional("Novo", "52998224725", "11988887777", "novo@teste.com");

        when(repository.findById(3L)).thenReturn(Optional.of(atual));
        when(repository.existsByEmail("novo@teste.com")).thenReturn(true);

        DuplicateDataException exception = assertThrows(DuplicateDataException.class, () -> useCase.update(3L, novo));

        assertEquals("Já existe um profissional com o mesmo email.", exception.getMessage());
        verify(repository, never()).update(3L, novo);
    }

    @Test
    void deveAtualizarQuandoNaoHouverDuplicidade() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "atual@teste.com");
        Profissional novo = profissional("Novo", "52998224725", "11988887777", "atual@teste.com");

        when(repository.findById(4L)).thenReturn(Optional.of(atual));
        when(repository.update(4L, novo)).thenReturn(novo);

        Profissional atualizado = useCase.update(4L, novo);

        assertEquals(novo, atualizado);
        verify(repository).update(4L, novo);
    }

    @Test
    void deveLancarExcecaoQuandoRepositorioRetornarNuloNoUpdate() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "atual@teste.com");
        Profissional novo = profissional("Novo", "52998224725", "11988887777", "atual@teste.com");

        when(repository.findById(5L)).thenReturn(Optional.of(atual));
        when(repository.update(5L, novo)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> useCase.update(5L, novo));
    }

    @Test
    void deveAtualizarQuandoDadosForemAlteradosSemDuplicidade() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "atual@teste.com");
        Profissional novo = profissional("Novo", "06854809096", "11999990000", "novo@teste.com");

        when(repository.findById(6L)).thenReturn(Optional.of(atual));
        when(repository.existsByCpf("06854809096")).thenReturn(false);
        when(repository.existsByCelular("11999990000")).thenReturn(false);
        when(repository.existsByEmail("novo@teste.com")).thenReturn(false);
        when(repository.update(6L, novo)).thenReturn(novo);

        Profissional atualizado = useCase.update(6L, novo);

        assertEquals(novo, atualizado);
        verify(repository).existsByCpf("06854809096");
        verify(repository).existsByCelular("11999990000");
        verify(repository).existsByEmail("novo@teste.com");
        verify(repository).update(6L, novo);
    }

    @Test
    void naoDeveConsultarDuplicidadeDeEmailQuandoMudarApenasCapitalizacao() {
        ProfissionalRepository repository = mock(ProfissionalRepository.class);
        UpdateProfissionalUseCase useCase = new UpdateProfissionalUseCase(repository);

        Profissional atual = profissional("Atual", "52998224725", "11988887777", "Atual@Teste.com");
        Profissional novo = profissional("Novo", "52998224725", "11988887777", "atual@teste.com");

        when(repository.findById(7L)).thenReturn(Optional.of(atual));
        when(repository.update(7L, novo)).thenReturn(novo);

        Profissional atualizado = useCase.update(7L, novo);

        assertEquals(novo, atualizado);
        verify(repository, never()).existsByEmail(any());
        verify(repository).update(7L, novo);
    }

    private Profissional profissional(String nome, String cpf, String celular, String email) {
        return new Profissional(
                null,
                nome,
                new Cpf(cpf),
                new Celular(celular),
                new Email(email),
                "https://img",
                "descricao",
                null,
                null,
                null
        );
    }
}
