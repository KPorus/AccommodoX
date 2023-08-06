/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Design;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author User
 */
    public class GradientPanel extends JPanel {

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

