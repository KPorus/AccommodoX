package User_Page;

import Design.GradientPanel;
import javax.swing.JFrame;
import User_data.User;
import javax.swing.JLabel;

public class admin extends JFrame {
        private User userData;
    public admin(User user) {
        this.userData = user;
        setTitle("Admin Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 800, 500);
        setResizable(false);
        setContentPane(new GradientPanel());
        
        JLabel title = new JLabel("Welcome " + user.getUsername());
        title.setBounds(50, 50, 300, 30); // Set the label's position and size
        add(title);
    }
}
