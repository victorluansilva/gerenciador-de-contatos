package agenda_de_contatos.service;

import agenda_de_contatos.model.Contato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlRepository implements IContatoRepository {

    private ConfigService configService;

    public MysqlRepository(ConfigService configService) {
        this.configService = configService;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                configService.getMysqlUrl(),
                configService.getMysqlUser(),
                configService.getMysqlPass()
        );
    }

    @Override
    public void setup() {

    }

    @Override
    public List<Contato> listarContatos() {
        List<Contato> contatosList = new ArrayList<>();
        String sql = "SELECT * FROM contatos";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contatosList.add(mapResultSetToContato(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contatosList;
    }

    @Override
    public void adicionarContato(Contato contato) throws SQLException {
        String sql = "INSERT INTO contatos(nome, telefone, dataNasc, email, endereco) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapContatoToStatement(stmt, contato);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contato.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizarContato(Contato contato) throws SQLException {
        String sql = "UPDATE contatos SET nome = ?, telefone = ?, dataNasc = ?, email = ?, endereco = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            mapContatoToStatement(stmt, contato);
            stmt.setInt(6, contato.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void excluirContato(Contato contato) throws SQLException {
        String sql = "DELETE FROM contatos WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contato.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean testarConexao() {
        try (Connection conn = getConnection()) {
            return conn != null;
        } catch (SQLException e) {
            return false;
        }
    }

    private Contato mapResultSetToContato(ResultSet rs) throws SQLException {
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

    private void mapContatoToStatement(PreparedStatement stmt, Contato contato) throws SQLException {
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