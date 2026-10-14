package service.historicoAlteracoesAdmin;

import dao.CriarInstrucaoDinamica;
import dao.HistoricoAlteracoesAdminDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import model.HistoricoAlteracoesAdmin;

public final class HistoricoAlteracoesAdminService {

    public static void salvarAcao(HistoricoAlteracoesAdmin historicoAlteracoesAdmin, Connection connection) throws SQLException{

        CriarInstrucaoDinamica criarInstrucaoDinamica = new CriarInstrucaoDinamica();

        criarInstrucaoDinamica.setCampo("id_admin", historicoAlteracoesAdmin.getIdAdmin(), Types.BIGINT);
        criarInstrucaoDinamica.setCampo("id_registro", historicoAlteracoesAdmin.getIdRegistro(), Types.BIGINT);
        criarInstrucaoDinamica.setCampo("tabela_modificada", historicoAlteracoesAdmin.getTabelaModificada(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo("acao", historicoAlteracoesAdmin.getAcao(), Types.VARCHAR);
        criarInstrucaoDinamica.setCampo("dados_antigos", historicoAlteracoesAdmin.getDadosAntigos(), Types.OTHER);
        criarInstrucaoDinamica.setCampo("dados_atualizados", historicoAlteracoesAdmin.getDadosAtualizados(), Types.OTHER);

        HistoricoAlteracoesAdminDAO dao = new HistoricoAlteracoesAdminDAO();

        dao.insert(criarInstrucaoDinamica, connection);
    }


}
