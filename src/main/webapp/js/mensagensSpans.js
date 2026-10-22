const mensagens = {
  "usuario": {
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
    "erro-id-usuario": "ID do usuário inválido",
    "erro-profissao": "Profissão inválida",
    "erro-cpf": "CPF inválido",
    "erro-id-fornecedor": "ID do fornecedor inválido"
  }, "fornecedor": {
    "erro-id-usuario": "ID do usuário inválido",
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-tipo-fornecedor": "Selecione um tipo do fornecedor"
  }, "empresa-demandante":{
    "erro-cnpj": "CNPJ inválido",
    "erro-razao-social": "Razão social inválida",
    "erro-eh-mandante": "Opção inválida",
    "erro-id-usuario": "ID do usuário inválido"
  }, "telefone":{
    "erro-id-usuario": "ID do usuário inválido",
    "erro-telefone": "O número inserido está inválido",
    "erro-tipo": "O tipo do telefone escolhido está inválido",
    "erro-principal": "Erro o valor dde principal está inválido"
  }, "telefone-admin": {
    "erro-id-admin": "ID do admin inválido",
    "erro-telefone": "O número inserido está inválido",
    "erro-tipo": "O tipo do telefone escolhido está inválido",
    "erro-principal": "Erro o valor dde principal está inválido"
  }, "acesso-admin":{
    "erro-id-admin": "ID do admin inválido",
    "erro-data-acesso":  "Data de acesso inválida",
    "erro-sucesso":  "Sucesso inválido",
    "erro-motivo-falha": "Motivo da falha inválido",
    "erro-navegador": "Navegador inválido",
    "erro-sistema-operacional":  "Sistema operacional inválido"
  }

};

export const mensagensSpans = (nomeTela) => {
  return mensagens[nomeTela];
}