package agenda_de_contatos.util;

import agenda_de_contatos.model.Contato;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class RepositoryMapper {

    public static Contato mapResultSetToContato(ResultSet rs) throws SQLException {
        Contato contato = new Contato();
        contato.setId(rs.getInt("id"));
        contato.setNome(rs.getString("nome"));
        contato.setTelefone(rs.getString("telefone"));
        java.sql.Date sqlDate = rs.getDate("dataNasc");
        if (sqlDate != null) {
            contato.setDataNascimento(sqlDate.toLocalDate());
        }
        contato.setEmail(rs.getString("email"));
        contato.setEndereco(rs.getString("endereco"));
        return contato;
    }

    public static void mapContatoToStatement(PreparedStatement stmt, Contato contato) throws SQLException {
        stmt.setString(1, contato.getNome());
        stmt.setString(2, contato.getTelefone());
        if (contato.getDataNascimento() != null) {
            stmt.setDate(3, java.sql.Date.valueOf(contato.getDataNascimento()));
        } else {
            stmt.setNull(3, Types.DATE);
        }
        stmt.setString(4, contato.getEmail());
        stmt.setString(5, contato.getEndereco());
    }
}