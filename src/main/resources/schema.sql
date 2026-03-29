-- Schema migration: add sexo column to profissional and create expediente_profissional
-- Add column `sexo` to `profissional` only if it does not exist (MySQL-safe)
SET @col_exists = (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'profissional'
    AND COLUMN_NAME = 'sexo'
);
SET @sql = IF(@col_exists = 0,
  'ALTER TABLE profissional ADD COLUMN sexo VARCHAR(20);',
  'SELECT 1;'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE TABLE IF NOT EXISTS expediente_profissional (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  profissional_id BIGINT NOT NULL,
  dia_semana VARCHAR(20) NOT NULL,
  inicio_turno TIME NOT NULL,
  fim_turno TIME NOT NULL,
  inicio_intervalo TIME NULL,
  fim_intervalo TIME NULL,
  CONSTRAINT fk_expediente_profissional_profissional FOREIGN KEY (profissional_id) REFERENCES profissional(id) ON DELETE CASCADE
);

-- Fix legacy unique constraint on servico_profissional.profissional_id
-- Old model used OneToOne and may have left a UNIQUE index on profissional_id.
-- New model allows multiple services per professional and must be ManyToOne.
SET @old_unique_idx = (
  SELECT s.INDEX_NAME
  FROM information_schema.STATISTICS s
  WHERE s.TABLE_SCHEMA = DATABASE()
    AND s.TABLE_NAME = 'servico_profissional'
    AND s.NON_UNIQUE = 0
    AND s.INDEX_NAME <> 'PRIMARY'
  GROUP BY s.INDEX_NAME
  HAVING COUNT(*) = 1
     AND MAX(CASE WHEN s.COLUMN_NAME = 'profissional_id' THEN 1 ELSE 0 END) = 1
  LIMIT 1
);

SET @sql = IF(@old_unique_idx IS NOT NULL,
  CONCAT('ALTER TABLE servico_profissional DROP INDEX ', @old_unique_idx, ';'),
  'SELECT 1;'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Enforce only one row per (profissional, servico)
SET @pair_unique_exists = (
  SELECT COUNT(*)
  FROM information_schema.STATISTICS s
  WHERE s.TABLE_SCHEMA = DATABASE()
    AND s.TABLE_NAME = 'servico_profissional'
    AND s.INDEX_NAME = 'uq_servico_profissional_profissional_servico'
    AND s.NON_UNIQUE = 0
);
SET @sql = IF(@pair_unique_exists = 0,
  'ALTER TABLE servico_profissional ADD CONSTRAINT uq_servico_profissional_profissional_servico UNIQUE (profissional_id, servico_id);',
  'SELECT 1;'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
