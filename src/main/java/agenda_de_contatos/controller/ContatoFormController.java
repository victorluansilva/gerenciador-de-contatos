package agenda_de_contatos.controller;


import agenda_de_contatos.model.Contato;
import agenda_de_contatos.service.ContatoService;
import agenda_de_contatos.util.FormatterUtil;
import agenda_de_contatos.util.NotificationUtil;
import agenda_de_contatos.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.Validator;


public class ContatoFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private DatePicker dataNascPicker;
    @FXML private TextField emailField;
    @FXML private TextField enderecoField;

    private Contato contato;
    private ContatoService contatoService;
    private MainController mainController;

    private final Validator validator = new Validator();

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        contatoService = new ContatoService();
        telefoneField.setTextFormatter(FormatterUtil.createPhoneFormatter());
    }

    public void setContato(Contato contato) {
        this.contato = contato;
        if (contato != null) {
            titleLabel.setText("Editar Contato");
            nomeField.setText(contato.getNome());
            telefoneField.setText(contato.getTelefone());
            if (contato.getDataNascimento() != null) {
                dataNascPicker.setValue(contato.getDataNascimento());
            }
            emailField.setText(contato.getEmail());
            enderecoField.setText(contato.getEndereco());
        } else {
            titleLabel.setText("Adicionar Novo Contato");
        }

        setupValidation();
    }

    private void setupValidation() {
        int currentId = (contato != null) ? contato.getId() : -1;

        validator.createCheck()
                .dependsOn("nome", nomeField.textProperty())
                .withMethod(c -> {
                    if (c.get("nome").toString().trim().isEmpty()) {
                        c.error("Nome é obrigatório.");
                    }
                })
                .decorates(nomeField)
                .immediate();

        Check telefoneCheck = validator.createCheck();
        telefoneCheck.dependsOn("telefone", telefoneField.textProperty())
                .withMethod(c -> {
                    String telefone = c.get("telefone").toString();
                    if (telefone.trim().isEmpty()) {
                        c.error("Telefone é obrigatório.");
                    } else if (!telefone.matches(ValidationUtil.PHONE_REGEX)) {
                        c.error("Formato inválido. Use (DD) XXXXX-XXXX");
                    } else if (!contatoService.isTelefoneUnico(telefone, currentId)) {
                        c.error("Este telefone já está cadastrado.");
                    }
                })
                .decorates(telefoneField)
                .immediate();

        validator.createCheck()
                .dependsOn("email", emailField.textProperty())
                .withMethod(c -> {
                    String email = c.get("email").toString().trim();
                    if (!email.isEmpty() && !email.matches(ValidationUtil.EMAIL_REGEX)) {
                        c.error("Formato de e-mail inválido.");
                    }
                })
                .decorates(emailField)
                .immediate();
    }

    @FXML
    private void handleSalvar() {
        if (!validator.validate()) {
            NotificationUtil.showErrorToast(nomeField, "Formulário inválido. Verifique os campos em vermelho.");
            return;
        }

        boolean isNew = (contato == null);
        if (isNew) {
            contato = new Contato();
        }
        contato.setNome(nomeField.getText().trim());
        contato.setTelefone(telefoneField.getText());
        if (dataNascPicker.getValue() != null) {
            contato.setDataNascimento(dataNascPicker.getValue());
        }
        contato.setEmail(emailField.getText().trim());
        contato.setEndereco(enderecoField.getText().trim());

        if (isNew) {
            contatoService.adicionarContato(contato);
            NotificationUtil.showSuccessToast(nomeField, "Contato adicionado com sucesso!");
        } else {
            contatoService.atualizarContato(contato);
            NotificationUtil.showSuccessToast(nomeField, "Contato atualizado com sucesso!");
        }

        if (mainController != null) {
            mainController.initialize();
        }
    }

}