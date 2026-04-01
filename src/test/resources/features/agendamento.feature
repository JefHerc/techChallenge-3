Feature: Agendamento de Serviços
  Como cliente de um estabelecimento de beleza e bem-estar
  Quero realizar agendamentos online
  Para garantir meu horário com o profissional desejado

  Scenario: Criar agendamento com dados válidos
    When o cliente envia POST para "/estabelecimentos/1/agendamentos" com o corpo:
      """
      {
        "profissionalId": 1,
        "servicoId": 1,
        "clienteId": 1,
        "dataHoraInicio": "2030-07-01T09:00:00"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And o campo JSON "status" deve ser "AGENDADO"
    And o campo JSON "profissionalId" deve ser "1"
    And o campo JSON "clienteId" deve ser "1"

  Scenario: Tentar criar agendamento informando status retorna erro de validação
    When o cliente envia POST para "/estabelecimentos/1/agendamentos" com o corpo:
      """
      {
        "profissionalId": 1,
        "servicoId": 1,
        "clienteId": 1,
        "dataHoraInicio": "2030-07-02T10:00:00",
        "status": "AGENDADO"
      }
      """
    Then o status HTTP da resposta deve ser 400
    And o campo JSON "code" deve ser "VALIDACAO_ENTRADA"
    And a resposta deve conter um erro de validação no campo "status"

  Scenario: Buscar agendamento inexistente retorna not found
    When o cliente envia GET para "/estabelecimentos/1/agendamentos/99999"
    Then o status HTTP da resposta deve ser 404
    And o campo JSON "code" deve ser "NAO_ENCONTRADO"

  Scenario: Criar e em seguida atualizar agendamento
    When o cliente envia POST para "/estabelecimentos/1/agendamentos" com o corpo:
      """
      {
        "profissionalId": 1,
        "servicoId": 1,
        "clienteId": 1,
        "dataHoraInicio": "2030-08-15T14:00:00"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia PUT para "/estabelecimentos/1/agendamentos/{lastId}" com o corpo:
      """
      {
        "profissionalId": 1,
        "servicoId": 1,
        "clienteId": 1,
        "dataHoraInicio": "2030-08-15T14:00:00",
        "status": "CONCLUIDO"
      }
      """
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "status" deve ser "CONCLUIDO"

  Scenario: Criar e em seguida excluir agendamento
    When o cliente envia POST para "/estabelecimentos/1/agendamentos" com o corpo:
      """
      {
        "profissionalId": 1,
        "servicoId": 1,
        "clienteId": 1,
        "dataHoraInicio": "2030-09-20T10:00:00"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia DELETE para "/estabelecimentos/1/agendamentos/{lastId}"
    Then o status HTTP da resposta deve ser 204

  Scenario: Listar agendamentos de um estabelecimento retorna paginação
    When o cliente envia GET para "/estabelecimentos/1/agendamentos?page=0&size=20"
    Then o status HTTP da resposta deve ser 200

  Scenario: Listar agendamentos de um estabelecimento por período informado
    When o cliente envia POST para "/estabelecimentos/1/agendamentos" com o corpo:
      """
      {
        "profissionalId": 1,
        "servicoId": 1,
        "clienteId": 1,
        "dataHoraInicio": "2031-10-15T13:00:00"
      }
      """
    Then o status HTTP da resposta deve ser 201
    When o cliente envia GET para "/estabelecimentos/1/agendamentos/periodo?dataInicial=2031-10-01&dataFinal=2031-10-31"
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "[0].estabelecimentoId" deve ser "1"
    And o campo JSON "[0].status" deve ser "AGENDADO"
