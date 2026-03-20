package com.fiap.gestao_servicos.core.repository;

import com.fiap.gestao_servicos.core.domain.Profissional;

import java.util.List;

public interface ProfissionalRepository {

    Profissional create(Profissional profissional);
    Profissional update(Long id, Profissional profissional);
    void deleteById(Long id);
    List<Profissional> findAll();
    Profissional findById(Long id);
    boolean existsById(Long id);
    boolean existsByCpf(String cpf);
    boolean existsByCelular(String celular);
    boolean existsByEmail(String email);
}