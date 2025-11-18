package agenda_de_contatos.service;

import agenda_de_contatos.model.Contato;
import agenda_de_contatos.util.RepositoryMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteRepository implements IContatoRepository {

    private static final String URL = "jdbc:sqlite:agenda_contatos.db";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    @Override
    public void setup() {
        String sql = "CREATE TABLE IF NOT EXISTS contatos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome VARCHAR(255) NOT NULL,"
                + "telefone VARCHAR(255) NOT NULL UNIQUE,"
                + "dataNasc DATE NULL,"
                + "email VARCHAR(255) NULL,"
                + "endereco VARCHAR(255) NULL"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contato> listarContatos() {
        List<Contato> contatosList = new ArrayList<>();
        String sql = "SELECT * FROM contatos";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contatosList.add(RepositoryMapper.mapResultSetToContato(rs));
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
            RepositoryMapper.mapContatoToStatement(stmt, contato);
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
            RepositoryMapper.mapContatoToStatement(stmt, contato);
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

}