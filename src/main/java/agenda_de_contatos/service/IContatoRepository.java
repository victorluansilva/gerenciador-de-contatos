package agenda_de_contatos.service;

import agenda_de_contatos.model.Contato;
import java.sql.SQLException;
import java.util.List;

public interface IContatoRepository {
    void setup();
    List<Contato> listarContatos();
    void adicionarContato(Contato contato) throws SQLException;
    void atualizarContato(Contato contato) throws SQLException;
    void excluirContato(Contato contato) throws SQLException;
    boolean testarConexao();
}