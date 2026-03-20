package com.fiap.gestao_servicos.core.usecase.profissional;

import com.fiap.gestao_servicos.core.domain.Profissional;
import com.fiap.gestao_servicos.core.exception.DuplicateDataException;
import com.fiap.gestao_servicos.core.exception.ResourceNotFoundException;
import com.fiap.gestao_servicos.core.repository.ProfissionalRepository;

public class UpdateProfissionalUsecase {

    private final ProfissionalRepository profissionalRepository;

    public UpdateProfissionalUsecase(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public Profissional update(Long id, Profissional profissional) {
        Profissional profissionalAtual = profissionalRepository.findById(id);
        if (profissionalAtual == null) {
            throw new ResourceNotFoundException("Profissional não encontrado para o id: " + id);
        }

        validarDuplicidade(profissionalAtual, profissional);

        Profissional atualizado = profissionalRepository.update(id, profissional);
        if (atualizado == null) {
            throw new ResourceNotFoundException("Profissional não encontrado para o id: " + id);
        }

        return atualizado;
    }

    private void validarDuplicidade(Profissional profissionalAtual, Profissional novoProfissional) {
        String cpfAtual = profissionalAtual.getCpf().getValue();
        String cpfNovo = novoProfissional.getCpf().getValue();
        if (!cpfAtual.equals(cpfNovo) && profissionalRepository.existsByCpf(cpfNovo)) {
            throw new DuplicateDataException("Já existe um profissional com o mesmo CPF.");
        }

        String celularAtual = profissionalAtual.getCelular().getValue();
        String celularNovo = novoProfissional.getCelular().getValue();
        if (!celularAtual.equals(celularNovo) && profissionalRepository.existsByCelular(celularNovo)) {
            throw new DuplicateDataException("Já existe um profissional com o mesmo celular.");
        }

        String emailAtual = profissionalAtual.getEmail().getValue();
        String emailNovo = novoProfissional.getEmail().getValue();
        if (!emailAtual.equalsIgnoreCase(emailNovo) && profissionalRepository.existsByEmail(emailNovo)) {
            throw new DuplicateDataException("Já existe um profissional com o mesmo email.");
        }
    }
}