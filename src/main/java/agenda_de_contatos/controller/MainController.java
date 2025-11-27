package agenda_de_contatos.controller;

import agenda_de_contatos.MainApplication;
import agenda_de_contatos.model.Contato;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.application.Platform;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private BorderPane mainPane;

    @FXML
    public void initialize() {
        handleShowContatoList();
    }

    public BorderPane getMainPane() {
        return mainPane;
    }

    @FXML
    private void handleShowContatoList() {
        loadView("contato-list-view.fxml", null);
    }

    @FXML
    private void handleShowContatoForm() {
        loadView("contato-form-view.fxml", null);
    }

    @FXML
    private void handleShowConfig() {
        loadView("config-view.fxml", null);
    }

    @FXML
    private void handleSair() {
        Platform.exit();
    }

    @FXML
    private void handleImportarContato(){
        loadView("import-contato-view.fxml", null);
    }

    public void showEditForm(Contato contato) {
        loadView("contato-form-view.fxml", contato);
    }

    private void loadView(String fxmlFile, Contato contato) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
            Node view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof ContatoListController) {
                ((ContatoListController) controller).setMainController(this);
            } else if (controller instanceof ContatoFormController) {
                ((ContatoFormController) controller).setMainController(this);
                ((ContatoFormController) controller).setContato(contato);
            } else if (controller instanceof ConfigController) {
                ((ConfigController) controller).setMainController(this);
            }

            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}