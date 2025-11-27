package agenda_de_contatos;

import agenda_de_contatos.service.ConfigService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private static ConfigService configService = new ConfigService();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 940, 460);

        scene.getStylesheets().add(MainApplication.class.getResource("dark-theme.css").toExternalForm());
        applySavedThemeStyles(root);

        stage.setTitle("Gerenciador de Contatos");
        stage.setScene(scene);
        stage.show();
    }

    public static void applySavedThemeStyles(Parent root) {
        String bg = configService.getBackgroundColor();
        String font = configService.getFontColor();
        String button = configService.getButtonColor();
        String sidebar = configService.getSidebarColor();
        String border = configService.getBorderColor();

        String style = String.format(
                // Cores de Fundo
                "-background-base: %s; " +
                        "-sidebar-background: %s; " +

                        // Cores de Botão e Foco
                        "-button-color: %s; " +
                        "-input-focus-border-color: %s; " +

                        // Cores de Fonte (para todos os componentes)
                        "-text-color: %s; " +
                        "-sidebar-text-color: %s; " +
                        "-button-text-color: %s; " +
                        "-table-header-text-color: %s; " +
                        "-table-cell-text-color: %s; " +
                        "-input-text-color: %s;",


                bg,       // 1. -background-base
                sidebar,  // 2. -sidebar-background
                button,   // 3. -button-color
                border,   // 4. -input-focus-border-color
                font,     // 5. -text-color
                font,     // 6. -sidebar-text-color
                font,     // 7. -button-text-color
                font,     // 8. -table-header-text-color
                font,     // 9. -table-cell-text-color
                font      // 10. -input-text-color

        );

        root.setStyle(style);
    }

    public static ConfigService getConfigService() {
        return configService;
    }
}