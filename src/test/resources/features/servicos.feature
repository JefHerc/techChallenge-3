# language: pt

Funcionalidade: Gerenciamento de Servicos do Estabelecimento
  Como dono de estabelecimento
  Quero gerenciar os servicos oferecidos pelo meu estabelecimento
  Para que profissionais possam ser vinculados e clientes possam agendar

  Cenario: Cadastrar servicos no estabelecimento com sucesso
    Dado que existe um estabelecimento com id 1 no banco de dados
    E que tenho a lista de servicos para cadastro
    Quando envio uma requisicao POST para "/estabelecimentos/1/servicos"
    Entao o status da resposta deve ser 201

  Cenario: Buscar servico por ID existente no estabelecimento
    Dado que existe um servico com id 1 no estabelecimento com id 1
    Quando envio uma requisicao GET para "/estabelecimentos/1/servicos/1"
    Entao o status da resposta deve ser 200
    E o corpo da resposta deve conter o campo "id"

  Cenario: Buscar servico por ID inexistente deve retornar 404
    Quando envio uma requisicao GET para "/estabelecimentos/1/servicos/99999"
    Entao o status da resposta deve ser 404
    E o codigo de erro deve ser "NAO_ENCONTRADO"

  Cenario: Deletar servico existente deve retornar 204
    Dado que existe um servico criado para o cenario de exclusao no estabelecimento 1
    Quando envio uma requisicao DELETE para o ultimo servico criado no estabelecimento 1
    Entao o status da resposta deve ser 204
