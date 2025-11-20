package agenda_de_contatos.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConfigService {

    public static final String COLOR_BLACK_TEXT = "000000";
    public static final String COLOR_BLACK_BUTTON = "000000";
    public static final String COLOR_BG_BLACK = "000000";
    private final String CONFIG_FILE = "config.properties";
    private Properties props = new Properties();

    public static final String COLOR_WHITE = "#FFFFFF";
    public static final String COLOR_BLACK = "#333333";
    public static final String COLOR_BLUE_TEXT = "#ECF0F1";
    public static final String COLOR_PINK_TEXT = "#FFC0CB";
    public static final String COLOR_DEFAULT_BLUE_BUTTON = "#4A89F3";
    public static final String COLOR_ORANGE_BUTTON = "#f0ad4e";
    public static final String COLOR_PINK_BUTTON = "#FFC0CB";
    public static final String COLOR_BG_LIGHT = "#F4F4F4";
    public static final String COLOR_BG_DARK = "#3C3F41";
    public static final String COLOR_BG_BLUE = "#2C3E50";
    public static final String COLOR_BG_PINK = "#FFC0CB";
    public static final String COLOR_SIDEBAR_DEFAULT = "#34495E";

    public static final String COLOR_CIANO_TEXT = "#00FFFF";
    public static final String COLOR_BG_CIANO = "#00FFFF";
    public static final String COLOR_SIDEBAR_CIANO = "#00FFFF";
    public static final String COLOR_CIANO_BUTTON = "#00FFFF";


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