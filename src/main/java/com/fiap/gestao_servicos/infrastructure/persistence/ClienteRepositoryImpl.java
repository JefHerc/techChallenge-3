package com.fiap.gestao_servicos.infrastructure.persistence;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteRepositoryJpa jpaRepository;

    public ClienteRepositoryImpl(ClienteRepositoryJpa jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente create(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);
        ClienteEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Cliente update(Long id, Cliente cliente) {
        ClienteEntity existing = jpaRepository.findById(id)
                .orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setNome(cliente.getNome());
        existing.setCpf(cliente.getCpf().getValue());
        existing.setCelular(cliente.getCelular().getValue());
        existing.setEmail(cliente.getEmail().getValue());
        existing.setSexo(cliente.getSexo());

        ClienteEntity updated = jpaRepository.save(existing);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Cliente> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Cliente findById(Long id) {
        ClienteEntity entity = jpaRepository.findById(id)
                .orElse(null);
        if (entity == null) {
            return null;
        }
        return toDomain(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByCelular(String celular) {
        return jpaRepository.existsByCelular(celular);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNome(cliente.getNome());
        entity.setCpf(cliente.getCpf().getValue());
        entity.setCelular(cliente.getCelular().getValue());
        entity.setEmail(cliente.getEmail().getValue());
        entity.setSexo(cliente.getSexo());
        return entity;
    }

    private Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNome(),
                new Cpf(entity.getCpf()),
                new Celular(entity.getCelular()),
                new Email(entity.getEmail()),
                entity.getSexo()
        );
    }
}