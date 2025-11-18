package agenda_de_contatos.util;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class FormatterUtil {

    public static TextFormatter<String> createPhoneFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            String digits = newText.replaceAll("\\D", "");

            if (digits.length() > 11) {
                return null;
            }

            int len = digits.length();
            StringBuilder formatted = new StringBuilder();

            if (len > 0) {
                formatted.append("(").append(digits.substring(0, Math.min(2, len)));
            }
            if (len > 2) {
                int split = (len > 10) ? 7 : 6;
                formatted.append(") ").append(digits.substring(2, Math.min(split, len)));
            }
            if (len > 6 && len <= 10) {
                formatted.append("-").append(digits.substring(6));
            } else if (len > 7) {
                formatted.append("-").append(digits.substring(7));
            }

            change.setText(formatted.toString());
            change.setRange(0, change.getControlText().length());
            change.setCaretPosition(formatted.length());
            change.setAnchor(formatted.length());

            return change;
        };
        return new TextFormatter<>(filter);
    }
}