INSERT INTO estabelecimento (
    id,
    nome,
    cnpj,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    estado,
    cep
) VALUES (
    1,
    'Studio BDD',
    '22345678000149',
    'Rua Teste',
    '100',
    'Sala 1',
    'Centro',
    'Sao Paulo',
    'SP',
    '01001000'
);

INSERT INTO estabelecimento_horarios (estabelecimento_id, dia_semana, abertura, fechamento, fechado) VALUES
    (1, 'MONDAY', '08:00:00', '18:00:00', FALSE),
    (1, 'TUESDAY', '08:00:00', '18:00:00', FALSE),
    (1, 'WEDNESDAY', '08:00:00', '18:00:00', FALSE),
    (1, 'THURSDAY', '08:00:00', '18:00:00', FALSE),
    (1, 'FRIDAY', '08:00:00', '18:00:00', FALSE),
    (1, 'SATURDAY', '08:00:00', '16:00:00', FALSE),
    (1, 'SUNDAY', '00:00:00', '00:00:00', TRUE);

INSERT INTO servico (id, nome, duracao_media, estabelecimento_id)
VALUES (1, 'CORTE', 3600000000000, 1);

INSERT INTO profissional (id, nome, cpf, celular, email, url_foto, descricao, sexo, estabelecimento_id)
VALUES (1, 'Profissional BDD', '31415926590', '11990000000', 'profissional.bdd@local.test', NULL, 'Profissional base para BDD', 'FEMININO', 1);

INSERT INTO cliente (id, nome, cpf, celular, email, sexo)
VALUES (1, 'Cliente Base BDD', '12345678909', '11991111111', 'cliente.base@local.test', 'FEMININO');

INSERT INTO servico_profissional (id, servico_id, profissional_id, valor)
VALUES (1, 1, 1, 100.00);