package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.User;
import User_data.UserDetails;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Account extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private User userData;
    private UserDetails details;

    public Account(User user) {
        this.userData = user;
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        this.userData = userDAO.getUser(user.getId());

        setTitle("Account Page");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());
        // Create a panel for the logo, title, and menu buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Create a panel for the logo and title
        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoTitlePanel.setOpaque(false);

        // Add your logo using a JLabel
        ImageIcon logoIcon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        Image scaledImage = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(logoIcon);

        // Add the logo to the logoTitlePanel
        logoTitlePanel.add(logoLabel);

        JLabel title = new JLabel("AccommodoX");
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        logoTitlePanel.add(title);

// Create a panel for the menu buttons
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuPanel.setOpaque(false);

        int userId = user.getId();
        JButton profile = new JButton("Profile");
        profile.setForeground(Color.white);
        profile.setBackground(new Color(24, 63, 102));
        profile.setFocusPainted(false); // Disable focus border

        JButton account = new JButton("Account Info");
        account.setForeground(Color.white);
        account.setBackground(new Color(24, 63, 102));
        account.setFocusPainted(false); // Disable focus border

        menuPanel.add(profile);
        menuPanel.add(account);

        // Add the logoTitlePanel and menuPanel to the headerPanel
        headerPanel.add(logoTitlePanel, BorderLayout.WEST);
        headerPanel.add(menuPanel, BorderLayout.EAST);

        // Create a panel for user information
        JPanel userInfoPanel = new JPanel(new GridLayout(7, 2, 0, 5));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(Color.WHITE);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));

        details = userDAO.getUserDetails(userId);
        JLabel nameLabel = new JLabel("Name:");
        JLabel nameLabelValue = new JLabel(user.getUsername());

        JLabel emailLabel = new JLabel("Email:");
        JLabel emailLabelValue = new JLabel(user.getEmail());

        JLabel roleLabel = new JLabel("Role:");
        JLabel roleLabelValue = new JLabel(user.getRole());

        String nameLabelText = nameLabel.getText();
        String nameLabelValueText = nameLabelValue.getText();
        String UseName = nameLabelText + " " + nameLabelValueText;

        String emailLabelText = emailLabel.getText();
        String emailLabelValueText = emailLabelValue.getText();
        String UseEmail = emailLabelText + " " + emailLabelValueText;

        String roleLabelText = roleLabel.getText();
        String roleLabelValueText = roleLabelValue.getText();
        String UseRole = roleLabelText + " " + roleLabelValueText;

        // Create a label for the concatenated role information
        JLabel useNameLabel = new JLabel(UseName);
        useNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        useNameLabel.setForeground(Color.WHITE); // Set text color to white

        // Create a label for the concatenated role information
        JLabel useEmailLabel = new JLabel(UseEmail);
        useEmailLabel.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        useEmailLabel.setForeground(Color.WHITE); // Set text color to white

        // Create a label for the concatenated role information
        JLabel useRoleLabel = new JLabel(UseRole);
        useRoleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        useRoleLabel.setForeground(Color.WHITE); // Set text color to white

        // Add user information to the userInfoPanel
        userInfoPanel.add(useNameLabel);
        userInfoPanel.add(useEmailLabel);
        userInfoPanel.add(useRoleLabel); // Add the label displaying the role information

        // Create a panel for the logo and user information
        JPanel logoBodyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoBodyPanel.setOpaque(false);

        ImageIcon logoIconBody = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\Avater.png");
        Image scaledImageBody = logoIconBody.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        logoIconBody = new ImageIcon(scaledImageBody);
        JLabel logoBodyLabel = new JLabel(logoIconBody);

        // Add the logo to the logoBodyPanel
        logoBodyPanel.add(logoBodyLabel);

        if (details != null) {
            JLabel address = new JLabel("Address: " + details.getAddress());
            address.setForeground(Color.WHITE);
            address.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
            JLabel phone = new JLabel("Phone: " + details.getPhone());
            phone.setForeground(Color.WHITE);
            phone.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size

            JButton edit = new JButton("Edit");
            edit.setForeground(Color.white);
            edit.setBackground(new Color(24, 63, 102));
            edit.setFocusPainted(false); // Disable focus border
            
            edit.addActionListener((ActionEvent e) -> {
                editPage edit1 = new editPage(user);
                edit1.setVisible(true);
                dispose();
            });

            userInfoPanel.add(address);
            userInfoPanel.add(phone);
            userInfoPanel.add(edit);
        } else {
            JLabel errorLabel = new JLabel("User details not available");
            userInfoPanel.add(errorLabel);
        }

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);
        mainContentPanel.add(userInfoPanel, BorderLayout.CENTER);
        // Center the logoBodyPanel and userInfoPanel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        centerPanel.add(logoBodyPanel, gbc);
        centerPanel.add(mainContentPanel, gbc);

        // Add headerPanel and centerPanel to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\Icon.jpeg");
        return icon.getImage();
    }
}
