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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;


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
                    // ALERTA DE CONFIRMAÇÃO
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Excluir Contato");
                    alert.setHeaderText("Tem certeza que deseja excluir este contato?");
                    alert.setContentText("Esta ação não poderá ser desfeita.");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        contatoService.excluirContato(contato);
                        NotificationUtil.showSuccessToast(
                                tableView,
                                "Contato excluído com sucesso!"
                        );
                        refreshTableData();

                    } else {
                        NotificationUtil.showSuccessToast(
                                tableView,
                                "Exclusão cancelada!"
                        );
                    }
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
}