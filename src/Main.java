

import UserAuthantication.register;
import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new register().setVisible(true));
    }
}

