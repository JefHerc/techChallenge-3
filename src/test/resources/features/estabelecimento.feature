Feature: Cadastro de Estabelecimentos
  Como prestador de serviços de beleza e bem-estar
  Quero cadastrar meu estabelecimento no sistema
  Para que clientes possam encontrá-lo e agendar serviços

  Scenario: Criar estabelecimento com dados válidos
    When o cliente envia POST para "/estabelecimentos" com o corpo:
      """
      {
        "nome": "Studio Beleza BDD",
        "endereco": {
          "logradouro": "Rua das Flores",
          "numero": "100",
          "bairro": "Centro",
          "cidade": "Sao Paulo",
          "estado": "SP",
          "cep": "01001000"
        },
        "cnpj": "32.345.678/0001-00",
        "urlFotos": [],
        "horarioFuncionamento": []
      }
      """
    Then o status HTTP da resposta deve ser 201
    And o campo JSON "nome" deve ser "Studio Beleza BDD"
    And o campo JSON "cnpj" deve ser "32345678000100"

  Scenario: Tentar criar estabelecimento com nome muito curto retorna erro de validação
    When o cliente envia POST para "/estabelecimentos" com o corpo:
      """
      {
        "nome": "A",
        "endereco": {
          "logradouro": "Rua das Flores",
          "numero": "100",
          "bairro": "Centro",
          "cidade": "Sao Paulo",
          "estado": "SP",
          "cep": "01001000"
        },
        "cnpj": "11.444.777/0001-61",
        "urlFotos": [],
        "horarioFuncionamento": []
      }
      """
    Then o status HTTP da resposta deve ser 400
    And o campo JSON "code" deve ser "VALIDACAO_ENTRADA"
    And a resposta deve conter um erro de validação no campo "nome"

  Scenario: Buscar estabelecimento inexistente retorna not found
    When o cliente envia GET para "/estabelecimentos/99999"
    Then o status HTTP da resposta deve ser 404
    And o campo JSON "code" deve ser "NAO_ENCONTRADO"

  Scenario: Criar e em seguida atualizar estabelecimento
    When o cliente envia POST para "/estabelecimentos" com o corpo:
      """
      {
        "nome": "Studio Para Atualizar",
        "endereco": {
          "logradouro": "Avenida Central",
          "numero": "200",
          "bairro": "Jardins",
          "cidade": "Sao Paulo",
          "estado": "SP",
          "cep": "01310000"
        },
        "cnpj": "42.345.678/0001-56",
        "urlFotos": [],
        "horarioFuncionamento": []
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia PUT para "/estabelecimentos/{lastId}" com o corpo:
      """
      {
        "nome": "Studio Atualizado",
        "endereco": {
          "logradouro": "Avenida Central",
          "numero": "201",
          "bairro": "Jardins",
          "cidade": "Sao Paulo",
          "estado": "SP",
          "cep": "01310000"
        },
        "cnpj": "42.345.678/0001-56",
        "urlFotos": [],
        "horarioFuncionamento": []
      }
      """
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "nome" deve ser "Studio Atualizado"

  Scenario: Criar e em seguida excluir estabelecimento
    When o cliente envia POST para "/estabelecimentos" com o corpo:
      """
      {
        "nome": "Studio Para Excluir",
        "endereco": {
          "logradouro": "Rua B",
          "numero": "3",
          "bairro": "Vila Nova",
          "cidade": "Campinas",
          "estado": "SP",
          "cep": "13010000"
        },
        "cnpj": "52.345.678/0001-00",
        "urlFotos": [],
        "horarioFuncionamento": []
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia DELETE para "/estabelecimentos/{lastId}"
    Then o status HTTP da resposta deve ser 204

  Scenario: Buscar estabelecimentos por critério deve retornar lista
    When o cliente envia GET para "/estabelecimentos?page=0&size=10"
    Then o status HTTP da resposta deve ser 200

  Scenario: Buscar estabelecimentos com filtros avançados por cidade e serviço
    When o cliente envia GET para "/estabelecimentos/busca?cidade=Sao%20Paulo&estado=SP&servicoNome=CORTE&page=0&size=10"
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "content[0].nome" deve ser "Studio BDD"
    And o campo JSON "content[0].endereco.cidade" deve ser "Sao Paulo"
