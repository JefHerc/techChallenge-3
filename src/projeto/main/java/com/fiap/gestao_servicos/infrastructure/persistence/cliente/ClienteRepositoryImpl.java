package com.fiap.gestao_servicos.infrastructure.persistence.cliente;

import com.fiap.gestao_servicos.core.domain.Celular;
import com.fiap.gestao_servicos.core.domain.Cliente;
import com.fiap.gestao_servicos.core.repository.ClienteRepository;
import com.fiap.gestao_servicos.core.domain.Cpf;
import com.fiap.gestao_servicos.core.domain.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteRepositoryJpa jpaRepository;

    public ClienteRepositoryImpl(ClienteRepositoryJpa jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Cliente create(Cliente cliente) {
        ClienteEntity entity = toEntity(cliente);
        ClienteEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional
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
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(this::toDomain);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
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

