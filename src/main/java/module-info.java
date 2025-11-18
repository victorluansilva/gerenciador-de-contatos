module agenda_de_contatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    requires org.xerial.sqlitejdbc;
    requires org.slf4j.simple;


    opens agenda_de_contatos to javafx.fxml;
    opens agenda_de_contatos.controller to javafx.fxml;
    opens agenda_de_contatos.model to javafx.base;
    opens agenda_de_contatos.util to javafx.fxml;

    exports agenda_de_contatos;
}