package dao;

import java.sql.SQLException;

public enum ConstraintsBanco {

    EMPRESA_DEMANDANTE_CNPJ_KEY("empresa_demandante_cnpj_key"),
    EMPRESA_DEMANDANTE_ID_USUARIO_KEY("empresa_demandante_id_usuario_key"),
    EMPRESA_DEMANDANTE_ID_USUARIO_TIPO_USUARIO_FKEY("empresa_demandante_id_usuario_tipo_usuario_fkey"),
    ENDERECO_ID_USUARIO_FKEY("endereco_id_usuario_fkey"),
    FORNECEDOR_CNPJ_KEY("fornecedor_cnpj_key"),
    FORNECEDOR_ID_USUARIO_KEY("fornecedor_id_usuario_key"),
    FORNECEDOR_ID_USUARIO_TIPO_USUARIO_FKEY("fornecedor_id_usuario_tipo_usuario_fkey"),
    PROFISSIONAL_CPF_HMAC_KEY("profissional_cpf_hmac_key"),
    PROFISSIONAL_ID_FORNECEDOR_FKEY("profissional_id_fornecedor_fkey"),
    PROFISSIONAL_ID_USUARIO_KEY("profissional_id_usuario_key"),
    PROFISSIONAL_ID_USUARIO_TIPO_USUARIO_FKEY("profissional_id_usuario_tipo_usuario_fkey"),
    TELEFONE_ID_USUARIO_FKEY("telefone_id_usuario_fkey"),
    TELEFONE_TELEFONE_KEY("telefone_telefone_key"),
    COMBINACAO_ID_TIPO_USUARIO_UNICA("combinacao_id_tipo_usuario_unica"),
    USUARIO_EMAIL_KEY("usuario_email_key"),
    ADMIN_EMAIL_KEY("admin_email_key"),
    HISTORICO_ALTERACOES_ID_ADMIN_FKEY("historico_alteracoes_id_admin_fkey"),
    LOG_ACESSOS_ADMIN_ID_ADMIN_FKEY("log_acessos_admin"),
    TELEFONE_ADMIN_ID_ADMIN_FKEY("telefone_admin_id_admin_fkey"),
    TELEFONE_ADMIN_TELEFONE_KEY("telefone_admin_telefone_key");

    private final String constraint;

    ConstraintsBanco(String constraint) {
        this.constraint = constraint;
    }

    public String getConstraint() {
        return constraint;
    }

    public boolean foiCausado(SQLException sqlException){
        return getConstraint().contains(sqlException.getMessage());
    };

}
