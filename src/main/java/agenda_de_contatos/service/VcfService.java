package agenda_de_contatos.service;

import agenda_de_contatos.model.Contato;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VcfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    // Salvar unico contato

    private static void montarVcfContato(Contato contato, StringBuilder vcf) {
        vcf.append("BEGIN:VCARD").append("\r\n");
        vcf.append("VERSION:3.0").append("\r\n");

        if (contato.getNome() != null && !contato.getNome().isBlank()) {
            vcf.append("FN:").append(contato.getNome()).append("\r\n");
            vcf.append("N:").append(contato.getNome()).append(";;;;").append("\r\n");
        }

        if (contato.getTelefone() != null && !contato.getTelefone().isBlank()) {
            vcf.append("TEL;TYPE=CELL:").append(contato.getTelefone()).append("\r\n");
        }

        if (contato.getEmail() != null && !contato.getEmail().isBlank()) {
            vcf.append("EMAIL:").append(contato.getEmail()).append("\r\n");
        }

        if (contato.getEndereco() != null && !contato.getEndereco().isBlank()) {
            vcf.append("ADR;TYPE=HOME:;;").append(contato.getEndereco()).append(";;;;").append("\r\n");
        }

        if (contato.getDataNascimento() != null) {
            vcf.append("BDAY:").append(contato.getDataNascimento().format(DATE_FORMAT)).append("\r\n");
        }

        vcf.append("END:VCARD").append("\r\n");
    }

    //  Salvar contatos juntos

    public static void salvarVarios(List<Contato> contatos, String caminhoArquivo) throws IOException {
        StringBuilder vcf = new StringBuilder();

        for (Contato contato : contatos) {
            montarVcfContato(contato, vcf);
            vcf.append("\r\n");
        }

        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            fos.write(vcf.toString().getBytes());
        } catch (IOException e) {
            throw new IOException("Erro ao salvar arquivo: " + caminhoArquivo, e);
        }
    }


    // Ler um contato

    public static Contato lerVcf(String caminhoArquivo) throws IOException {
        List<Contato> contatos = lerVarios(caminhoArquivo);
        return contatos.isEmpty() ? null : contatos.get(0);
    }


    // Ler vários contatos

    public static List<Contato> lerVarios(String caminhoArquivo) throws IOException {

        List<Contato> lista = new ArrayList<>();
        Contato contatoAtual = null;

        try (BufferedReader buffRead = new BufferedReader(new FileReader(caminhoArquivo))) {

            String linha;

            while ((linha = buffRead.readLine()) != null) {

                if (linha.startsWith("BEGIN:VCARD")) {
                    contatoAtual = new Contato();
                    continue;
                }

                if (linha.startsWith("END:VCARD")) {
                    if (contatoAtual != null)
                        lista.add(contatoAtual);
                    contatoAtual = null;
                    continue;
                }

                if (contatoAtual == null) continue;

                if (linha.startsWith("FN:")) {
                    contatoAtual.setNome(linha.substring(3));

                } else if (linha.startsWith("TEL")) {
                    String telefone = linha.substring(linha.indexOf(":") + 1);
                    contatoAtual.setTelefone(telefone);

                } else if (linha.startsWith("EMAIL:")) {
                    contatoAtual.setEmail(linha.substring(6));

                } else if (linha.startsWith("ADR")) {
                    String end = linha.substring(linha.indexOf(":") + 1)
                            .replace(";;", "")
                            .replace(";;;;", "");
                    contatoAtual.setEndereco(end);

                } else if (linha.startsWith("BDAY:")) {
                    String dataStr = linha.substring(5);
                    try {
                        LocalDate data = LocalDate.parse(dataStr, DATE_FORMAT);
                        contatoAtual.setDataNascimento(data);
                    } catch (Exception ignored) {}
                }
            }

        } catch (IOException e) {
            throw new IOException("Erro ao ler VCF: " + caminhoArquivo, e);
        }

        return lista;
    }

    public static void saveVcf(Contato contato, String caminhoArquivo) throws IOException {
        StringBuilder vcf = new StringBuilder();
        montarVcfContato(contato, vcf);

        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo)) {
            fos.write(vcf.toString().getBytes());
        } catch (IOException e) {
            throw new IOException("Erro ao salvar o arquivo: " + caminhoArquivo, e);
        }
    }
}
