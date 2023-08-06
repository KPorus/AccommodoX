package Login;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new register().setVisible(true));
    }
}


class GradientPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Define gradient colors
        Color startColor = new Color(14, 129, 152);
        Color endColor = new Color(14, 205, 205);

        // Create gradient paint
        GradientPaint gradientPaint = new GradientPaint(
                50, 0, startColor,
                getWidth(), getHeight(), endColor
        );

        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

class FocusListener implements java.awt.event.FocusListener {

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
