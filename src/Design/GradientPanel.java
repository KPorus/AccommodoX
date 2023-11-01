package Design;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Define gradient colors
        Color startColor = new Color(24, 63, 102);  // #183F66
        Color endColor = new Color(147, 206, 233); // #93CEE9

        // Create gradient paint
        GradientPaint gradientPaint = new GradientPaint(
                50, 0, startColor,
                getWidth(), getHeight(), endColor
        );

        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
