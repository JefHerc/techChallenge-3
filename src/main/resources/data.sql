INSERT INTO estabelecimento (
    nome,
    cnpj,
    logradouro,
    numero,
    complemento,
    bairro,
    cidade,
    estado,
    cep
)
SELECT
    'Studio Bela Vida',
    '64245654000168',
    'Avenida Paulista',
    '1000',
    'Sala 10',
    'Bela Vista',
    'São Paulo',
    'SP',
    '01310100'
WHERE NOT EXISTS (
    SELECT 1 FROM estabelecimento e WHERE e.cnpj = '64245654000168'
);

-- Insert horario(s) into estabelecimento_horarios collection table


INSERT INTO servico (nome, duracao_media, estabelecimento_id)
SELECT
    'CORTE',
    3600000000000,
    e.id
FROM estabelecimento e
WHERE e.cnpj = '64245654000168'
  AND NOT EXISTS (
      SELECT 1
      FROM servico s
    WHERE s.nome = 'CORTE'
        AND s.estabelecimento_id = e.id
  );

INSERT INTO servico (nome, duracao_media, estabelecimento_id)
SELECT
        'COLORACAO',
        5400000000000,
        e.id
FROM estabelecimento e
WHERE e.cnpj = '64245654000168'
    AND NOT EXISTS (
            SELECT 1
            FROM servico s
            WHERE s.nome = 'COLORACAO'
                AND s.estabelecimento_id = e.id
    );

INSERT INTO profissional (nome, cpf, celular, email, url_foto, descricao, estabelecimento_id)
SELECT
    'Carla Souza',
    '06854809096',
    '11988887777',
    'carla@studio.com',
    NULL,
    'Especialista em corte e coloração',
    e.id
FROM estabelecimento e
WHERE e.cnpj = '64245654000168'
  AND NOT EXISTS (
      SELECT 1
      FROM profissional p
      WHERE p.cpf = '06854809096'
  );

INSERT INTO servico_profissional (servico_id, profissional_id, valor)
SELECT
    s.id,
    p.id,
    120.00
FROM servico s
JOIN estabelecimento e ON e.id = s.estabelecimento_id
JOIN profissional p ON p.estabelecimento_id = e.id
WHERE e.cnpj = '64245654000168'
    AND s.nome = 'CORTE'
  AND p.cpf = '06854809096'
  AND NOT EXISTS (
      SELECT 1
      FROM servico_profissional sp
            WHERE sp.profissional_id = p.id
                AND sp.servico_id = s.id
    );

INSERT INTO servico_profissional (servico_id, profissional_id, valor)
SELECT
        s.id,
        p.id,
        180.00
FROM servico s
JOIN estabelecimento e ON e.id = s.estabelecimento_id
JOIN profissional p ON p.estabelecimento_id = e.id
WHERE e.cnpj = '64245654000168'
    AND s.nome = 'COLORACAO'
    AND p.cpf = '06854809096'
    AND NOT EXISTS (
            SELECT 1
            FROM servico_profissional sp
            WHERE sp.profissional_id = p.id
                AND sp.servico_id = s.id
  );

-- Set sexo for the profissional (if column exists)
UPDATE profissional p
JOIN estabelecimento e ON e.id = p.estabelecimento_id
SET p.sexo = 'FEMININO'
WHERE e.cnpj = '64245654000168'
    AND p.cpf = '06854809096'
    AND (p.sexo IS NULL OR p.sexo = '');

-- Insert expediente_profissional entries for the profissional (three sample days)
INSERT INTO expediente_profissional (profissional_id, dia_semana, inicio_turno, fim_turno, inicio_intervalo, fim_intervalo)
SELECT
        p.id,
        'MONDAY',
        '08:00:00',
        '12:00:00',
        '10:00:00',
        '10:15:00'
FROM profissional p
JOIN estabelecimento e ON e.id = p.estabelecimento_id
WHERE e.cnpj = '64245654000168'
    AND p.cpf = '06854809096'
    AND NOT EXISTS (
            SELECT 1 FROM expediente_profissional ep WHERE ep.profissional_id = p.id AND ep.dia_semana = 'MONDAY'
    );

INSERT INTO expediente_profissional (profissional_id, dia_semana, inicio_turno, fim_turno, inicio_intervalo, fim_intervalo)
SELECT
        p.id,
        'WEDNESDAY',
        '13:00:00',
        '17:00:00',
        NULL,
        NULL
FROM profissional p
JOIN estabelecimento e ON e.id = p.estabelecimento_id
WHERE e.cnpj = '64245654000168'
    AND p.cpf = '06854809096'
    AND NOT EXISTS (
            SELECT 1 FROM expediente_profissional ep WHERE ep.profissional_id = p.id AND ep.dia_semana = 'WEDNESDAY'
    );

INSERT INTO expediente_profissional (profissional_id, dia_semana, inicio_turno, fim_turno, inicio_intervalo, fim_intervalo)
SELECT
        p.id,
        'FRIDAY',
        '09:00:00',
        '15:00:00',
        '12:00:00',
        '13:00:00'
FROM profissional p
JOIN estabelecimento e ON e.id = p.estabelecimento_id
WHERE e.cnpj = '64245654000168'
    AND p.cpf = '06854809096'
    AND NOT EXISTS (
            SELECT 1 FROM expediente_profissional ep WHERE ep.profissional_id = p.id AND ep.dia_semana = 'FRIDAY'
    );

INSERT INTO cliente (nome, cpf, celular, email, sexo)
SELECT
    'João Silva',
    '34028317088',
    '11999998888',
    'joao@cliente.com',
    'MASCULINO'
WHERE NOT EXISTS (
    SELECT 1 FROM cliente c WHERE c.cpf = '34028317088'
);

INSERT INTO agendamento (
    profissional_id,
    servico_id,
    estabelecimento_id,
    cliente_id,
    data_hora_inicio,
    data_hora_fim,
    status
)
SELECT
    p.id,
    s.id,
    e.id,
    c.id,
    DATE_SUB(NOW(), INTERVAL 1 DAY),
    DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 1 HOUR,
    'CONCLUIDO'
FROM profissional p
JOIN estabelecimento e ON e.id = p.estabelecimento_id
JOIN servico s ON s.estabelecimento_id = e.id AND s.nome = 'CORTE'
JOIN cliente c ON c.cpf = '34028317088'
WHERE p.cpf = '06854809096'
  AND NOT EXISTS (
      SELECT 1
      FROM agendamento a
      WHERE a.profissional_id = p.id
        AND a.status = 'CONCLUIDO'
  );

INSERT INTO agendamento (
    profissional_id,
    servico_id,
    estabelecimento_id,
    cliente_id,
    data_hora_inicio,
    data_hora_fim,
    status
)
SELECT
    p.id,
    s.id,
    e.id,
    c.id,
    DATE_ADD(NOW(), INTERVAL 1 DAY),
    DATE_ADD(NOW(), INTERVAL 1 DAY) + INTERVAL 1 HOUR,
    'AGENDADO'
FROM profissional p
JOIN estabelecimento e ON e.id = p.estabelecimento_id
JOIN servico s ON s.estabelecimento_id = e.id AND s.nome = 'CORTE'
JOIN cliente c ON c.cpf = '34028317088'
WHERE p.cpf = '06854809096'
  AND NOT EXISTS (
      SELECT 1
      FROM agendamento a
      WHERE a.profissional_id = p.id
        AND a.status = 'AGENDADO'
  );

INSERT INTO avaliacao (
    agendamento_id,
    nota_estabelecimento,
    nota_profissional,
    comentario_estabelecimento,
    comentario_profissional
)
SELECT
    a.id,
    4.8,
    5.0,
    'Ambiente excelente e atendimento rápido.',
    'Profissional muito atenciosa e técnica.'
FROM agendamento a
JOIN profissional p ON p.id = a.profissional_id
WHERE p.cpf = '06854809096'
  AND a.status = 'CONCLUIDO'
  AND NOT EXISTS (
      SELECT 1
      FROM avaliacao av
      WHERE av.agendamento_id = a.id
  );
