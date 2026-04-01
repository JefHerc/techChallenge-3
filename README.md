# Gestão de Serviços

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot 4.0.3](https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Wrapper-C71A36?logo=apachemaven&logoColor=white)
![MySQL 8.4](https://img.shields.io/badge/MySQL-8.4-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![CI](https://github.com/JefHerc/techChallenge-3/actions/workflows/ci.yml/badge.svg)

Backend para agendamento e gerenciamento de serviços de beleza e bem-estar, desenvolvido como desafio técnico com foco em Clean Architecture, qualidade de código e manutenibilidade. A aplicação centraliza o cadastro de clientes, estabelecimentos, serviços, profissionais, agendamentos e avaliações em uma API REST organizada por camadas.

## Sumário

- [Acesso Rápido](#acesso-rápido)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Diferenciais](#diferenciais)
- [Arquitetura e Estrutura](#arquitetura-e-estrutura)
- [Pré-requisitos](#pré-requisitos)
- [Como Rodar o Projeto](#como-rodar-o-projeto)
- [Documentação da API](#documentação-da-api)
- [Como Rodar os Testes](#como-rodar-os-testes)
- [Teste de Carga (Não Funcional)](#teste-de-carga-não-funcional)

## Acesso Rápido

| Recurso | Acesso |
| --- | --- |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| Mailpit | `http://localhost:8025` |
| Guia de performance | `docs/performance-tests.md` |
| Execução local | [Como Rodar o Projeto](#como-rodar-o-projeto) |
| Testes | [Como Rodar os Testes](#como-rodar-os-testes) |
| CI | `.github/workflows/ci.yml` |

## Tecnologias Utilizadas

- Java 17
- Spring Boot 4.0.3
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Mail
- Springdoc OpenAPI / Swagger UI
- MySQL 8.4
- H2 Database (testes)
- Maven Wrapper
- Docker e Docker Compose
- JUnit 5
- Cucumber 7.22 (BDD)
- ArchUnit
- Gatling
- Checkstyle, PMD e SpotBugs

## Diferenciais

- Estrutura em camadas inspirada em Clean Architecture, com separação clara entre domínio, casos de uso e infraestrutura.
- Documentação da API com Swagger/OpenAPI para facilitar exploração e validação dos contratos.
- Tratamento padronizado de erros e validações de entrada para manter consistência nas respostas HTTP.
- Testes em diferentes níveis: domínio, casos de uso, BDD, arquitetura, camada web e carga.
- Ambiente local pronto com Docker Compose, incluindo MySQL e Mailpit.
- Análise estática com Checkstyle, PMD e SpotBugs.
- Pipeline de CI automatizada com publicação de relatórios de cobertura e BDD.

## Arquitetura e Estrutura

O projeto adota uma organização inspirada em Clean Architecture para separar regras de negócio, casos de uso e detalhes de infraestrutura.

### Camadas

- `core.domain`: entidades, value objects e regras centrais do negócio.
- `core.usecase`: casos de uso da aplicação e validações associadas.
- `core.repository` e `core.notification`: contratos (ports) usados pelo núcleo da aplicação.
- `core.exception`: exceções de domínio e regras de negócio.
- `infrastructure.controller`: adaptadores de entrada HTTP e tratamento global de erros.
- `infrastructure.persistence`: implementações de persistência com JPA.
- `infrastructure.mapper`: conversão entre DTOs, entidades de persistência e domínio.
- `infrastructure.config`, `infrastructure.scheduler` e `infrastructure.notification`: configurações, jobs agendados e integrações externas.

### Estrutura resumida

```text
src/
  main/
    java/com/fiap/gestao_servicos/
      core/
        domain/
        exception/
        notification/
        pagination/
        repository/
        usecase/
      infrastructure/
        config/
        controller/
        mapper/
        notification/
        pagination/
        persistence/
        scheduler/
    resources/
  test/
    java/com/fiap/gestao_servicos/
      architecture/
      bdd/
      core/
      infrastructure/
      integration/
      performance/
    resources/
      features/
```

## Pré-requisitos

Antes de executar o projeto, tenha instalado:

- JDK 17 ou superior
- Docker e Docker Compose
- Maven 3.9+ ou utilize o Maven Wrapper incluído no repositório

## Como Rodar o Projeto

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd gestao-servicos
```

### 2. Configurar variáveis de ambiente

Para execução local, a aplicação já possui valores padrão em `application.yaml`, mas você pode sobrescrever as configurações via variáveis de ambiente quando necessário.

Principais variáveis:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_MAIL_HOST`
- `SPRING_MAIL_PORT`
- `NOTIFICATION_EMAIL_FROM`

### 3. Subir dependências locais

Para desenvolvimento local, suba banco de dados e Mailpit:

```bash
docker compose up -d db mailpit
```

Se preferir subir toda a stack em containers:

```bash
docker compose up --build -d
```

### 4. Executar a aplicação

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### 5. Serviços auxiliares

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Mailpit UI: `http://localhost:8025`

## Documentação da API

Depois de iniciar a aplicação, a documentação interativa da API REST fica disponível no Swagger UI.

- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Principais endpoints REST

#### Clientes (`/clientes`)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/clientes` | Cria um cliente |
| GET | `/clientes` | Lista clientes (paginado) |
| GET | `/clientes/{id}` | Busca cliente por id |
| PUT | `/clientes/{id}` | Atualiza um cliente |
| DELETE | `/clientes/{id}` | Remove um cliente |

#### Estabelecimentos (`/estabelecimentos`)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/estabelecimentos` | Cria um estabelecimento |
| GET | `/estabelecimentos` | Lista estabelecimentos (paginado) |
| GET | `/estabelecimentos/{id}` | Busca estabelecimento por id |
| PUT | `/estabelecimentos/{id}` | Atualiza um estabelecimento |
| DELETE | `/estabelecimentos/{id}` | Remove um estabelecimento |
| GET | `/estabelecimentos/busca` | Busca com filtros |

#### Serviços (`/estabelecimentos/{estabelecimentoId}/servicos`)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/estabelecimentos/{id}/servicos` | Cria serviços para um estabelecimento |
| GET | `/estabelecimentos/{id}/servicos` | Lista serviços (paginado) |
| GET | `/estabelecimentos/{id}/servicos/{sid}` | Busca serviço por id |
| PUT | `/estabelecimentos/{id}/servicos/{sid}` | Atualiza um serviço |
| DELETE | `/estabelecimentos/{id}/servicos/{sid}` | Remove um serviço |

#### Profissionais (`/estabelecimentos/{estabelecimentoId}/profissionais`)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/estabelecimentos/{id}/profissionais` | Cria profissional para um estabelecimento |
| GET | `/estabelecimentos/{id}/profissionais` | Lista profissionais (paginado) |
| GET | `/estabelecimentos/{id}/profissionais/{pid}` | Busca profissional por id |
| PUT | `/estabelecimentos/{id}/profissionais/{pid}` | Atualiza um profissional |
| DELETE | `/estabelecimentos/{id}/profissionais/{pid}` | Remove um profissional |

#### Agendamentos (`/estabelecimentos/{estabelecimentoId}/agendamentos`)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/estabelecimentos/{id}/agendamentos` | Cria um agendamento |
| GET | `/estabelecimentos/{id}/agendamentos` | Lista agendamentos (paginado) |
| GET | `/estabelecimentos/{id}/agendamentos/periodo` | Lista agendamentos por período |
| GET | `/estabelecimentos/{id}/agendamentos/{aid}` | Busca agendamento por id |
| PUT | `/estabelecimentos/{id}/agendamentos/{aid}` | Atualiza um agendamento |
| DELETE | `/estabelecimentos/{id}/agendamentos/{aid}` | Remove um agendamento |

#### Avaliações (`/estabelecimentos/{estabelecimentoId}/...`)

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/estabelecimentos/{id}/agendamentos/{aid}/avaliacoes` | Cria avaliação para um agendamento |
| GET | `/estabelecimentos/{id}/avaliacoes` | Lista avaliações do estabelecimento (paginado) |
| GET | `/estabelecimentos/{id}/agendamentos/{aid}/avaliacoes/{vid}` | Busca avaliação por id |
| PUT | `/estabelecimentos/{id}/agendamentos/{aid}/avaliacoes/{vid}` | Atualiza uma avaliação |
| DELETE | `/estabelecimentos/{id}/agendamentos/avaliacoes/{vid}` | Remove uma avaliação |

## Como Rodar os Testes

O projeto possui testes de domínio, casos de uso, BDD, camada web, integração, arquitetura e performance.

### Suite principal

No Linux/macOS:

```bash
./mvnw test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

### Tipos de teste

| Tipo | Localização | Ferramenta |
| --- | --- | --- |
| Unitários (domínio e casos de uso) | `src/test/java/.../core/` | JUnit 5, Mockito |
| BDD | `src/test/java/.../bdd/` e `src/test/resources/features/` | Cucumber |
| Arquitetura | `src/test/java/.../architecture/` | ArchUnit |
| Camada web | `src/test/java/.../integration/` | Spring MockMvc |
| Performance | `src/test/java/.../performance/` | Gatling |

### Cobertura de testes

A cobertura é gerada com JaCoCo durante a fase `verify` do Maven.

No Linux/macOS:

```bash
./mvnw verify
```

No Windows PowerShell:

```powershell
.\mvnw.cmd verify
```

Relatórios gerados localmente:

- **JaCoCo HTML**: `target/site/jacoco/index.html`
- **JaCoCo XML**: `target/site/jacoco/jacoco.xml`
- **Cucumber HTML**: `target/cucumber-reports/cucumber.html`

Na pipeline de CI, os relatórios de cobertura e BDD também são publicados como artifacts.

### Executar testes de performance

```bash
./mvnw gatling:test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd gatling:test
```

Para mais detalhes sobre carga e performance, consulte `docs/performance-tests.md`.

## Teste de Carga (Não Funcional)

O cenário de carga utiliza Gatling para simular alto volume de leituras e criações de agendamento:

- `GET /estabelecimentos/{id}/agendamentos`
- `POST /estabelecimentos/{id}/agendamentos`

### 1. Subir a aplicação com perfil dedicado de carga

No Linux/macOS:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=loadtest
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=loadtest"
```

Esse perfil usa H2 em memória e dados iniciais para execução local de carga.

### 2. Executar o teste de carga com Gatling

Por padrão, a simulação executa alta simultaneidade sustentada (80 usuários de leitura e 40 de escrita).

No Linux/macOS:

```bash
./mvnw gatling:test -Dgatling.simulationClass=com.fiap.gestao_servicos.performance.AgendamentoLoadSimulation
```

No Windows PowerShell:

```powershell
.\mvnw.cmd gatling:test -Dgatling.simulationClass=com.fiap.gestao_servicos.performance.AgendamentoLoadSimulation
.\mvnw.cmd gatling:test "-Dgatling.simulationClass=com.fiap.gestao_servicos.performance.AgendamentoLoadSimulation" "-DbaseUrl=http://localhost:8080"
```

### 3. Ajustar volume e SLA (opcional)

Exemplo:

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

### 4. Relatório

O relatório HTML é gerado em:

- `target/gatling/<nome-da-simulacao>/index.html`
