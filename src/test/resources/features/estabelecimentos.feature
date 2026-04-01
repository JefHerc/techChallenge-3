# language: pt

Funcionalidade: Gerenciamento de Estabelecimentos
  Como dono de estabelecimento
  Quero gerenciar meu cadastro no sistema
  Para que clientes possam me encontrar e agendar servicos

  Cenario: Criar um novo estabelecimento com dados validos
    Dado que tenho os dados de um novo estabelecimento valido
    Quando envio uma requisicao POST para "/estabelecimentos"
    Entao o status da resposta deve ser 201
    E o corpo da resposta deve conter o nome "Studio BDD Novo"
    E o corpo da resposta deve conter o campo "id"

  Cenario: Criar estabelecimento com payload invalido deve retornar erro de validacao
    Dado que tenho os dados de um estabelecimento com payload invalido
    Quando envio uma requisicao POST para "/estabelecimentos"
    Entao o status da resposta deve ser 400
    E o codigo de erro deve ser "VALIDACAO_ENTRADA"

  Cenario: Buscar estabelecimento por ID existente
    Dado que existe um estabelecimento com id 1 no banco de dados
    Quando envio uma requisicao GET para "/estabelecimentos/1"
    Entao o status da resposta deve ser 200
    E o corpo da resposta deve conter o campo "id"

  Cenario: Buscar estabelecimento por ID inexistente deve retornar 404
    Quando envio uma requisicao GET para "/estabelecimentos/99999"
    Entao o status da resposta deve ser 404
    E o codigo de erro deve ser "NAO_ENCONTRADO"

  Cenario: Listar estabelecimentos cadastrados deve retornar resultados
    Quando envio uma requisicao GET para "/estabelecimentos?page=0&size=10"
    Entao o status da resposta deve ser 200
    E o corpo da resposta deve conter o nome "Studio BDD"

  Cenario: Atualizar estabelecimento com dados validos
    Dado que existe um estabelecimento criado para o cenario de atualizacao
    E que tenho os dados de um estabelecimento para atualizacao
    Quando envio uma requisicao PUT para o ultimo estabelecimento cadastrado
    Entao o status da resposta deve ser 200
    E o corpo da resposta deve conter o nome "Studio BDD Atualizado"

  Cenario: Deletar estabelecimento existente deve retornar 204
    Dado que existe um estabelecimento criado para o cenario de exclusao
    Quando envio uma requisicao DELETE para o ultimo estabelecimento cadastrado
    Entao o status da resposta deve ser 204
