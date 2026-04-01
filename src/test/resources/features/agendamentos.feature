# language: pt

Funcionalidade: Gerenciamento de Agendamentos
  Como cliente do sistema
  Quero realizar agendamentos de servicos
  Para que eu possa ser atendido no horario escolhido

  Cenario: Criar agendamento com dados validos
    Dado que existe uma base de dados com estabelecimento, profissional, servico e cliente configurados
    E que tenho os dados de um novo agendamento valido para segunda-feira
    Quando envio uma requisicao POST para "/estabelecimentos/1/agendamentos"
    Entao o status da resposta deve ser 201
    E o corpo da resposta deve conter o campo "id"
    E o campo "status" deve ser "AGENDADO"

  Cenario: Criar agendamento com payload invalido deve retornar erro de validacao
    Dado que tenho os dados de um agendamento com campos obrigatorios ausentes
    Quando envio uma requisicao POST para "/estabelecimentos/1/agendamentos"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "VALIDACAO_ENTRADA"

  Cenario: Criar agendamento fora do horario de funcionamento deve retornar erro de regra de negocio
    Dado que existe uma base de dados com estabelecimento, profissional, servico e cliente configurados
    E que tenho os dados de um novo agendamento fora do horario de funcionamento
    Quando envio uma requisicao POST para "/estabelecimentos/1/agendamentos"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "REGRA_NEGOCIO"
    E a mensagem de erro deve conter "fora do horário de funcionamento do estabelecimento"

  Cenario: Criar agendamento em horario ja ocupado pelo profissional deve retornar erro de regra de negocio
    Dado que existe um agendamento criado para o cenario de conflito
    E que tenho os dados de um agendamento em horario ja ocupado pelo profissional
    Quando envio uma requisicao POST para "/estabelecimentos/1/agendamentos"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "REGRA_NEGOCIO"
    E a mensagem de erro deve conter "Profissional já possui agendamento no horário informado"

  Cenario: Buscar agendamento por ID inexistente deve retornar 404
    Quando envio uma requisicao GET para "/estabelecimentos/1/agendamentos/99999"
    Entao o status da resposta deve ser 404
    E o codigo de erro deve ser "NAO_ENCONTRADO"

  Cenario: Atualizar status de agendamento existente
    Dado que existe um agendamento criado para o cenario de atualizacao
    E que tenho os dados de atualizacao de agendamento com status CONCLUIDO
    Quando envio uma requisicao PUT para o ultimo agendamento criado no estabelecimento 1
    Entao o status da resposta deve ser 200
    E o campo "status" deve ser "CONCLUIDO"

  Cenario: Deletar agendamento existente deve retornar 204
    Dado que existe um agendamento criado para o cenario de exclusao
    Quando envio uma requisicao DELETE para o ultimo agendamento criado no estabelecimento 1
    Entao o status da resposta deve ser 204
