package Design;

import java.awt.event.FocusEvent;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author User
 */
public class FocusListener implements java.awt.event.FocusListener {

    private final String placeholder;

    public FocusListener(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextComponent component) {
            if (component.getText().equals(placeholder)) {
                component.setText("");
                if (component instanceof JPasswordField passwordField) {
                    passwordField.setEchoChar('*');
                }
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JTextComponent component) {
            if (component.getText().isEmpty()) {
                component.setText(placeholder);
                if (component instanceof JPasswordField passwordField) {
                    passwordField.setEchoChar((char) 0);
                }
            }
        }
    }
}