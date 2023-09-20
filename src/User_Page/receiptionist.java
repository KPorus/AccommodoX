
package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.User;
import User_data.UserDetails;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class receiptionist extends JFrame {

    private User userData;
    private UserDetails details;
    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;

    public receiptionist(User user) {
        this.userData = user;
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        this.userData = userDAO.getUser(user.getId());

        setTitle("Welcome receiptionist" + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 20));
        menuPanel.setOpaque(false);

        int userId = user.getId();
        JButton profile = new JButton("Profile");
        JButton users = new JButton("Booked Rooms");
        JButton rooms = new JButton("Rooms");
        rooms.addActionListener((ActionEvent e) -> {
            new rooms(userId).setVisible(true);
            dispose();
        });
        users.addActionListener((ActionEvent e) -> {
            new BookedRooms(user, userId).setVisible(true);
            dispose();
        });
        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(rooms);
        
        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        JLabel title = new JLabel("Welcome " + user.getUsername()); // Using user.getUsername()
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0)); // Adjust the values as needed
        title.setForeground(new Color(255, 255, 255));

        welcomePanel.add(title, BorderLayout.NORTH);

        JPanel userInfoPanel = new JPanel(new GridLayout(5, 1, 0, 10)); // 3 rows, 1 column
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(new Color(255, 255, 255));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        details = userDAO.getUserDetails(userId);
        JLabel nameLabel = new JLabel("Name: " + user.getUsername()); // Replace with actual method to get name
        JLabel emailLabel = new JLabel("Email: " + user.getEmail()); // Replace with actual method to get email
        JLabel roleLabel = new JLabel("Role: " + user.getRole()); // Replace with actual method to get role
        if (details != null || details == null) {

            JLabel address = new JLabel("Address: " + details.getAddress()); // Replace with actual method to get role
            JLabel phone = new JLabel("phone: " + details.getPhone()); // Replace with actual method to get role

            JButton edit = new JButton("Edit");
            edit.addActionListener((ActionEvent e) -> {
                editPage edit1 = new editPage(user);
                edit1.setVisible(true);
                dispose();
            });

            userInfoPanel.add(nameLabel);
            userInfoPanel.add(emailLabel);
            userInfoPanel.add(roleLabel);
            userInfoPanel.add(address);
            userInfoPanel.add(phone);
            userInfoPanel.add(edit);
        } else if (details == null) {
            details = userDAO.getUserDetails(userId);
            JLabel address = new JLabel("Address: " + details.getAddress()); // Replace with actual method to get role
            JLabel phone = new JLabel("phone: " + details.getPhone()); // Replace with actual method to get role

            JButton edit = new JButton("Edit");
            edit.addActionListener((ActionEvent e) -> {
                editPage edit1 = new editPage(user);
                edit1.setVisible(true);
                dispose();
            });

            userInfoPanel.add(nameLabel);
            userInfoPanel.add(emailLabel);
            userInfoPanel.add(roleLabel);
            userInfoPanel.add(address);
            userInfoPanel.add(phone);
            userInfoPanel.add(edit);
        } else {
            JLabel errorLabel = new JLabel("User details not available");
            userInfoPanel.add(errorLabel);
        }
        welcomePanel.add(userInfoPanel, BorderLayout.CENTER);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);
    }

    public static void main(String[] args) {
        new receiptionist().setVisible(true);
    }

    private receiptionist() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
