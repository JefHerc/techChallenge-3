# language: pt

Funcionalidade: Gerenciamento de Profissionais
  Como gestor do estabelecimento
  Quero cadastrar e gerenciar profissionais
  Para organizar a agenda e os servicos disponiveis

  Cenario: Criar um novo profissional com dados validos
    Dado que tenho os dados de um novo profissional valido no estabelecimento 1
    Quando envio uma requisicao POST para "/estabelecimentos/1/profissionais"
    Entao o status da resposta deve ser 201
    E o corpo da resposta deve conter o nome "Profissional BDD Novo"
    E o corpo da resposta deve conter o campo "id"

  Cenario: Criar profissional com email invalido deve retornar erro de validacao
    Dado que tenho os dados de um profissional com email invalido
    Quando envio uma requisicao POST para "/estabelecimentos/1/profissionais"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "VALIDACAO_ENTRADA"

  Cenario: Criar profissional com expediente fora do horario do estabelecimento deve retornar erro de regra de negocio
    Dado que tenho os dados de um profissional com expediente fora do horario do estabelecimento
    Quando envio uma requisicao POST para "/estabelecimentos/1/profissionais"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "REGRA_NEGOCIO"
    E a mensagem de erro deve conter "Expediente do profissional no dia MONDAY deve estar dentro do horario de funcionamento do estabelecimento"

  Cenario: Buscar profissional por ID inexistente deve retornar 404
    Quando envio uma requisicao GET para "/estabelecimentos/1/profissionais/99999"
    Entao o status da resposta deve ser 404
    E o codigo de erro deve ser "NAO_ENCONTRADO"

  Cenario: Deletar profissional existente deve retornar 204
    Dado que existe um profissional criado para o cenario de exclusao no estabelecimento 1
    Quando envio uma requisicao DELETE para o ultimo profissional criado no estabelecimento 1
    Entao o status da resposta deve ser 204
