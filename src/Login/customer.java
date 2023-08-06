package Login;

import javax.swing.JFrame;

class customer extends JFrame {

    public customer() {
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 800, 500);
        setResizable(false);
        setContentPane(new GradientPanel());
    }
}
