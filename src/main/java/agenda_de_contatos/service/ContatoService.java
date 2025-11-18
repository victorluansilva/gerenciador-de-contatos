package agenda_de_contatos.service;

import agenda_de_contatos.model.Contato;
import agenda_de_contatos.util.NotificationUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContatoService {

    private static final List<Contato> contatosEmMemoria = new ArrayList<>();
    private IContatoRepository repository;
    private ConfigService configService;

    public ContatoService() {
        this.configService = new ConfigService();
        if (configService.getStorageType().equals("mysql")) {
            this.repository = new MysqlRepository(configService);
        } else {
            this.repository = new SqliteRepository();
            this.repository.setup();
        }
        carregarContatosDoBanco();
    }

    public boolean isConexaoMySQL() {
        return configService.getStorageType().equals("mysql");
    }

    private void carregarContatosDoBanco() {
        contatosEmMemoria.clear();
        contatosEmMemoria.addAll(repository.listarContatos());
    }

    public List<Contato> listarContatos() {
        carregarContatosDoBanco();
        return contatosEmMemoria;
    }

    public boolean isTelefoneUnico(String telefone, int currentContatoId) {
        for (Contato c : contatosEmMemoria) {
            if (c.getTelefone() != null && c.getTelefone().equals(telefone)) {
                if (c.getId() != currentContatoId) {
                    return false;
                }
            }
        }
        return true;
    }

    public void adicionarContato(Contato contato) {
        try {
            repository.adicionarContato(contato);
            contatosEmMemoria.add(contato);
        } catch (SQLException e) {
            NotificationUtil.showInfoAlert("Operação em Memória", "Conexão indisponível. O novo contato foi salvo apenas nesta sessão.");
            contato.setId(-(contatosEmMemoria.size() + 1));
            contatosEmMemoria.add(contato);
            e.printStackTrace();
        }
    }

    public void atualizarContato(Contato contato) {
        int indexNaLista = -1;
        for (int i = 0; i < contatosEmMemoria.size(); i++) {
            if (contatosEmMemoria.get(i).getId() == contato.getId()) {
                indexNaLista = i;
                break;
            }
        }

        if (contato.getId() < 0) {
            if (indexNaLista != -1) contatosEmMemoria.set(indexNaLista, contato);
            NotificationUtil.showInfoAlert("Operação em Memória", "Contato atualizado na sessão. Sincronize para salvar no banco.");
            return;
        }

        try {
            repository.atualizarContato(contato);
            if (indexNaLista != -1) {
                contatosEmMemoria.set(indexNaLista, contato);
            }
        } catch (SQLException e) {
            if (indexNaLista != -1) contatosEmMemoria.set(indexNaLista, contato);
            NotificationUtil.showInfoAlert("Operação em Memória", "Conexão indisponível. O contato foi atualizado apenas nesta sessão.");
            e.printStackTrace();
        }
    }


    public void excluirContato(Contato contato) {
        boolean removed = contatosEmMemoria.remove(contato);

        if (contato.getId() < 0) {
            if(removed) NotificationUtil.showInfoAlert("Operação em Memória", "Contato offline removido da sessão.");
            return;
        }

        try {
            repository.excluirContato(contato);
        } catch (SQLException e) {
            if(removed) {
                contatosEmMemoria.add(contato);
            }
            NotificationUtil.showInfoAlert("Operação em Memória", "Conexão indisponível. O contato *não* foi excluído.");
            e.printStackTrace();
        }
    }

    public boolean testarConexaoDB() {
        return repository.testarConexao();
    }

    public void sincronizarComBanco() {
        if (!isConexaoMySQL() || !repository.testarConexao()) {
            NotificationUtil.showInfoAlert("Sincronização Falhou", "Não foi possível conectar ao banco MySQL.");
            return;
        }

        List<Contato> contatosParaSincronizar = contatosEmMemoria.stream()
                .filter(u -> u.getId() < 0)
                .collect(Collectors.toList());

        if (!contatosParaSincronizar.isEmpty()) {
            int sucessoCount = 0;
            for (Contato contatoSinc : contatosParaSincronizar) {
                try {
                    repository.adicionarContato(contatoSinc);
                    sucessoCount++;
                } catch (SQLException e) {
                    System.err.println("Erro ao sincronizar contato " + contatoSinc.getNome() + ": " + e.getMessage());
                }
            }
            NotificationUtil.showInfoAlert("Sincronização", sucessoCount + " contato(s) foram salvos no banco de dados.");
        } else {
            NotificationUtil.showInfoAlert("Reconectado", "Conexão com o banco de dados restabelecida. Nenhum dado pendente.");
        }
        carregarContatosDoBanco();
    }

}