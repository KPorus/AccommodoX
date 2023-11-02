package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.User;
import User_data.UserDetails;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class admin extends JFrame {

    private User userData;
    private UserDetails details;
    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;

    public admin(User user) {
        this.userData = user;
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        this.userData = userDAO.getUser(user.getId());

        setTitle("Admin Profile Page");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout()); // Use BorderLayout for the main frame

        // Create a panel for the menu options on the top
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        menuPanel.setOpaque(false);

        int userId = user.getId();
        JButton profile = new JButton("Profile");
        JButton users = new JButton("Customers");
        JButton rooms = new JButton("Rooms");
        JButton offer = new JButton("Offers");

        // Add action listeners to the buttons
        rooms.addActionListener((ActionEvent e) -> {
            new rooms(userId).setVisible(true);
            dispose();
        });

        users.addActionListener((ActionEvent e) -> {
            new allCustomer(user, userId).setVisible(true);
            dispose();
        });

        offer.addActionListener((ActionEvent e) -> {
            new Offers(userId).setVisible(true);
            dispose();
        });

        // Add buttons to the menu panel
        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(rooms);
        menuPanel.add(offer);

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
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Add a gap between the logo and title
        title.setFont(new Font("SansSerif", Font.BOLD, 24)); // Increase the font size
        // Add the title to the logoTitlePanel
        logoTitlePanel.add(title);

        // Create a label for the title
        JLabel titleLabel = new JLabel("Welcome " + user.getUsername());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Add a gap between the title and user info
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18)); // Increase the font size

        // Create a panel for user information
        JPanel userInfoPanel = new JPanel(new GridLayout(7, 2, 0, 5)); // Adjust the gap and columns
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(Color.WHITE); // Set text color to white
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

        // Create a panel for the logo and title
        JPanel logoBodyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Change alignment to RIGHT
        logoBodyPanel.setOpaque(false);

        // Add your logo using a JLabel
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
            edit.setOpaque(false);
            edit.addActionListener((ActionEvent e) -> {
                editPage edit1 = new editPage(user);
                edit1.setVisible(true);
                dispose();
            });

            userInfoPanel.add(address);
            userInfoPanel.add(phone);
            userInfoPanel.add(edit);
        }

        // Add headerPanel and userInfoPanel to the frame
        headerPanel.add(logoTitlePanel, BorderLayout.WEST);
        headerPanel.add(menuPanel, BorderLayout.CENTER);

        // Create a main content panel to hold user information
        JPanel mainContentPanel = new JPanel(new BorderLayout()); // Use BorderLayout
        mainContentPanel.setOpaque(false);

        // Center the logoBodyPanel and userInfoPanel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        centerPanel.add(logoBodyPanel, gbc);
        centerPanel.add(userInfoPanel, gbc);

        // Add headerPanel and mainContentPanel to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Example usage
        User user = new User(/* Initialize user data */);
        new admin(user).setVisible(true);
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }

//    // Method to create a rounded image with a shadow effect
//    private ImageIcon createRoundedImageWithShadow(ImageIcon originalImageIcon, int size, int shadowSize) {
//        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = image.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        // Create a shadow for the image
//        int x = (size - originalImageIcon.getIconWidth()) / 2 + shadowSize;
//        int y = (size - originalImageIcon.getIconHeight()) / 2 + shadowSize;
//        g2d.setColor(new Color(0, 0, 0, 100));
//        g2d.fillRoundRect(x, y, originalImageIcon.getIconWidth(), originalImageIcon.getIconHeight(), size, size);
//
//        // Draw the rounded image
//        g2d.setClip(new Ellipse2D.Double(x, y, originalImageIcon.getIconWidth(), originalImageIcon.getIconHeight()));
//        g2d.drawImage(originalImageIcon.getImage(), x, y, null);
//
//        g2d.dispose();
//        return new ImageIcon(image);
//    }
}
