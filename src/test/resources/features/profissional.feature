Feature: Cadastro de Profissionais
  Como dono de um estabelecimento
  Quero cadastrar e gerenciar profissionais vinculados ao meu negócio
  Para disponibilizar agenda e serviços aos clientes

  Scenario: Cadastrar profissional com dados válidos
    When o cliente envia POST para "/estabelecimentos/1/profissionais" com o corpo:
      """
      {
        "nome": "Ana BDD",
        "cpf": "55566677720",
        "celular": "11995554444",
        "email": "ana.bdd@email.com",
        "urlFoto": "https://cdn.exemplo.com/profissionais/ana-bdd.jpg",
        "descricao": "Especialista em corte e finalizacao",
        "sexo": "FEMININO",
        "expedientes": [
          {
            "diaSemana": "segunda-feira",
            "inicioTurno": "09:00",
            "fimTurno": "17:00"
          }
        ],
        "servicosProfissional": [
          {
            "servicoId": 1,
            "valor": 120.00
          }
        ]
      }
      """
    Then o status HTTP da resposta deve ser 201
    And o campo JSON "nome" deve ser "Ana BDD"
    And o campo JSON "email" deve ser "ana.bdd@email.com"
    And o campo JSON "sexo" deve ser "FEMININO"

  Scenario: Tentar cadastrar profissional com email inválido retorna erro de validação
    When o cliente envia POST para "/estabelecimentos/1/profissionais" com o corpo:
      """
      {
        "nome": "Profissional Invalido",
        "cpf": "55566677720",
        "celular": "11995554444",
        "email": "email-invalido",
        "urlFoto": null,
        "descricao": "Teste de validacao",
        "sexo": "FEMININO",
        "expedientes": [],
        "servicosProfissional": []
      }
      """
    Then o status HTTP da resposta deve ser 400
    And o campo JSON "code" deve ser "VALIDACAO_ENTRADA"
    And a resposta deve conter um erro de validação no campo "email"

  Scenario: Buscar profissional inexistente retorna not found
    When o cliente envia GET para "/estabelecimentos/1/profissionais/99999"
    Then o status HTTP da resposta deve ser 404
    And o campo JSON "code" deve ser "NAO_ENCONTRADO"

  Scenario: Criar e em seguida atualizar profissional
    When o cliente envia POST para "/estabelecimentos/1/profissionais" com o corpo:
      """
      {
        "nome": "Carlos BDD",
        "cpf": "98765432100",
        "celular": "11994443333",
        "email": "carlos.bdd@email.com",
        "urlFoto": "https://cdn.exemplo.com/profissionais/carlos-bdd.jpg",
        "descricao": "Especialista em barba",
        "sexo": "MASCULINO",
        "expedientes": [
          {
            "diaSemana": "terca-feira",
            "inicioTurno": "10:00",
            "fimTurno": "16:00"
          }
        ],
        "servicosProfissional": [
          {
            "servicoId": 1,
            "valor": 90.00
          }
        ]
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia PUT para "/estabelecimentos/1/profissionais/{lastId}" com o corpo:
      """
      {
        "nome": "Carlos BDD Atualizado",
        "cpf": "98765432100",
        "celular": "11994443333",
        "email": "carlos.bdd.atualizado@email.com",
        "urlFoto": "https://cdn.exemplo.com/profissionais/carlos-bdd-atualizado.jpg",
        "descricao": "Especialista em corte masculino",
        "sexo": "MASCULINO",
        "expedientes": [
          {
            "diaSemana": "quarta-feira",
            "inicioTurno": "09:00",
            "fimTurno": "15:00"
          }
        ],
        "servicosProfissional": [
          {
            "servicoId": 1,
            "valor": 95.00
          }
        ]
      }
      """
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "nome" deve ser "Carlos BDD Atualizado"
    And o campo JSON "email" deve ser "carlos.bdd.atualizado@email.com"

  Scenario: Criar e em seguida excluir profissional
    When o cliente envia POST para "/estabelecimentos/1/profissionais" com o corpo:
      """
      {
        "nome": "Marina BDD",
        "cpf": "27182818205",
        "celular": "11992223333",
        "email": "marina.bdd@email.com",
        "urlFoto": null,
        "descricao": "Especialista em atendimento rapido",
        "sexo": "FEMININO",
        "expedientes": [
          {
            "diaSemana": "quinta-feira",
            "inicioTurno": "09:00",
            "fimTurno": "14:00"
          }
        ],
        "servicosProfissional": [
          {
            "servicoId": 1,
            "valor": 110.00
          }
        ]
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia DELETE para "/estabelecimentos/1/profissionais/{lastId}"
    Then o status HTTP da resposta deve ser 204

  Scenario: Listar profissionais de um estabelecimento retorna resultado paginado
    When o cliente envia GET para "/estabelecimentos/1/profissionais?page=0&size=10"
    Then o status HTTP da resposta deve ser 200
