

import UserAuthantication.register;
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

