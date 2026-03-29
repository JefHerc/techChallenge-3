Feature: Gerenciamento de Serviços do Estabelecimento
  Como dono de um estabelecimento de beleza
  Quero cadastrar e gerenciar os serviços disponíveis
  Para que clientes possam visualizar e agendar os serviços oferecidos

  Scenario: Cadastrar serviço em estabelecimento com dados válidos
    When o cliente envia POST para "/estabelecimentos/1/servicos" com o corpo:
      """
      [
        {
          "nome": "PINTURA",
          "duracaoMedia": "PT1H30M"
        }
      ]
      """
    Then o status HTTP da resposta deve ser 201
    And o campo JSON "$[0].nome" deve ser "Pintura"
    And o campo JSON "$[0].duracaoMedia" deve ser "PT1H30M"

  Scenario: Tentar cadastrar serviço com nome vazio retorna erro de validação
    When o cliente envia POST para "/estabelecimentos/1/servicos" com o corpo:
      """
      [
        {
          "nome": "",
          "duracaoMedia": "PT1H"
        }
      ]
      """
    Then o status HTTP da resposta deve ser 400
    And o campo JSON "code" deve ser "VALIDACAO_ENTRADA"

  Scenario: Buscar serviço inexistente retorna not found
    When o cliente envia GET para "/estabelecimentos/1/servicos/99999"
    Then o status HTTP da resposta deve ser 404
    And o campo JSON "code" deve ser "NAO_ENCONTRADO"

  Scenario: Criar e em seguida atualizar serviço
    When o cliente envia POST para "/estabelecimentos/1/servicos" com o corpo:
      """
      [
        {
          "nome": "MANICURE",
          "duracaoMedia": "PT45M"
        }
      ]
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado da lista JSON
    When o cliente envia PUT para "/estabelecimentos/1/servicos/{lastId}" com o corpo:
      """
      {
        "nome": "MANICURE",
        "duracaoMedia": "PT1H"
      }
      """
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "duracaoMedia" deve ser "PT1H"

  Scenario: Criar e em seguida excluir serviço
    When o cliente envia POST para "/estabelecimentos/1/servicos" com o corpo:
      """
      [
        {
          "nome": "PEDICURE",
          "duracaoMedia": "PT45M"
        }
      ]
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado da lista JSON
    When o cliente envia DELETE para "/estabelecimentos/1/servicos/{lastId}"
    Then o status HTTP da resposta deve ser 204

  Scenario: Listar serviços de um estabelecimento retorna resultado paginado
    When o cliente envia GET para "/estabelecimentos/1/servicos?page=0&size=10"
    Then o status HTTP da resposta deve ser 200
