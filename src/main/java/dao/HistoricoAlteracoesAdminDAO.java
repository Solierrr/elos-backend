package dao;

import static exception.ErrosGerais.ERRO_GENERICO;
import static exception.ErrosGerais.SUCESSO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.GenericExceptionEnum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import model.HistoricoAlteracoesAdmin;

public final class HistoricoAlteracoesAdminDAO implements GenericDAO<HistoricoAlteracoesAdmin> {

    private static final String TABELA = "historico_alteracoes";

    @Override
    public GenericExceptionEnum insert(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirInsert(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        return preparedStatement.executeUpdate() >= 1 ? SUCESSO : ERRO_GENERICO;
    }

    @Override
    public List<HistoricoAlteracoesAdmin> readAll(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        List<HistoricoAlteracoesAdmin> historicoAlteracoesAdmins = new ArrayList<>();

        PreparedStatement preparedStatement = connection.prepareStatement(criarInstrucaoDinamica.construirSelect(TABELA));

        criarInstrucaoDinamica.aplicarValoresDoPreparedStatement(preparedStatement);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            historicoAlteracoesAdmins.add(new HistoricoAlteracoesAdmin(
                    resultSet.getLong("id"),
                    resultSet.getLong("id_admin"),
                    resultSet.getLong("id_registro"),
                    resultSet.getString("tabela_modificada"),
                    resultSet.getString("acao"),
                    lerJson(resultSet.getString("dados_antigos")),
                    lerJson(resultSet.getString("dados_atualizados")),
                    resultSet.getObject("data_alteracao", OffsetDateTime.class)
            ));
        }
        return historicoAlteracoesAdmins;
    }

    @Override
    public GenericExceptionEnum update(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection)  throws IllegalArgumentException{
        throw new IllegalArgumentException("Esse dao não suporta updates!");
    }

    @Override
    public GenericExceptionEnum delete(CriarInstrucaoDinamica criarInstrucaoDinamica, Connection connection) throws SQLException{
        throw new IllegalArgumentException("Esse dao não suporta deletes!");
    }

    private static JsonObject lerJson(String texto) {
        if (texto == null) {
            return null;
        }
        return JsonParser.parseString(texto).getAsJsonObject();
    }

}

