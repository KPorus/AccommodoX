package User_Page;

import javax.swing.JFrame;
import Design.FocusListener;
import Design.GradientPanel;
import User_data.User; // Make sure to import the appropriate User class
import javax.swing.JLabel;

public class customer extends JFrame {
    private User userData;

    public customer(User user) {
        this.userData = user;
        
        setTitle("Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());
        
        JLabel title = new JLabel("Welcome " + user.getUsername());
        title.setBounds(50, 50, 300, 30); // Set the label's position and size
        add(title);

        // Here you can use the userData to customize the UI or perform other operations
    }
}
