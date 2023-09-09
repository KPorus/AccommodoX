package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.User;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class allCustomer extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;

    public allCustomer(User user, int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("All customers page");
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
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assuming you have a method to fetch a specific user by ID from your database
                User userData = userDAO.getUser(userId); // Fetch user data by ID
                new admin(userData).setVisible(true); // Pass the fetched user data
                dispose();
            }
        });
        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(emp);

        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        JPanel userInfoPanel = new JPanel(new GridLayout(5, 1, 0, 10)); // 5 rows, 1 column
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(new Color(255, 255, 255));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        // Here you should fetch the list of customers from the database using userDAO
        // and then iterate over them to display their information
        // For example:
        List<User> customers = userDAO.getAllCustomers();
        for (User customer : customers) {
            JLabel nameLabel = new JLabel("Name: " + customer.getUsername());
            JLabel emailLabel = new JLabel("Email: " + customer.getEmail());
            JLabel roleLabel = new JLabel("Role: " + customer.getRole());

            userInfoPanel.add(nameLabel);
            userInfoPanel.add(emailLabel);
            userInfoPanel.add(roleLabel);
        }

        welcomePanel.add(userInfoPanel, BorderLayout.CENTER);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);
    }


}
