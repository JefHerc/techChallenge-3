
-- ============================================================
-- CLIENTES
-- ============================================================

INSERT INTO cliente (nome, cpf, celular, email, sexo)
SELECT 'João Silva',    '34028317088', '11999998888', 'joao@cliente.com',    'MASCULINO'
WHERE NOT EXISTS (SELECT 1 FROM cliente c WHERE c.cpf = '34028317088');

INSERT INTO cliente (nome, cpf, celular, email, sexo)
SELECT 'Maria Oliveira','52998224725', '11988887776', 'maria@cliente.com',   'FEMININO'
WHERE NOT EXISTS (SELECT 1 FROM cliente c WHERE c.cpf = '52998224725');

INSERT INTO cliente (nome, cpf, celular, email, sexo)
SELECT 'Pedro Alves',   '39053344705', '11977776665', 'pedro@cliente.com',   'MASCULINO'
WHERE NOT EXISTS (SELECT 1 FROM cliente c WHERE c.cpf = '39053344705');

    -- ============================================================
    -- ESTABELECIMENTO 1 — Studio Bela Vida
    -- ============================================================

    INSERT INTO estabelecimento (nome, cnpj, logradouro, numero, complemento, bairro, cidade, estado, cep)
    SELECT 'Studio Bela Vida', '64245654000168', 'Avenida Paulista', '1000', 'Sala 10', 'Bela Vista', 'São Paulo', 'SP', '01310100'
    WHERE NOT EXISTS (SELECT 1 FROM estabelecimento e WHERE e.cnpj = '64245654000168');

    INSERT INTO estabelecimento_horarios (estabelecimento_id, dia_semana, abertura, fechamento, fechado)
    SELECT e.id, d.dia, '08:00:00', '20:00:00', FALSE
    FROM estabelecimento e
    JOIN (SELECT 'MONDAY' AS dia UNION ALL SELECT 'TUESDAY' UNION ALL SELECT 'WEDNESDAY'
                UNION ALL SELECT 'THURSDAY' UNION ALL SELECT 'FRIDAY' UNION ALL SELECT 'SATURDAY') d
    WHERE e.cnpj = '64245654000168'
        AND NOT EXISTS (SELECT 1 FROM estabelecimento_horarios eh WHERE eh.estabelecimento_id = e.id AND eh.dia_semana = d.dia);

    -- Serviços do estabelecimento 1
    INSERT INTO servico (nome, duracao_media, estabelecimento_id)
    SELECT s.nome, s.dur, e.id FROM estabelecimento e
    JOIN (SELECT 'CORTE' AS nome, 3600000000000 AS dur
                UNION ALL SELECT 'COLORACAO',   5400000000000
                UNION ALL SELECT 'HIDRATACAO',  2700000000000
                UNION ALL SELECT 'ESCOVA',      1800000000000
                UNION ALL SELECT 'MANICURE',    2700000000000) s
    WHERE e.cnpj = '64245654000168'
        AND NOT EXISTS (SELECT 1 FROM servico sv WHERE sv.nome = s.nome AND sv.estabelecimento_id = e.id);

    -- Profissionais do estabelecimento 1
    INSERT INTO profissional (nome, cpf, celular, email, url_foto, descricao, sexo, estabelecimento_id)
    SELECT p.nome, p.cpf, p.cel, p.email, NULL, p.descricao, p.sexo, e.id
    FROM estabelecimento e
    JOIN (SELECT 'Carla Souza'    AS nome, '06854809096' AS cpf, '11988887777' AS cel, 'carla@e1.com'    AS email, 'Especialista coloração'   AS descricao, 'FEMININO'  AS sexo
                UNION ALL SELECT 'Ana Lima',          '11144477735', '11988887778', 'ana@e1.com',     'Especialista hidratação',  'FEMININO'
                UNION ALL SELECT 'Bruno Costa',       '16899535009', '11988887779', 'bruno@e1.com',   'Especialista corte masc',  'MASCULINO'
                UNION ALL SELECT 'Débora Melo',       '45317828791', '11988887780', 'debora@e1.com',  'Manicure e pedicure',      'FEMININO') p
    WHERE e.cnpj = '64245654000168'
        AND NOT EXISTS (SELECT 1 FROM profissional pf WHERE pf.cpf = p.cpf);

    -- Expediente — profissionais do estabelecimento 1
    INSERT INTO expediente_profissional (profissional_id, dia_semana, inicio_turno, fim_turno, inicio_intervalo, fim_intervalo)
    SELECT pf.id, d.dia, '08:00:00', '18:00:00', '12:00:00', '13:00:00'
    FROM profissional pf
    JOIN estabelecimento e ON e.id = pf.estabelecimento_id
    JOIN (SELECT 'MONDAY' AS dia UNION ALL SELECT 'TUESDAY' UNION ALL SELECT 'WEDNESDAY'
                UNION ALL SELECT 'THURSDAY' UNION ALL SELECT 'FRIDAY' UNION ALL SELECT 'SATURDAY') d
    WHERE e.cnpj = '64245654000168'
        AND pf.cpf IN ('06854809096','11144477735','16899535009','45317828791')
        AND NOT EXISTS (SELECT 1 FROM expediente_profissional ep WHERE ep.profissional_id = pf.id AND ep.dia_semana = d.dia);

    -- Vínculo serviço-profissional — estabelecimento 1
    INSERT INTO servico_profissional (servico_id, profissional_id, valor)
    SELECT sv.id, pf.id,
                 CASE sv.nome WHEN 'CORTE' THEN 80.00 WHEN 'COLORACAO' THEN 180.00 WHEN 'HIDRATACAO' THEN 120.00 WHEN 'ESCOVA' THEN 60.00 ELSE 50.00 END
    FROM servico sv
    JOIN estabelecimento e ON e.id = sv.estabelecimento_id
    JOIN profissional pf ON pf.estabelecimento_id = e.id
    WHERE e.cnpj = '64245654000168'
        AND pf.cpf IN ('06854809096','11144477735','16899535009','45317828791')
        AND NOT EXISTS (SELECT 1 FROM servico_profissional sp WHERE sp.servico_id = sv.id AND sp.profissional_id = pf.id);

    -- Agendamentos — estabelecimento 1 (3 por profissional: AGENDADO, CANCELADO, CONCLUIDO)
    INSERT INTO agendamento (profissional_id, servico_id, estabelecimento_id, cliente_id, data_hora_inicio, data_hora_fim, status)
    SELECT pf.id, sv.id, e.id, c.id, ag.inicio, ag.inicio + INTERVAL 1 HOUR, ag.status
    FROM estabelecimento e
    JOIN profissional pf ON pf.estabelecimento_id = e.id
    JOIN servico sv ON sv.estabelecimento_id = e.id AND sv.nome = 'CORTE'
    JOIN cliente c ON c.cpf = '34028317088'
    JOIN (SELECT 'AGENDADO'  AS status, DATE_ADD(NOW(), INTERVAL 2  DAY) AS inicio
                UNION ALL SELECT 'CANCELADO', DATE_SUB(NOW(), INTERVAL 3  DAY)
                UNION ALL SELECT 'CONCLUIDO', DATE_SUB(NOW(), INTERVAL 10 DAY)) ag
    WHERE e.cnpj = '64245654000168'
        AND pf.cpf IN ('06854809096','11144477735','16899535009','45317828791')
        AND NOT EXISTS (
            SELECT 1 FROM agendamento a WHERE a.profissional_id = pf.id AND a.status = ag.status
        );

    -- Avaliação dos agendamentos CONCLUIDO — estabelecimento 1
    INSERT INTO avaliacao (agendamento_id, nota_estabelecimento, nota_profissional, comentario_estabelecimento, comentario_profissional)
    SELECT a.id, 4.8, 5.0, 'Ambiente excelente.', 'Profissional muito atenciosa.'
    FROM agendamento a
    JOIN profissional pf ON pf.id = a.profissional_id
    JOIN estabelecimento e ON e.id = a.estabelecimento_id
    WHERE e.cnpj = '64245654000168'
        AND a.status = 'CONCLUIDO'
        AND NOT EXISTS (SELECT 1 FROM avaliacao av WHERE av.agendamento_id = a.id);

    -- ============================================================
    -- ESTABELECIMENTO 2 — Espaço Harmonia
    -- ============================================================

    INSERT INTO estabelecimento (nome, cnpj, logradouro, numero, complemento, bairro, cidade, estado, cep)
    SELECT 'Espaço Harmonia', '12345678000195', 'Rua das Flores', '200', 'Loja 3', 'Jardins', 'São Paulo', 'SP', '01420000'
    WHERE NOT EXISTS (SELECT 1 FROM estabelecimento e WHERE e.cnpj = '12345678000195');

    INSERT INTO estabelecimento_horarios (estabelecimento_id, dia_semana, abertura, fechamento, fechado)
    SELECT e.id, d.dia, '09:00:00', '19:00:00', FALSE
    FROM estabelecimento e
    JOIN (SELECT 'MONDAY' AS dia UNION ALL SELECT 'TUESDAY' UNION ALL SELECT 'WEDNESDAY'
                UNION ALL SELECT 'THURSDAY' UNION ALL SELECT 'FRIDAY' UNION ALL SELECT 'SATURDAY') d
    WHERE e.cnpj = '12345678000195'
        AND NOT EXISTS (SELECT 1 FROM estabelecimento_horarios eh WHERE eh.estabelecimento_id = e.id AND eh.dia_semana = d.dia);

    -- Serviços do estabelecimento 2
    INSERT INTO servico (nome, duracao_media, estabelecimento_id)
    SELECT s.nome, s.dur, e.id FROM estabelecimento e
    JOIN (SELECT 'CORTE' AS nome, 3600000000000 AS dur
                UNION ALL SELECT 'COLORACAO',   5400000000000
                UNION ALL SELECT 'HIDRATACAO',  2700000000000
                UNION ALL SELECT 'ESCOVA',      1800000000000
                UNION ALL SELECT 'MANICURE',    2700000000000) s
    WHERE e.cnpj = '12345678000195'
        AND NOT EXISTS (SELECT 1 FROM servico sv WHERE sv.nome = s.nome AND sv.estabelecimento_id = e.id);

    -- Profissionais do estabelecimento 2
    INSERT INTO profissional (nome, cpf, celular, email, url_foto, descricao, sexo, estabelecimento_id)
    SELECT p.nome, p.cpf, p.cel, p.email, NULL, p.descricao, p.sexo, e.id
    FROM estabelecimento e
    JOIN (SELECT 'Fernanda Dias'  AS nome, '11222333457' AS cpf, '11977770001' AS cel, 'fernanda@e2.com' AS email, 'Colorista'            AS descricao, 'FEMININO'  AS sexo
                UNION ALL SELECT 'Ricardo Neves',       '22333444568', '11977770002', 'ricardo@e2.com',  'Barbeiro',             'MASCULINO'
                UNION ALL SELECT 'Juliana Pires',       '33444555679', '11977770003', 'juliana@e2.com',  'Escova e hidratação',  'FEMININO'
                UNION ALL SELECT 'Lucas Martins',       '44555666780', '11977770004', 'lucas@e2.com',    'Corte masculino',      'MASCULINO') p
    WHERE e.cnpj = '12345678000195'
        AND NOT EXISTS (SELECT 1 FROM profissional pf WHERE pf.cpf = p.cpf);

    -- Expediente — profissionais do estabelecimento 2
    INSERT INTO expediente_profissional (profissional_id, dia_semana, inicio_turno, fim_turno, inicio_intervalo, fim_intervalo)
    SELECT pf.id, d.dia, '09:00:00', '18:00:00', '12:00:00', '13:00:00'
    FROM profissional pf
    JOIN estabelecimento e ON e.id = pf.estabelecimento_id
    JOIN (SELECT 'MONDAY' AS dia UNION ALL SELECT 'TUESDAY' UNION ALL SELECT 'WEDNESDAY'
                UNION ALL SELECT 'THURSDAY' UNION ALL SELECT 'FRIDAY' UNION ALL SELECT 'SATURDAY') d
    WHERE e.cnpj = '12345678000195'
        AND pf.cpf IN ('11222333457','22333444568','33444555679','44555666780')
        AND NOT EXISTS (SELECT 1 FROM expediente_profissional ep WHERE ep.profissional_id = pf.id AND ep.dia_semana = d.dia);

    -- Vínculo serviço-profissional — estabelecimento 2
    INSERT INTO servico_profissional (servico_id, profissional_id, valor)
    SELECT sv.id, pf.id,
                 CASE sv.nome WHEN 'CORTE' THEN 90.00 WHEN 'COLORACAO' THEN 200.00 WHEN 'HIDRATACAO' THEN 130.00 WHEN 'ESCOVA' THEN 70.00 ELSE 55.00 END
    FROM servico sv
    JOIN estabelecimento e ON e.id = sv.estabelecimento_id
    JOIN profissional pf ON pf.estabelecimento_id = e.id
    WHERE e.cnpj = '12345678000195'
        AND pf.cpf IN ('11222333457','22333444568','33444555679','44555666780')
        AND NOT EXISTS (SELECT 1 FROM servico_profissional sp WHERE sp.servico_id = sv.id AND sp.profissional_id = pf.id);

    -- Agendamentos — estabelecimento 2
    INSERT INTO agendamento (profissional_id, servico_id, estabelecimento_id, cliente_id, data_hora_inicio, data_hora_fim, status)
    SELECT pf.id, sv.id, e.id, c.id, ag.inicio, ag.inicio + INTERVAL 1 HOUR, ag.status
    FROM estabelecimento e
    JOIN profissional pf ON pf.estabelecimento_id = e.id
    JOIN servico sv ON sv.estabelecimento_id = e.id AND sv.nome = 'CORTE'
    JOIN cliente c ON c.cpf = '52998224725'
    JOIN (SELECT 'AGENDADO'  AS status, DATE_ADD(NOW(), INTERVAL 3  DAY) AS inicio
                UNION ALL SELECT 'CANCELADO', DATE_SUB(NOW(), INTERVAL 4  DAY)
                UNION ALL SELECT 'CONCLUIDO', DATE_SUB(NOW(), INTERVAL 11 DAY)) ag
    WHERE e.cnpj = '12345678000195'
        AND pf.cpf IN ('11222333457','22333444568','33444555679','44555666780')
        AND NOT EXISTS (
            SELECT 1 FROM agendamento a WHERE a.profissional_id = pf.id AND a.status = ag.status
        );

    -- Avaliação — estabelecimento 2
    INSERT INTO avaliacao (agendamento_id, nota_estabelecimento, nota_profissional, comentario_estabelecimento, comentario_profissional)
    SELECT a.id, 4.5, 4.7, 'Ótimo espaço.', 'Muito competente.'
    FROM agendamento a
    JOIN profissional pf ON pf.id = a.profissional_id
    JOIN estabelecimento e ON e.id = a.estabelecimento_id
    WHERE e.cnpj = '12345678000195'
        AND a.status = 'CONCLUIDO'
        AND NOT EXISTS (SELECT 1 FROM avaliacao av WHERE av.agendamento_id = a.id);

    -- ============================================================
    -- ESTABELECIMENTO 3 — Salão Top Style
    -- ============================================================

    INSERT INTO estabelecimento (nome, cnpj, logradouro, numero, complemento, bairro, cidade, estado, cep)
    SELECT 'Salão Top Style', '98765432000198', 'Rua das Acácias', '500', 'Sala 2', 'Centro', 'Campinas', 'SP', '13010000'
    WHERE NOT EXISTS (SELECT 1 FROM estabelecimento e WHERE e.cnpj = '98765432000198');

    INSERT INTO estabelecimento_horarios (estabelecimento_id, dia_semana, abertura, fechamento, fechado)
    SELECT e.id, d.dia, '08:00:00', '18:00:00', FALSE
    FROM estabelecimento e
    JOIN (SELECT 'MONDAY' AS dia UNION ALL SELECT 'TUESDAY' UNION ALL SELECT 'WEDNESDAY'
                UNION ALL SELECT 'THURSDAY' UNION ALL SELECT 'FRIDAY' UNION ALL SELECT 'SATURDAY') d
    WHERE e.cnpj = '98765432000198'
        AND NOT EXISTS (SELECT 1 FROM estabelecimento_horarios eh WHERE eh.estabelecimento_id = e.id AND eh.dia_semana = d.dia);

    -- Serviços do estabelecimento 3
    INSERT INTO servico (nome, duracao_media, estabelecimento_id)
    SELECT s.nome, s.dur, e.id FROM estabelecimento e
    JOIN (SELECT 'CORTE' AS nome, 3600000000000 AS dur
                UNION ALL SELECT 'COLORACAO',   5400000000000
                UNION ALL SELECT 'HIDRATACAO',  2700000000000
                UNION ALL SELECT 'ESCOVA',      1800000000000
                UNION ALL SELECT 'MANICURE',    2700000000000) s
    WHERE e.cnpj = '98765432000198'
        AND NOT EXISTS (SELECT 1 FROM servico sv WHERE sv.nome = s.nome AND sv.estabelecimento_id = e.id);

    -- Profissionais do estabelecimento 3
    INSERT INTO profissional (nome, cpf, celular, email, url_foto, descricao, sexo, estabelecimento_id)
    SELECT p.nome, p.cpf, p.cel, p.email, NULL, p.descricao, p.sexo, e.id
    FROM estabelecimento e
    JOIN (SELECT 'Tatiane Rocha'  AS nome, '55666777890' AS cpf, '19966660001' AS cel, 'tatiane@e3.com' AS email, 'Colorista avançada'   AS descricao, 'FEMININO'  AS sexo
                UNION ALL SELECT 'Marcos Vidal',        '66777888900', '19966660002', 'marcos@e3.com',  'Barbeiro clássico',    'MASCULINO'
                UNION ALL SELECT 'Patrícia Cunha',      '77888999093', '19966660003', 'patricia@e3.com','Manicure e pedicure',  'FEMININO'
                UNION ALL SELECT 'Rodrigo Farias',      '88999000192', '19966660004', 'rodrigo@e3.com', 'Corte e escova',       'MASCULINO') p
    WHERE e.cnpj = '98765432000198'
        AND NOT EXISTS (SELECT 1 FROM profissional pf WHERE pf.cpf = p.cpf);

    -- Expediente — profissionais do estabelecimento 3
    INSERT INTO expediente_profissional (profissional_id, dia_semana, inicio_turno, fim_turno, inicio_intervalo, fim_intervalo)
    SELECT pf.id, d.dia, '08:00:00', '17:00:00', '12:00:00', '13:00:00'
    FROM profissional pf
    JOIN estabelecimento e ON e.id = pf.estabelecimento_id
    JOIN (SELECT 'MONDAY' AS dia UNION ALL SELECT 'TUESDAY' UNION ALL SELECT 'WEDNESDAY'
                UNION ALL SELECT 'THURSDAY' UNION ALL SELECT 'FRIDAY' UNION ALL SELECT 'SATURDAY') d
    WHERE e.cnpj = '98765432000198'
        AND pf.cpf IN ('55666777890','66777888900','77888999093','88999000192')
        AND NOT EXISTS (SELECT 1 FROM expediente_profissional ep WHERE ep.profissional_id = pf.id AND ep.dia_semana = d.dia);

    -- Vínculo serviço-profissional — estabelecimento 3
    INSERT INTO servico_profissional (servico_id, profissional_id, valor)
    SELECT sv.id, pf.id,
                 CASE sv.nome WHEN 'CORTE' THEN 70.00 WHEN 'COLORACAO' THEN 160.00 WHEN 'HIDRATACAO' THEN 110.00 WHEN 'ESCOVA' THEN 55.00 ELSE 45.00 END
    FROM servico sv
    JOIN estabelecimento e ON e.id = sv.estabelecimento_id
    JOIN profissional pf ON pf.estabelecimento_id = e.id
    WHERE e.cnpj = '98765432000198'
        AND pf.cpf IN ('55666777890','66777888900','77888999093','88999000192')
        AND NOT EXISTS (SELECT 1 FROM servico_profissional sp WHERE sp.servico_id = sv.id AND sp.profissional_id = pf.id);

    -- Agendamentos — estabelecimento 3
    INSERT INTO agendamento (profissional_id, servico_id, estabelecimento_id, cliente_id, data_hora_inicio, data_hora_fim, status)
    SELECT pf.id, sv.id, e.id, c.id, ag.inicio, ag.inicio + INTERVAL 1 HOUR, ag.status
    FROM estabelecimento e
    JOIN profissional pf ON pf.estabelecimento_id = e.id
    JOIN servico sv ON sv.estabelecimento_id = e.id AND sv.nome = 'CORTE'
    JOIN cliente c ON c.cpf = '39053344705'
    JOIN (SELECT 'AGENDADO'  AS status, DATE_ADD(NOW(), INTERVAL 4  DAY) AS inicio
                UNION ALL SELECT 'CANCELADO', DATE_SUB(NOW(), INTERVAL 5  DAY)
                UNION ALL SELECT 'CONCLUIDO', DATE_SUB(NOW(), INTERVAL 12 DAY)) ag
    WHERE e.cnpj = '98765432000198'
        AND pf.cpf IN ('55666777890','66777888900','77888999093','88999000192')
        AND NOT EXISTS (
            SELECT 1 FROM agendamento a WHERE a.profissional_id = pf.id AND a.status = ag.status
        );

    -- Avaliação — estabelecimento 3
    INSERT INTO avaliacao (agendamento_id, nota_estabelecimento, nota_profissional, comentario_estabelecimento, comentario_profissional)
    SELECT a.id, 4.2, 4.9, 'Bom atendimento.', 'Muito habilidoso.'
    FROM agendamento a
    JOIN profissional pf ON pf.id = a.profissional_id
    JOIN estabelecimento e ON e.id = a.estabelecimento_id
    WHERE e.cnpj = '98765432000198'
        AND a.status = 'CONCLUIDO'
        AND NOT EXISTS (SELECT 1 FROM avaliacao av WHERE av.agendamento_id = a.id);

    -- ============================================================
    -- ESTABELECIMENTO 4 — Sem vínculos (deletável)
    -- ============================================================

    INSERT INTO estabelecimento (nome, cnpj, logradouro, numero, complemento, bairro, cidade, estado, cep)
    SELECT 'Espaço Livre', '74185296000107', 'Rua Teste', '10', 'Sala 1', 'Centro', 'São Paulo', 'SP', '01010000'
    WHERE NOT EXISTS (SELECT 1 FROM estabelecimento e WHERE e.cnpj = '74185296000107');

    -- ============================================================
    -- ESTABELECIMENTO 5 — Studio Bela Vida Premium
    -- ============================================================

    INSERT INTO estabelecimento (nome, cnpj, logradouro, numero, complemento, bairro, cidade, estado, cep)
    SELECT 'Studio Bela Vida Premium', '64245654000249', 'Avenida Paulista', '1002', 'Sala 12', 'Bela Vista', 'São Paulo', 'SP', '01310100'
    WHERE NOT EXISTS (SELECT 1 FROM estabelecimento e WHERE e.cnpj = '64245654000249');
