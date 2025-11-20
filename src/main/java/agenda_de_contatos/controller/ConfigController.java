package agenda_de_contatos.controller;

import agenda_de_contatos.MainApplication;
import agenda_de_contatos.service.ConfigService;
import agenda_de_contatos.service.MysqlRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

public class ConfigController {
//iniciar botoes//
    @FXML private ChoiceBox<String> storageChoiceBox;
    @FXML private TextField hostField;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button testButton;
    @FXML private Label hostLabel;
    @FXML private Label userLabel;
    @FXML private Label passLabel;
    @FXML private Label storageStatusLabel;

    @FXML private Button btnFontWhite;
    @FXML private Button btnFontBlack;
    @FXML private Button btnFontBlue;
    @FXML private Button btnFontPurple;
    @FXML private Button btnFontMagenta;

    @FXML private Button btnFontYellow;
    @FXML private Button btnButtonWhite;
    @FXML private Button btnButtonBlue;
    @FXML private Button btnButtonOrange;
    @FXML private Button btnButtonYellow;
    @FXML private Button btnBgLight;
    @FXML private Button btnBgDark;
    @FXML private Button btnBgBlue;
    @FXML private Button btnBgYellow;
    @FXML private Button btnFontBrown;
    @FXML private Button btnButtonWhite;
    @FXML private Button btnButtonBlue;
    @FXML private Button btnButtonOrange;
    @FXML private Button btnButtonBrown;
    @FXML private Button btnBgLight;
    @FXML private Button btnBgDark;
    @FXML private Button btnBgBlue;
    @FXML private Button btnTxtBrown;


    @FXML private Button btnFontGreen;
    @FXML private Button btnButtonWhite;
    @FXML private Button btnButtonBlue;
    @FXML private Button btnButtonOrange;
    @FXML private Button btnButtonGreen;
    @FXML private Button btnBgLight;
    @FXML private Button btnBgDark;
    @FXML private Button btnBgBlue;
    @FXML private Button btnBgGreen;
    @FXML private Button btnFontNubank;
    @FXML private Button btnButtonWhite;
    @FXML private Button btnButtonBlue;
    @FXML private Button btnButtonOrange;
    @FXML private Button btnButtonNubank;
    @FXML private Button btnBgLight;
    @FXML private Button btnBgDark;
    @FXML private Button btnBgBlue;
    @FXML private Button btnTxtRed;
    @FXML private Button btnButtonRed;
    @FXML private Button btnBgRed;

    @FXML private Button btnBgNubank;


    @FXML private Button btnFontPink;
    @FXML private Button btnButtonWhite;
    @FXML private Button btnButtonBlue;
    @FXML private Button btnButtonOrange;
    @FXML private Button btnButtonPurple;
    @FXML private Button btnButtonMagenta;

    @FXML private Button btnBgLight;
    @FXML private Button btnBgDark;
    @FXML private Button btnBgBlue;
    @FXML private Button btnBgPurple;
    @FXML private Button btnBgMagenta;
    @FXML private Button btnBgPink;

    @FXML private Button btnBgBlack;
    @FXML private Button btnFontBlack2;
    @FXML private Button btnButtonBlack2;
    @FXML private Button btnSidebarBlack;

    @FXML private Button btnFontCian;
    @FXML private Button btnBgCian;
    @FXML private Button btnSidebarCian;
    @FXML private Button btnButtonCian;

    @FXML private Button btnSidebarLight;
    @FXML private Button btnSidebarDark;
    @FXML private Button btnSidebarBlue;
    @FXML private Button btnSidebarPurple;
    @FXML private Button btnSidebarMagenta;
    @FXML private Button btnSidebarYellow;
    @FXML private Button btnSidebarRed;
    @FXML private Button btnSidebarGreen;
    @FXML private Button btnSidebarPink;



    @FXML private Label configStatusLabel;
    @FXML private TabPane tabPane;

    private MainController mainController;
    private ConfigService configService;

    private Map<String, Button> fontButtons = new HashMap<>();
    private Map<String, Button> buttonButtons = new HashMap<>();
    private Map<String, Button> backgroundButtons = new HashMap<>();

    private Map<String, Button> sidebarButtons = new HashMap<>();


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        configService = MainApplication.getConfigService();
        storageChoiceBox.setItems(FXCollections.observableArrayList("SQLite (Local)", "MySQL (Rede)"));
        loadStorageConfigData();
        storageChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            toggleMysqlFields(newVal.equals("MySQL (Rede)"));
        });

        groupThemeButtons();
        loadAndApplyCurrentThemeSelection();
    }

    private void loadStorageConfigData() {
        String storageType = configService.getStorageType();
        storageChoiceBox.setValue(storageType.equals("mysql") ? "MySQL (Rede)" : "SQLite (Local)");
        hostField.setText(configService.getMysqlUrl());
        userField.setText(configService.getMysqlUser());
        passField.setText(configService.getMysqlPass());
        toggleMysqlFields(storageType.equals("mysql"));
        storageStatusLabel.setText("");
    }

    private void toggleMysqlFields(boolean enable) {
        hostLabel.setVisible(enable); hostField.setVisible(enable);
        userLabel.setVisible(enable); userField.setVisible(enable);
        passLabel.setVisible(enable); passField.setVisible(enable);
        testButton.setVisible(enable);
    }

    @FXML
    private void handleSalvarStorage() {
        String storageType = storageChoiceBox.getValue().equals("MySQL (Rede)") ? "mysql" : "sqlite";
        configService.setStorageType(storageType);
        configService.setMysqlUrl(hostField.getText());
        configService.setMysqlUser(userField.getText());
        configService.setMysqlPass(passField.getText());
        configService.saveConfig();

        storageStatusLabel.setText("Configurações salvas! Reinicie o app para aplicar.");
        storageStatusLabel.setStyle("-fx-text-fill: green;");
    }

    @FXML
    private void handleTestar() {
        ConfigService testConfig = new ConfigService();
        testConfig.setMysqlUrl(hostField.getText());
        testConfig.setMysqlUser(userField.getText());
        testConfig.setMysqlPass(passField.getText());

        MysqlRepository testRepo = new MysqlRepository(testConfig);
        if (testRepo.testarConexao()) {
            storageStatusLabel.setText("Conexão MySQL bem-sucedida!");
            storageStatusLabel.setStyle("-fx-text-fill: green");
        } else {
            storageStatusLabel.setText("Falha na conexão MySQL.");
            storageStatusLabel.setStyle("-fx-text-fill: red");
        }
    }
// MÉTODOS DE TEMA

    private void groupThemeButtons() {

        fontButtons.put(ConfigService.COLOR_WHITE, btnFontWhite);
        fontButtons.put(ConfigService.COLOR_BLACK, btnFontBlack);
        fontButtons.put(ConfigService.COLOR_BLUE_TEXT, btnFontBlue);
        fontButtons.put(ConfigService.COLOR_PURPLE_TEXT, btnFontPurple);
        fontButtons.put(ConfigService.COLOR_MAGENTA_TEXT, btnFontMagenta);
        fontButtons.put(ConfigService.COLOR_YELLOW_TEXT, btnFontYellow);

        fontButtons.put(ConfigService.COLOR_TXT_RED, btnTxtRed);
        fontButtons.put(ConfigService.COLOR_TXT_BROWN, btnTxtBrown);
        fontButtons.put(ConfigService.COLOR_GREEN_TEXT, btnFontGreen);
        fontButtons.put(ConfigService.COLOR_NUBANK_TEXT, btnFontNubank);

        fontButtons.put(ConfigService.COLOR_BLACK_TEXT, btnFontBlack2);
        fontButtons.put(ConfigService.COLOR_PINK_TEXT, btnFontPink);
        fontButtons.put(ConfigService.COLOR_CIANO_TEXT, btnFontCian);

        buttonButtons.put(ConfigService.COLOR_WHITE, btnButtonWhite);
        buttonButtons.put(ConfigService.COLOR_DEFAULT_BLUE_BUTTON, btnButtonBlue);
        buttonButtons.put(ConfigService.COLOR_ORANGE_BUTTON, btnButtonOrange);
        buttonButtons.put(ConfigService.COLOR_PURPLE_BUTTON, btnButtonPurple);
        buttonButtons.put(ConfigService.COLOR_MAGENTA_BUTTON, btnButtonMagenta);
        buttonButtons.put(ConfigService.COLOR_YELLOW_BUTTON, btnButtonYellow);
        buttonButtons.put(ConfigService.COLOR_BTN_RED, btnButtonRed);

        backgroundButtons.put(ConfigService.COLOR_BG_LIGHT, btnBgLight);
        backgroundButtons.put(ConfigService.COLOR_BG_DARK, btnBgDark);
        backgroundButtons.put(ConfigService.COLOR_BG_BLUE, btnBgBlue);
        backgroundButtons.put(ConfigService.COLOR_BG_PURPLE, btnBgPurple);
        backgroundButtons.put(ConfigService.COLOR_BG_MAGENTA, btnBgMagenta);
        backgroundButtons.put(ConfigService.COLOR_BG_YELLOW, btnBgYellow);
        backgroundButtons.put(ConfigService.COLOR_BACKGROUND_RED, btnBgRed);
        buttonButtons.put(ConfigService.COLOR_BTN_BROWN, btnButtonBrown);
        buttonButtons.put(ConfigService.COLOR_GREEN_BUTTON, btnButtonGreen);
        buttonButtons.put(ConfigService.COLOR_NUBANK_BUTTON, btnButtonNubank);
        buttonButtons.put(ConfigService.COLOR_BLACK_BUTTON, btnButtonBlack2);
        buttonButtons.put(ConfigService.COLOR_PINK_BUTTON, btnButtonPink);
        buttonButtons.put(ConfigService.COLOR_CIANO_BUTTON, btnButtonCian);

//
        backgroundButtons.put(ConfigService.COLOR_BG_LIGHT, btnBgLight);
        backgroundButtons.put(ConfigService.COLOR_BG_DARK, btnBgDark);
        backgroundButtons.put(ConfigService.COLOR_BG_BLUE, btnBgBlue);
        backgroundButtons.put(ConfigService.COLOR_BG_PURPLE, btnBgPurple);
        backgroundButtons.put(ConfigService.COLOR_BG_MAGENTA, btnBgMagenta);
        backgroundButtons.put(ConfigService.COLOR_BG_GREEN, btnBgGreen);
        backgroundButtons.put(ConfigService.COLOR_BG_NUBANK, btnBgNubank);
        backgroundButtons.put(ConfigService.COLOR_BG_BLACK, btnBgBlack);
        backgroundButtons.put(ConfigService.COLOR_BG_PINK, btnBgPink);
        backgroundButtons.put(ConfigService.COLOR_BG_CIANO, btnBgCian);


        sidebarButtons.put(ConfigService.COLOR_BG_LIGHT, btnSidebarLight);
        sidebarButtons.put(ConfigService.COLOR_BG_DARK, btnSidebarDark);
        sidebarButtons.put(ConfigService.COLOR_BG_BLUE, btnSidebarBlue);
        sidebarButtons.put(ConfigService.COLOR_BG_PURPLE, btnSidebarPurple);
        sidebarButtons.put(ConfigService.COLOR_BG_MAGENTA, btnSidebarMagenta);
        sidebarButtons.put(ConfigService.COLOR_BG_YELLOW, btnSidebarYellow);
        sidebarButtons.put(ConfigService.COLOR_SIDEBAR_RED, btnSidebarRed);

        sidebarButtons.put(ConfigService.COLOR_BG_GREEN, btnSidebarGreen);
        sidebarButtons.put(ConfigService.COLOR_BG_BLACK, btnSidebarBlack);
        sidebarButtons.put(ConfigService.COLOR_BG_PINK, btnSidebarPink);
        sidebarButtons.put(ConfigService.COLOR_SIDEBAR_CIANO, btnSidebarCian);
    }

    private void loadAndApplyCurrentThemeSelection() {
        updateButtonSelection(fontButtons, configService.getFontColor());
        updateButtonSelection(buttonButtons, configService.getButtonColor());
        updateButtonSelection(backgroundButtons, configService.getBackgroundColor());
        updateButtonSelection(sidebarButtons, configService.getSidebarColor());
    }

    @FXML
    private void handleThemeChange(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String userData = (String) clickedButton.getUserData();

        if (userData == null || !userData.contains(":")) return;

        String[] parts = userData.split(":");
        String type = parts[0];
        String colorValue = parts[1];

        Map<String, Button> currentGroup = null;

        switch (type) {
            case "font":
                configService.setFontColor(colorValue);
                currentGroup = fontButtons;
                break;

            case "button":
                configService.setButtonColor(colorValue);
                currentGroup = buttonButtons;
                break;

            case "background":
                configService.setBackgroundColor(colorValue);
                currentGroup = backgroundButtons;
                break;

            case "sidebar":
                configService.setSidebarColor(colorValue);
                currentGroup = sidebarButtons;
                break;

            default:
                return;
        }

        updateButtonSelection(currentGroup, colorValue);
        applyStyleChange(null, null);

    }
    private void updateButtonSelection(Map<String, Button> buttonGroup, String selectedValue) {
        if (buttonGroup == null) return;

        String selectedClass = "selected-theme-button";

        buttonGroup.forEach((value, button) -> {
            boolean isSelected = value.equalsIgnoreCase(selectedValue);

            if (isSelected) {
                if (!button.getStyleClass().contains(selectedClass)) {
                    button.getStyleClass().add(selectedClass);
                }
            } else {
                button.getStyleClass().remove(selectedClass);
            }
            button.setDisable(isSelected);
        });
    }


    private void applyStyleChange(String variableName, String value) {
        if (mainController != null &&
                mainController.getMainPane() != null &&
                mainController.getMainPane().getScene() != null) {

            Parent root = mainController.getMainPane().getScene().getRoot();
            if (root != null) {
                MainApplication.applySavedThemeStyles(root);
            }
        }
    }
}