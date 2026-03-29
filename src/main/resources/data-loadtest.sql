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
    'Load Test Studio',
    '11444777000161',
    'Rua Performance',
    '100',
    'Sala 1',
    'Centro',
    'Sao Paulo',
    'SP',
    '01001000'
);

INSERT INTO estabelecimento_horarios (estabelecimento_id, dia_semana, abertura, fechamento, fechado) VALUES
    (1, 'MONDAY', '00:00:00', '23:59:00', FALSE),
    (1, 'TUESDAY', '00:00:00', '23:59:00', FALSE),
    (1, 'WEDNESDAY', '00:00:00', '23:59:00', FALSE),
    (1, 'THURSDAY', '00:00:00', '23:59:00', FALSE),
    (1, 'FRIDAY', '00:00:00', '23:59:00', FALSE),
    (1, 'SATURDAY', '00:00:00', '23:59:00', FALSE),
    (1, 'SUNDAY', '00:00:00', '23:59:00', FALSE);

INSERT INTO servico (id, nome, duracao_media, estabelecimento_id)
VALUES (1, 'CORTE', 3600000000000, 1);

INSERT INTO profissional (id, nome, cpf, celular, email, url_foto, descricao, sexo, estabelecimento_id) VALUES
    (1, 'Profissional Carga 1', '52998224725', '11990000000', 'profissional.carga1@local.test', NULL, 'Profissional para carga', 'FEMININO', 1),
    (2, 'Profissional Carga 2', '39053344705', '11990000001', 'profissional.carga2@local.test', NULL, 'Profissional para carga', 'MASCULINO', 1),
    (3, 'Profissional Carga 3', '16899535009', '11990000002', 'profissional.carga3@local.test', NULL, 'Profissional para carga', 'FEMININO', 1),
    (4, 'Profissional Carga 4', '45317828791', '11990000003', 'profissional.carga4@local.test', NULL, 'Profissional para carga', 'MASCULINO', 1),
    (5, 'Profissional Carga 5', '11144477735', '11990000004', 'profissional.carga5@local.test', NULL, 'Profissional para carga', 'FEMININO', 1);

INSERT INTO cliente (id, nome, cpf, celular, email, sexo)
VALUES (1, 'Cliente Carga', '12345678909', '11991111111', 'cliente.carga@local.test', 'MASCULINO');

INSERT INTO servico_profissional (id, servico_id, profissional_id, valor) VALUES
    (1, 1, 1, 100.00),
    (2, 1, 2, 100.00),
    (3, 1, 3, 100.00),
    (4, 1, 4, 100.00),
    (5, 1, 5, 100.00);

