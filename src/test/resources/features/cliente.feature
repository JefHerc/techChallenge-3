Feature: Cadastro de Clientes
  Como usuário do sistema
  Quero cadastrar e gerenciar meu perfil de cliente
  Para poder realizar agendamentos nos estabelecimentos

  Scenario: Cadastrar cliente com dados válidos
    When o cliente envia POST para "/clientes" com o corpo:
      """
      {
        "nome": "Maria da Silva",
        "cpf": "52998224725",
        "celular": "11987654321",
        "email": "maria.silva.bdd@email.com",
        "sexo": "FEMININO"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And o campo JSON "nome" deve ser "Maria da Silva"
    And o campo JSON "email" deve ser "maria.silva.bdd@email.com"

  Scenario: Tentar cadastrar cliente com email inválido retorna erro de validação
    When o cliente envia POST para "/clientes" com o corpo:
      """
      {
        "nome": "Joao Santos",
        "cpf": "98765432100",
        "celular": "11999998888",
        "email": "email-invalido",
        "sexo": "MASCULINO"
      }
      """
    Then o status HTTP da resposta deve ser 400
    And o campo JSON "code" deve ser "VALIDACAO_ENTRADA"
    And a resposta deve conter um erro de validação no campo "email"

  Scenario: Buscar cliente inexistente retorna not found
    When o cliente envia GET para "/clientes/99999"
    Then o status HTTP da resposta deve ser 404
    And o campo JSON "code" deve ser "NAO_ENCONTRADO"

  Scenario: Criar e em seguida atualizar cliente
    When o cliente envia POST para "/clientes" com o corpo:
      """
      {
        "nome": "Ana Lima",
        "cpf": "98765432100",
        "celular": "11988880000",
        "email": "ana.lima.bdd@email.com",
        "sexo": "FEMININO"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia PUT para "/clientes/{lastId}" com o corpo:
      """
      {
        "nome": "Ana Lima Souza",
        "cpf": "98765432100",
        "celular": "11988881111",
        "email": "ana.lima.souza@email.com",
        "sexo": "FEMININO"
      }
      """
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "nome" deve ser "Ana Lima Souza"
    And o campo JSON "email" deve ser "ana.lima.souza@email.com"

  Scenario: Criar e em seguida excluir cliente
    When o cliente envia POST para "/clientes" com o corpo:
      """
      {
        "nome": "Pedro Costa",
        "cpf": "27182818205",
        "celular": "11977776666",
        "email": "pedro.costa.bdd@email.com",
        "sexo": "MASCULINO"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia DELETE para "/clientes/{lastId}"
    Then o status HTTP da resposta deve ser 204

  Scenario: Listar clientes retorna resultado paginado
    When o cliente envia GET para "/clientes?page=0&size=10"
    Then o status HTTP da resposta deve ser 200
