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
    "erro-eh-mandante": "Opção inválida",
    "erro-tipo-fornecedor": "Selecione um tipo do fornecedor",
    "erro-profissao": "Profissão inválida",
    "erro-cpf": "CPF inválido",
    "erro-id-fornecedor": "ID do fornecedor inválido"
  }, "profissional": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-profissao": "Profissão inválida",
    "erro-cpf": "CPF inválido",
    "erro-id-fornecedor": "ID do fornecedor inválido"
  }, "fornecedor": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-tipo-fornecedor": "Selecione um tipo do fornecedor"
  }, "empresa-demandante":{
    "erro-id": "O ID inserido é inválido",
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-eh-mandante": "Opção inválida",
    "erro-id-usuario": "ID do usuário inválido"
  }, "telefone":{
    "erro-id": "O ID inserido é inválido",
    "erro-id-usuario": "ID do usuário inválido",
    "erro-telefone": "O número inserido está inválido",
    "erro-tipo": "O tipo do telefone escolhido está inválido",
    "erro-principal": "Erro o valor dde principal está inválido"
  }, "telefone-admin": {
    "erro-id": "O ID inserido é inválido",
    "erro-id-admin": "ID do admin inválido",
    "erro-telefone": "O número inserido está inválido",
    "erro-tipo": "O tipo do telefone escolhido está inválido",
    "erro-principal": "Erro o valor dde principal está inválido"
  }, "acesso-admin":{
    "erro-id": "O ID inserido é inválido",
    "erro-id-admin": "ID do admin inválido",
    "erro-data-acesso":  "Data de acesso inválida",
    "erro-sucesso":  "Sucesso inválido",
    "erro-motivo-falha": "Motivo da falha inválido",
    "erro-navegador": "Navegador inválido",
    "erro-sistema-operacional":  "Sistema operacional inválido"
  }, "historico-alteracoes":{
    "erro-id": "O ID inserido é inválido",
    "erro-id-admin": "ID do admin inválido",
    "erro-id-registro": "ID do registro inválido",
    "erro-data-alteracao":  "Data de alteração inválida",
    "erro-acao": "A ação escolhida é inválida"
  }, "admin":{
    "erro-id": "O ID inserido é inválido",
    "erro-email": "E-mail inválido",
    "erro-nome": "Nome inválido",
    "erro-funcao": "Função inválida",
    "erro-em-atividade": "Em atividade inválido",
    "erro-senha": "A senha está fraca",


  }
};

export const mensagensSpans = (nomeTela) => {
  return mensagens[nomeTela];
}