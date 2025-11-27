package agenda_de_contatos.controller;

import agenda_de_contatos.model.Contato;
import agenda_de_contatos.service.ContatoService;
import agenda_de_contatos.util.NotificationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ContatoListController {

    @FXML
    private TableView<Contato> tableView;
    @FXML
    private TableColumn<Contato, String> colNome;
    @FXML
    private TableColumn<Contato, String> colTelefone;
    @FXML
    private TableColumn<Contato, String> colEmail;
    @FXML
    private TableColumn<Contato, Void> colAcoes;

    @FXML
    private Label statusLabel;
    @FXML
    private Button syncButton;

    @FXML
    private HBox statusBar;

    @FXML
    private TextField searchField;

    private ContatoService contatoService;
    private ObservableList<Contato> masterData = FXCollections.observableArrayList();
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize(){
        contatoService = new ContatoService();
        setupTableColumns();
        loadDataAndSetupFilter();
        adicionarBotoesDeAcao();
        atualizarStatusConexao();
    }

    private void setupTableColumns() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadDataAndSetupFilter() {
        masterData.setAll(contatoService.listarContatos());
        FilteredList<Contato> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(contato -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (contato.getNome() != null && contato.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (contato.getTelefone() != null && contato.getTelefone().contains(newValue)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Contato> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void atualizarStatusConexao(){
        if (contatoService.isConexaoMySQL()) {
            statusBar.setVisible(true);
            statusBar.setManaged(true);

            boolean isConnected = contatoService.testarConexaoDB();
            if (isConnected){
                statusLabel.setText("DB Status: connected");
                statusLabel.setStyle("-fx-text-fill: green");
                syncButton.setText("refresh");
                syncButton.setDisable(true);
                syncButton.setVisible(false);
            } else{
                statusLabel.setText("DB Status: offline");
                statusLabel.setStyle("-fx-text-fill: red");
                syncButton.setText("retry");
                syncButton.setDisable(false);
                syncButton.setVisible(true);
            }
        } else {
            statusBar.setVisible(false);
            statusBar.setManaged(false);
        }
    }

    @FXML
    private void handleSincronizar(){
        contatoService.sincronizarComBanco();
        loadDataAndSetupFilter();
        atualizarStatusConexao();
    }

    private void refreshTableData() {
        loadDataAndSetupFilter();
        atualizarStatusConexao();
    }

    private void adicionarBotoesDeAcao() {
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox pane = new HBox(5, btnEditar, btnExcluir);

            {
                btnEditar.setOnAction(event -> {
                    Contato contato = getTableView().getItems().get(getIndex());
                    if (mainController != null) {
                        mainController.showEditForm(contato);
                    }
                });
                btnExcluir.setOnAction(event -> {
                    Contato contato = getTableView().getItems().get(getIndex());
                    contatoService.excluirContato(contato);
                    NotificationUtil.showSuccessToast(tableView,"Contato excluído com sucesso!");
                    refreshTableData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    @FXML
    private void handleAdicionarContato() {
        if (mainController != null) {
            mainController.showEditForm(null);
        }
    }

    @FXML
    private void handleExportar() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar contatos");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo VCF (*.vcf)", "*.vcf")
        );
        Stage stage = (Stage) tableView.getScene().getWindow();


        File arquivo = fileChooser.showSaveDialog(stage);
        if (arquivo == null) return;

        exportarContatos(arquivo);
    }

    private void exportarContatos(File arquivo) {

        try (FileWriter fw = new FileWriter(arquivo)) {

            List<Contato> contatos = contatoService.listarContatos();

            fw.write("Nome;Telefone;Email\n"); // cabeçalho

            for (Contato c : contatos) {
                fw.write(
                        (c.getNome() != null ? c.getNome() : "") + ";" +
                                (c.getTelefone() != null ? c.getTelefone() : "") + ";" +
                                (c.getEmail() != null ? c.getEmail() : "") +
                                "\n"
                );
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
