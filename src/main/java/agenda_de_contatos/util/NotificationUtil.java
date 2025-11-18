package agenda_de_contatos.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;

public class NotificationUtil {


    public static void showSuccessToast(Node ownerNode, String message) {
        Window owner = getWindowFromNode(ownerNode);
        Notifications.create()
                .title("Sucesso")
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .owner(owner)
                .showInformation();
    }


    public static void showErrorToast(Node ownerNode, String message) {
        Window owner = getWindowFromNode(ownerNode);
        Notifications.create()
                .title("Erro")
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .owner(owner)
                .showError();
    }


    public static void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private static Window getWindowFromNode(Node node) {
        if (node != null && node.getScene() != null) {
            return node.getScene().getWindow();
        }
        return null;
    }
}