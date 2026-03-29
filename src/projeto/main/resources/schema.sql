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
