Feature: Avaliações e Comentários
  Como cliente que utilizou um serviço
  Quero avaliar o estabelecimento e o profissional
  Para ajudar outros clientes a tomarem decisões informadas

  Scenario: Criar avaliação após agendamento com dados válidos
    Given que existe um agendamento criado para o estabelecimento 1
    When o cliente envia POST para "/agendamentos/{agendamentoId}/avaliacoes" com o corpo:
      """
      {
        "notaEstabelecimento": 4.5,
        "notaProfissional": 5.0,
        "comentarioEstabelecimento": "Ambiente limpo e atendimento excelente",
        "comentarioProfissional": "Profissional muito atenciosa"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And o campo JSON "notaEstabelecimento" deve ser 4.5
    And o campo JSON "notaProfissional" deve ser 5.0
    And o campo JSON "comentarioEstabelecimento" deve ser "Ambiente limpo e atendimento excelente"

  Scenario: Tentar criar avaliação com nota de profissional maior que 5 retorna erro de validação
    Given que existe um agendamento criado para o estabelecimento 1
    When o cliente envia POST para "/agendamentos/{agendamentoId}/avaliacoes" com o corpo:
      """
      {
        "notaEstabelecimento": 4.5,
        "notaProfissional": 5.5,
        "comentarioEstabelecimento": "Bom atendimento",
        "comentarioProfissional": "Otimo profissional"
      }
      """
    Then o status HTTP da resposta deve ser 400
    And o campo JSON "code" deve ser "VALIDACAO_ENTRADA"
    And a resposta deve conter um erro de validação no campo "notaProfissional"

  Scenario: Buscar avaliação inexistente retorna not found
    Given que existe um agendamento criado para o estabelecimento 1
    When o cliente envia GET para "/agendamentos/{agendamentoId}/avaliacoes/99999"
    Then o status HTTP da resposta deve ser 404
    And o campo JSON "code" deve ser "NAO_ENCONTRADO"

  Scenario: Criar e em seguida atualizar avaliação
    Given que existe um agendamento criado para o estabelecimento 1
    When o cliente envia POST para "/agendamentos/{agendamentoId}/avaliacoes" com o corpo:
      """
      {
        "notaEstabelecimento": 3.0,
        "notaProfissional": 4.0,
        "comentarioEstabelecimento": "Bom",
        "comentarioProfissional": "Atencioso"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia PUT para "/agendamentos/{agendamentoId}/avaliacoes/{lastId}" com o corpo:
      """
      {
        "notaEstabelecimento": 5.0,
        "notaProfissional": 5.0,
        "comentarioEstabelecimento": "Excelente!",
        "comentarioProfissional": "Muito profissional"
      }
      """
    Then o status HTTP da resposta deve ser 200
    And o campo JSON "notaEstabelecimento" deve ser 5.0
    And o campo JSON "comentarioEstabelecimento" deve ser "Excelente!"

  Scenario: Criar avaliação e em seguida excluí-la
    Given que existe um agendamento criado para o estabelecimento 1
    When o cliente envia POST para "/agendamentos/{agendamentoId}/avaliacoes" com o corpo:
      """
      {
        "notaEstabelecimento": 4.0,
        "notaProfissional": 4.0,
        "comentarioEstabelecimento": "Atendimento ok",
        "comentarioProfissional": "Bom servico"
      }
      """
    Then o status HTTP da resposta deve ser 201
    And salvo o id do recurso criado
    When o cliente envia DELETE para "/agendamentos/{agendamentoId}/avaliacoes/{lastId}"
    Then o status HTTP da resposta deve ser 204
