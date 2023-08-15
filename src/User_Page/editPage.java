package User_Page;

import DB.MySQLConnection;
import Design.GradientPanel;
import User_data.User;
import java.awt.BorderLayout;
import java.awt.Color;
import DB.UserDAO;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class editPage extends JFrame {

    private MySQLConnection mysqlConnection;
    private UserDAO userDAO;
    private User userData;

    public editPage(User user) {
        this.userData = user;
        mysqlConnection = new MySQLConnection(); // Initialize MySQLConnection
        userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("Edit profile page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 800, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");

        JButton users = new JButton("Customers");
        JButton emp = new JButton("Employees");

        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(emp);

        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        JPanel userInfoPanel = new JPanel(new GridLayout(6, 1, 0, 10)); // 3 rows, 1 column
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(new Color(255, 255, 255));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        JTextField nameLabel = new JTextField(user.getUsername()); // Replace with actual method to get name
        JTextField emailLabel = new JTextField(user.getEmail()); // Replace with actual method to get email
        JTextField passLabel = new JTextField(user.getPassword()); // Replace with actual method to get role

        JLabel address = new JLabel("Address");
        JTextField addressText = new JTextField("");
        JPanel addresspanel = new JPanel(new GridLayout(1, 1, 10, 10));
        addresspanel.add(address);
        addresspanel.add(addressText);
        addresspanel.setOpaque(false);

        JLabel phone = new JLabel("Phone");
        JTextField phoneText = new JTextField("");
        JPanel phonepanel = new JPanel(new GridLayout(1, 1, 10, 10));
        phonepanel.add(phone);
        phonepanel.add(phoneText);
        phonepanel.setOpaque(false);

        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Name = nameLabel.getText();
                String Email = emailLabel.getText();
                String Pass = passLabel.getText();
                String Address = addressText.getText();
                String Phone = phoneText.getText();
                int Id = user.getId();
                if (userDAO.updateUserAndDetails(Id, Name, Pass, Email, Address, Phone)) {
                    JOptionPane.showConfirmDialog(editPage.this, "User info updated", "Success Message", 0);
                } else {
                    JOptionPane.showMessageDialog(editPage.this, "Failed.");
                }
            }
        });
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Name = nameLabel.getText();
                String Email = emailLabel.getText();
                String Pass = passLabel.getText();
                int Id = user.getId();
                User initialUserData = new User(Id, Name, Email, Pass, user.getRole()); // Provide actual user data
                new admin(initialUserData).setVisible(true);
                dispose();
            }
        });
        userInfoPanel.add(nameLabel);
        userInfoPanel.add(emailLabel);
        userInfoPanel.add(passLabel);
        userInfoPanel.add(addresspanel);
        userInfoPanel.add(phonepanel);
        userInfoPanel.add(save);

        welcomePanel.add(userInfoPanel, BorderLayout.CENTER);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);
    }

    public static void main(String[] args) {
        new editPage().setVisible(true);
    }

    private editPage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
