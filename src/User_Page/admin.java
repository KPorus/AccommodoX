package User_Page;

import Design.GradientPanel;
import javax.swing.JFrame;
import User_data.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class admin extends JFrame {
    private User userData;

    public admin(User user) {
        this.userData = user;
        setTitle("Admin Profile Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 800, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        JButton home = new JButton("Home");
        JButton users = new JButton("Customers");
        JButton emp = new JButton("Employees");

        menuPanel.add(profile);
        menuPanel.add(home);
        menuPanel.add(users);
        menuPanel.add(emp);

        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        JLabel title = new JLabel("Welcome " + user.getUsername()); // Using user.getUsername()
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0)); // Adjust the values as needed
        title.setForeground(new Color(255, 255, 255));

        welcomePanel.add(title, BorderLayout.NORTH);
        
        JPanel userInfoPanel = new JPanel(new GridLayout(3, 1, 0, 10)); // 3 rows, 1 column
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(new Color(255,255,255));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        JLabel nameLabel = new JLabel("Name: " + user.getUsername()); // Replace with actual method to get name
        JLabel emailLabel = new JLabel("Email: " + user.getEmail()); // Replace with actual method to get email
        JLabel roleLabel = new JLabel("Role: " + user.getRole()); // Replace with actual method to get role

        userInfoPanel.add(nameLabel);
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(roleLabel);

        welcomePanel.add(userInfoPanel, BorderLayout.CENTER);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);
    }

    public static void main(String[] args) {
        new admin().setVisible(true);
    }

    private admin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
