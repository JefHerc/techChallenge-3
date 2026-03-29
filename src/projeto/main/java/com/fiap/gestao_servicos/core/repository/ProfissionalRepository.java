package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfissionalRepository {

    Profissional create(Profissional profissional);
    Profissional update(Long id, Profissional profissional);
    void deleteById(Long id);
    Page<Profissional> findAll(Pageable pageable);
    Optional<Profissional> findById(Long id);
    boolean existsById(Long id);
    boolean existsByCpf(String cpf);
    boolean existsByCelular(String celular);
    boolean existsByEmail(String email);
    boolean existsVinculoProfissionalServico(Long profissionalId, Long servicoId);
}

