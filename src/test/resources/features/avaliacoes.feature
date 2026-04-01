# language: pt

Funcionalidade: Gerenciamento de Avaliacoes
  Como cliente
  Quero avaliar o servico recebido
  Para que outros clientes possam tomar decisoes informadas

  Cenario: Criar avaliacao para agendamento concluido
    Dado que existe um agendamento concluido para avaliacao
    E que tenho os dados de uma nova avaliacao valida
    Quando envio uma requisicao POST para o agendamento de avaliacao no estabelecimento 1
    Entao o status da resposta deve ser 201
    E o corpo da resposta deve conter o campo "id"

  Cenario: Criar avaliacao para agendamento nao concluido deve retornar erro de regra de negocio
    Dado que existe um agendamento criado para avaliacao ainda nao concluido
    E que tenho os dados de uma nova avaliacao valida
    Quando envio uma requisicao POST para o agendamento de avaliacao no estabelecimento 1
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "REGRA_NEGOCIO"
    E a mensagem de erro deve conter "Agendamento deve estar com status CONCLUIDO para receber avaliação"

  Cenario: Buscar avaliacao por ID inexistente deve retornar 404
    Dado que existe um agendamento concluido para avaliacao
    Quando envio uma requisicao GET para uma avaliacao inexistente do agendamento de avaliacao no estabelecimento 1
    Entao o status da resposta deve ser 404
    E o codigo de erro deve ser "NAO_ENCONTRADO"

  Cenario: Listar avaliacoes do estabelecimento
    Dado que existe um agendamento concluido para avaliacao
    Quando envio uma requisicao GET para "/estabelecimentos/1/avaliacoes"
    Entao o status da resposta deve ser 200

  Cenario: Deletar avaliacao existente deve retornar 204
    Dado que existe uma avaliacao criada para o cenario de exclusao
    Quando envio uma requisicao DELETE para a ultima avaliacao criada no estabelecimento 1
    Entao o status da resposta deve ser 204
