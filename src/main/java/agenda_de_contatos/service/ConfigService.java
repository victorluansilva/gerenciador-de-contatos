package agenda_de_contatos.service;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConfigService {

    private final String CONFIG_FILE = "config.properties";
    private Properties props = new Properties();

    public static final String COLOR_WHITE = "#FFFFFF";
    public static final String COLOR_BLACK = "#333333";

    //COLOR TEXT
    public static final String COLOR_BLUE_TEXT = "#4A89F3";
    public static final String COLOR_PURPLE_TEXT = "#8A2BE2";
    public static final String COLOR_MAGENTA_TEXT = "#FF00FF";
    public static final String COLOR_GREEN_TEXT = "#2CFF05";
    public static final String COLOR_NUBANK_TEXT = "#D3D3D3";
    public static final String COLOR_PINK_TEXT = "#FFC0CB";
    public static final String COLOR_TXT_RED = "#FF0000";
    public static final String COLOR_TXT_BROWN= "#895129";
    public static final String COLOR_CIANO_TEXT = "#00FFFF";
    public static final String COLOR_BLACK_TEXT = "000000";
    public static final String COLOR_YELLOW_TEXT = "#E6E600";
    public static final String COLOR_DRAK_TEXT = "#2C3E50";
    public static final String COLO_LIGHT_TEXT = "#FFFFFF";
    public static final String COLO_DARK_TEXT = "#3C3F41";
    public static final String COLO_BLUE_TEXT = "#4A89F3";
    public static final String COLO_ORANGE_TEXT = "#f0ad4e";
    public static final String COLO_BLUEDARK_TEXT = "#554af3";

    //COLOR BTN
    public static final String COLOR_DEFAULT_BLUE_BUTTON = "#4A89F3";
    public static final String COLOR_ORANGE_BUTTON = "#f0ad4e";
    public static final String COLOR_GREEN_BUTTON = "#2CFF05";
    public static final String COLOR_NUBANK_BUTTON = "#D3D3D3";
    public static final String COLOR_PURPLE_BUTTON = "#8A2BE2";
    public static final String COLOR_MAGENTA_BUTTON = "#FF00FF";
    public static final String COLOR_BTN_BROWN = "#895129";
    public static final String COLOR_YELLOW_BUTTON = "#E6E600";
    public static final String COLOR_BLACK_BUTTON = "000000";
    public static final String COLOR_CIANO_BUTTON = "#00FFFF";
    public static final String COLOR_DRAK_BUTTON = "#2C3E50";
    public static final String COLOR_LIGHT_BUTTON = "#FFFFFF";
    public static final String COLOR_DARK_BUTTON = "#3C3F41";
    public static final String COLOR_BLUEDARK_BUTTON = "#554af3";
    public static final String COLOR_BTN_RED = "#FF0000";
    public static final String COLOR_BTN_PINK = "#FFC0CB";


    //COLOR BG
    public static final String COLOR_BACKGROND_BROWN = "#895129";
    public static final String COLOR_BACKGROUND_RED = "FF0000";
    public static final String COLOR_BG_PURPLE = "#8A2BE2";
    public static final String COLOR_BG_MAGENTA = "#FF00FF";
    public static final String COLOR_BG_YELLOW = "#E6E600";
    public static final String COLOR_BG_NUBANK = "#D3D3D3";
    public static final String COLOR_BG_LIGHT = "#FFFFFF";
    public static final String COLOR_BG_DARK = "#3C3F41";
    public static final String COLOR_BG_BLUE = "#4A89F3";
    public static final String COLOR_BG_GREEN = "#2CFF05";
    public static final String COLOR_BG_BLACK = "000000";
    //public static final String COLOR_BG_ROXONUBANK= "#820AD1";
    public static final String COLOR_BG_CIANO = "#00FFFF";
    public static final String COLOR_BG_DRAK = "#2C3E50";
    public static final String COLOR_BG_BLUEDARK = "#554af3";
    public static final String COLOR_BG_PINK = "#FFC0CB";
    public static final String COLOR_BG_ORANGE = "#f0ad4e";

    //COLOR SIDEBAR
    public static final String COLOR_SIDEBAR_DEFAULT = "#34495E";
    public static final String COLOR_SIDEBAR_PURPLE = "#8A2BE2";
    public static final String COLOR_SIDEBAR_MAGENTA = "#FF00FF";
    public static final String COLOR_SIDEBAR_RED = "FF0000";
    public static final String COLOR_SIDEBAR_BROWN = "#895129";
    public static final String COLOR_SIDEBAR_CIANO = "#00FFFF";
    public static final String COLOR_SIDEBAR_BLUEDARK = "#554af3";
    public static final String COLOR_SIDEBAR_LIGHT = "#FFFFFF";
    public static final String COLOR_SIDEBAR_DARK = "#3C3E50";
    public static final String COLOR_SIDEBAR_BLACK = "#000000";
    //public static final String COLOR_SIDEBAR_ROXONUBANK = "#820AD1";
    public static final String COLOR_SIDEBAR_DRAK = "#2C3E50";
    public static final String COLOR_SIDEBAR_BLUE =  "#4A89F3";
    public static final String COLOR_SIDEBAR_YELLOW = "#E6E600";
    public static final String COLOR_SIDEBAR_GREEN = "#2CFF05";
    public static final String COLOR_SIDEBAR_NUBANK = "#D3D3D3";
    public static final String COLOR_SIDEBAR_PINK = "#FFC0CB";
    public static final String COLOR_SIDEBAR_ORANGE = "#f0ad4e";



    public static final String COLOR_TEXT_BG_ROXONUBANK= "#820AD1";
    public static final String COLOR_TEXT_BG_ROXONUBANK1= "#820AD1";

    public ConfigService() {
        loadConfig();
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            props.setProperty("storage.type", "sqlite");
            props.setProperty("mysql.url", "jdbc:mysql://localhost:3306/db_agenda_de_contatos");
            props.setProperty("mysql.user", "root");
            props.setProperty("mysql.pass", "");
            props.setProperty("ui.theme.background", COLOR_BG_DARK);
            props.setProperty("ui.theme.font", COLOR_WHITE);
            props.setProperty("ui.theme.button", COLOR_DEFAULT_BLUE_BUTTON);
            props.setProperty("ui.theme.sidebar", COLOR_SIDEBAR_DEFAULT);
            saveConfig();
        }
    }

    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Configurações da Agenda de Contatos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStorageType() { return props.getProperty("storage.type", "sqlite"); }
    public void setStorageType(String type) { props.setProperty("storage.type", type); }
    public String getMysqlUrl() { return props.getProperty("mysql.url"); }
    public void setMysqlUrl(String url) { props.setProperty("mysql.url", url); }
    public String getMysqlUser() { return props.getProperty("mysql.user"); }
    public void setMysqlUser(String user) { props.setProperty("mysql.user", user); }
    public String getMysqlPass() { return props.getProperty("mysql.pass"); }
    public void setMysqlPass(String pass) { props.setProperty("mysql.pass", pass); }


    private String getColorProperty(String key, String defaultValue) {
        String value = props.getProperty(key);

        // Se não existir ou estiver vazio, usa o padrão
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        // Corrige o caso em que o Properties salvou "\#"
        if (value.startsWith("\\#")) {
            value = value.substring(1);
        }

        value = value.trim();

        // Se já é só um HEX puro, ok
        if (value.matches("^#[0-9a-fA-F]{3,8}$")) {
            return value;
        }

        // Se o valor é um CSS gigante, tenta extrair a primeira cor "#XXXXXX"
        Matcher m = Pattern.compile("#[0-9a-fA-F]{3,8}").matcher(value);
        if (m.find()) {
            return m.group();
        }

        // Último fallback
        return defaultValue;
    }

    public String getBackgroundColor() {
        return getColorProperty("ui.theme.background", COLOR_BG_DARK);
    }
    public void setBackgroundColor(String colorHex) {
        props.setProperty("ui.theme.background", colorHex);
        saveConfig();
    }

    public String getFontColor() {
        return getColorProperty("ui.theme.font", COLOR_WHITE);
    }
    public void setFontColor(String colorHex) {
        props.setProperty("ui.theme.font", colorHex);
        saveConfig();
    }

    public String getButtonColor() {
        return getColorProperty("ui.theme.button", COLOR_DEFAULT_BLUE_BUTTON);
    }
    public void setButtonColor(String colorHex) {
        props.setProperty("ui.theme.button", colorHex);
        saveConfig();
    }

    public String getSidebarColor() {
        return getColorProperty("ui.theme.sidebar", COLOR_SIDEBAR_DEFAULT);
    }
    public void setSidebarColor(String colorHex) {
        props.setProperty("ui.theme.sidebar", colorHex);
        saveConfig();
    }


}