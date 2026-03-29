# Testes Não Funcionais (Performance e Carga)

Este projeto possui um cenário de carga com Gatling focado em alto volume de agendamentos simultâneos.

## Objetivo

Validar que a API suporta concorrência elevada sem degradação significativa:

- Leitura de agendamentos (`GET /estabelecimentos/{id}/agendamentos`)
- Criação concorrente de agendamentos (`POST /estabelecimentos/{id}/agendamentos`)

## Pré-requisitos

- Aplicação em execução (ex.: `http://localhost:8080`)
- Banco com dados mínimos para os IDs usados no teste

Opcional recomendado para execução local rápida:

- Subir a aplicação com perfil dedicado de carga (`loadtest`), que usa H2 em memória e carga inicial automática.

## Execução

Comando padrão:

```bash
./mvnw gatling:test -Dgatling.simulationClass=com.fiap.gestao_servicos.performance.AgendamentoLoadSimulation
```

Subindo a aplicação local para carga (perfil `loadtest`):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=loadtest
```

Com parâmetros de volume e SLA:

```bash
./mvnw gatling:test \
  -Dgatling.simulationClass=com.fiap.gestao_servicos.performance.AgendamentoLoadSimulation \
  -DbaseUrl=http://localhost:8080 \
  -DestabelecimentoId=1 \
  -DprofissionalIds=1,2,3,4,5 \
  -DservicoId=1 \
  -DclienteId=1 \
  -DreadUsers=200 \
  -DwriteUsers=150 \
  -DrampSeconds=60 \
  -DdurationSeconds=300 \
  -DmaxP95Ms=2000 \
  -DmaxErrorPercent=2.0
```

## Parâmetros disponíveis

- `baseUrl` (default: `http://localhost:8080`)
- `estabelecimentoId` (default: `1`)
- `profissionalIds` (default: `1,2,3,4,5`)
- `servicoId` (default: `1`)
- `clienteId` (default: `1`)
- `readUsers` (default: `80`)
- `writeUsers` (default: `40`)
- `rampSeconds` (default: `30`)
- `durationSeconds` (default: `120`)
- `maxP95Ms` (default: `1500`)
- `maxErrorPercent` (default: `2.0`)

## Resultado

O Gatling gera relatório HTML em:

- `target/gatling/<nome-da-simulacao>/index.html`
