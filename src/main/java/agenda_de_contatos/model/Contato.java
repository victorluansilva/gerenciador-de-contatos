package agenda_de_contatos.model;

import java.time.LocalDate;

public class Contato {

    private int id;
    private String nome;
    private String telefone;
    private LocalDate dataNasc;
    private String endereco;
    private String email;

    public Contato() {
        this.nome = "";
        this.telefone = "";
        this.dataNasc = null;
        this.endereco = "";
        this.email = "";
    }

    public Contato(String nome, String telefone, LocalDate dataNasc, String endereco, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.dataNasc = dataNasc;
        this.endereco = endereco;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNasc;
    }

    public void setDataNascimento(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
