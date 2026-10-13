package dao;

import static exception.ErrosDadosProfissional.CPF_INVALIDO;
import static exception.ErrosDadosProfissional.ID_FORNECEDOR_NAO_REGISTRADO;
import static exception.ErrosDadosTelefone.TELEFONE_INVALIDO;
import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGeraisDados.CNPJ_INVALIDO;
import static exception.ErrosGeraisDados.EMAIL_INVALIDO;
import static exception.ErrosGeraisDados.ID_USUARIO_INVALIDO;
import static exception.ErrosGeraisDados.ID_USUARIO_NAO_REGISTRADO;
import static exception.ErrosGeraisDados.TIPO_USUARIO_INCOMPATIVEL;

import exception.GenericExceptionEnum;
import java.sql.SQLException;

public enum ConstraintsBanco {

    EMPRESA_DEMANDANTE_CNPJ_KEY("empresa_demandante_cnpj_key", CNPJ_INVALIDO),
    EMPRESA_DEMANDANTE_ID_USUARIO_KEY("empresa_demandante_id_usuario_key", ID_USUARIO_INVALIDO),
    EMPRESA_DEMANDANTE_ID_USUARIO_TIPO_USUARIO_FKEY("empresa_demandante_id_usuario_tipo_usuario_fkey", TIPO_USUARIO_INCOMPATIVEL),
    ENDERECO_ID_USUARIO_FKEY("endereco_id_usuario_fkey", ID_USUARIO_NAO_REGISTRADO),
    FORNECEDOR_CNPJ_KEY("fornecedor_cnpj_key", CNPJ_INVALIDO),
    FORNECEDOR_ID_USUARIO_KEY("fornecedor_id_usuario_key", ID_USUARIO_NAO_REGISTRADO),
    FORNECEDOR_ID_USUARIO_TIPO_USUARIO_FKEY("fornecedor_id_usuario_tipo_usuario_fkey", TIPO_USUARIO_INCOMPATIVEL),
    PROFISSIONAL_CPF_HMAC_KEY("profissional_cpf_hmac_key", CPF_INVALIDO),
    PROFISSIONAL_ID_FORNECEDOR_FKEY("profissional_id_fornecedor_fkey", ID_FORNECEDOR_NAO_REGISTRADO),
    PROFISSIONAL_ID_USUARIO_KEY("profissional_id_usuario_key", ID_USUARIO_NAO_REGISTRADO),
    PROFISSIONAL_ID_USUARIO_TIPO_USUARIO_FKEY("profissional_id_usuario_tipo_usuario_fkey", TIPO_USUARIO_INCOMPATIVEL),
    TELEFONE_ID_USUARIO_FKEY("telefone_id_usuario_fkey", ID_USUARIO_NAO_REGISTRADO),
    TELEFONE_TELEFONE_KEY("telefone_telefone_key", TELEFONE_INVALIDO),
    COMBINACAO_ID_TIPO_USUARIO_UNICA("combinacao_id_tipo_usuario_unica", ERRO_GENERICO),
    USUARIO_EMAIL_KEY("usuario_email_key", EMAIL_INVALIDO),
    ADMIN_EMAIL_KEY("admin_email_key", EMAIL_INVALIDO),
    HISTORICO_ALTERACOES_ID_ADMIN_FKEY("historico_alteracoes_id_admin_fkey", null),
    LOG_ACESSOS_ADMIN_ID_ADMIN_FKEY("log_acessos_admin", null),
    TELEFONE_ADMIN_ID_ADMIN_FKEY("telefone_admin_id_admin_fkey", TELEFONE_INVALIDO),
    TELEFONE_ADMIN_TELEFONE_KEY("telefone_admin_telefone_key", ID_USUARIO_NAO_REGISTRADO);

    private final String constraint;
    private final GenericExceptionEnum enumAssociado;

    ConstraintsBanco(String constraint, GenericExceptionEnum enumAssociado) {
        this.constraint = constraint;
        this.enumAssociado = enumAssociado;
    }

    public String getConstraint() {
        return constraint;
    }

    public GenericExceptionEnum getEnumAssociado() {
        return enumAssociado;
    }

    public boolean foiCausado(SQLException sqlException){
        return sqlException.getMessage().contains(getConstraint());
    }

    public static GenericExceptionEnum foiCausadoPorQual(SQLException sqlException){
        if (sqlException == null){
            throw new IllegalArgumentException("Nenhuma exceção sql tinha sido lançada!");
        }

        for(ConstraintsBanco constraintsBanco : ConstraintsBanco.values()){
            if(constraintsBanco.foiCausado(sqlException))
                return constraintsBanco.getEnumAssociado();
        }

        throw new IllegalArgumentException("Constraint não mapeada! "+ sqlException.getMessage());
    }

}
