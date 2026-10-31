const mensagens = {
  "usuario": {
    "erro-id": "O ID inserido é inválido",
    "erro-email": "E-mail inválido",
    "erro-senha": "A senha está fraca",
    "erro-nome": "Nome inválido",
    "erro-raio-procura-km": "Raio de procura inválido",
    "erro-tipo-usuario": "Selecione um tipo de usuário",
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-tipo-fornecedor": "Selecione um tipo do fornecedor",
    "erro-profissao": "Profissão inválida",
    "erro-cpf": "CPF inválido",
    "erro-id-fornecedor": "ID do fornecedor inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "profissional": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-profissao": "Profissão inválida",
    "erro-cpf": "CPF inválido",
    "erro-id-fornecedor": "ID do fornecedor inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "fornecedor": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-tipo-fornecedor": "Selecione um tipo do fornecedor",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "empresa-demandante": {
    "erro-id": "O ID inserido é inválido",
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "telefone": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-telefone": "O número inserido está inválido",
    "erro-tipo": "O tipo do telefone escolhido está inválido",
    "erro-principal": "Erro o valor dde principal está inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "telefone-admin": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-admin": "ID do admin inválido",
    "erro-telefone": "O número inserido está inválido",
    "erro-tipo": "O tipo do telefone escolhido está inválido",
    "erro-principal": "Erro o valor dde principal está inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "acesso-admin": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-admin": "ID do admin inválido",
    "erro-data-acesso": "Data de acesso inválida",
    "erro-sucesso": "Sucesso inválido",
    "erro-motivo-falha": "Motivo da falha inválido",
    "erro-navegador": "Navegador inválido",
    "erro-sistema-operacional": "Sistema operacional inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "historico-alteracoes": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-admin": "ID do admin inválido",
    "erro-id-registro": "ID do registro inválido",
    "erro-data-alteracao": "Data de alteração inválida",
    "erro-acao": "A ação escolhida é inválida",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "admin": {
    "erro-id": "O ID inserido é inválido",
    "erro-email": "E-mail inválido",
    "erro-nome": "Nome inválido",
    "erro-funcao": "Função inválida",
    "erro-em-atividade": "Em atividade inválido",
    "erro-senha": "A senha está fraca",
    "erro-join": "O registro inserido não se conecta com o atual"
  }, "endereco": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-cidade": "A cidade inserida é inválida",
    "erro-estado": "O estado escolhido é inválido",
    "erro-bairro": "O bairro inserido é inválido",
    "erro-cep": "O CEP escolhido é inválido",
    "erro-logradouro": "O logradouro inserido é inválido",
    "erro-numero": "O numero inserido é inválido",
    "erro-complemento": "O complemento inserido é inválido",
    "erro-join": "O registro inserido não se conecta com o atual"
  }
};

export const mensagensSpans = (nomeTela) => {
  return mensagens[nomeTela];
}