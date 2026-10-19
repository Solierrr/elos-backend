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
  }

};

export const mensagensSpans = (nomeTela) => {
  return mensagens[nomeTela];
}