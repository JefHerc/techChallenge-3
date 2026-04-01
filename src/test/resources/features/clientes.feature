# language: pt

Funcionalidade: Gerenciamento de Clientes
  Como administrador do sistema
  Quero gerenciar o cadastro de clientes
  Para que eu possa manter os dados atualizados e garantir o funcionamento dos agendamentos

  Cenario: Criar um novo cliente com dados validos
    Dado que tenho os dados de um novo cliente valido
    Quando envio uma requisicao POST para "/clientes"
    Entao o status da resposta deve ser 201
    E o corpo da resposta deve conter o nome "Ana Lima BDD"
    E o corpo da resposta deve conter o campo "id"

  Cenario: Criar cliente com email invalido deve retornar erro de validacao
    Dado que tenho os dados de um cliente com email invalido
    Quando envio uma requisicao POST para "/clientes"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "VALIDACAO_ENTRADA"

  Cenario: Buscar cliente por ID existente
    Dado que existe um cliente com id 1 no banco de dados
    Quando envio uma requisicao GET para "/clientes/1"
    Entao o status da resposta deve ser 200
    E o corpo da resposta deve conter o campo "id"

  Cenario: Buscar cliente por ID inexistente deve retornar 404
    Quando envio uma requisicao GET para "/clientes/99999"
    Entao o status da resposta deve ser 404
    E o codigo de erro deve ser "NAO_ENCONTRADO"

  Cenario: Atualizar cliente com dados validos
    Dado que existe um cliente criado para o cenario de atualizacao
    E que tenho os dados de um cliente para atualizacao
    Quando envio uma requisicao PUT para o ultimo cliente cadastrado
    Entao o status da resposta deve ser 200
    E o corpo da resposta deve conter o nome "Cliente Atualizado BDD"

  Cenario: Deletar cliente existente deve retornar 204
    Dado que existe um cliente criado para o cenario de exclusao
    Quando envio uma requisicao DELETE para o ultimo cliente cadastrado
    Entao o status da resposta deve ser 204
