# Testes de Performance e Carga

Os testes não funcionais ficam em `src/test/java/com/fiap/gestao_servicos/performance/AgendamentoPerformanceTest.java`.

## Objetivo

Validar que a API de agendamentos:

- suporta múltiplos `POST /estabelecimentos/{id}/agendamentos` simultâneos;
- mantém a consulta `GET /estabelecimentos/{id}/agendamentos` responsiva mesmo com volume elevado de registros;
- respeita um SLA básico de tempo de resposta em ambiente local/CI.

## Cenários cobertos

### 1. Carga simultânea de criação

- **48 requisições concorrentes** de criação de agendamentos;
- validação de **100% de sucesso HTTP 201**;
- verificação de **tempo total do lote** e **latência p95**.

### 2. Performance de leitura com volume alto

- geração de **80 novos agendamentos** para ampliar a massa de dados;
- execução repetida da listagem paginada da agenda;
- validação de **tempo médio** e **pior caso** de resposta.

## Como executar

### Suite completa

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

### Apenas os testes não funcionais

```powershell
.\mvnw.cmd -Dtest=AgendamentoPerformanceTest test
```

## Ambiente utilizado

Os testes sobem a aplicação com o perfil `bdd`, usando:

- **H2 em memória**;
- dados iniciais controlados em `src/test/resources/data-bdd.sql`;
- servidor HTTP embutido em porta aleatória.

> Os limites de SLA foram definidos para detectar degradação relevante sem tornar a execução instável em máquinas de desenvolvimento ou CI.
