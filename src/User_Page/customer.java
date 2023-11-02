package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.Offer;
import javax.swing.JFrame;
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
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class customer extends JFrame {

    private User userData;
    private UserDetails details;
    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;

    private JLabel offersLabel;
    private List<Offer> offers;
    private int currentOfferIndex = 0;

    public customer(User user) {
        this.userData = user;
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        this.userData = userDAO.getUser(user.getId());

        setTitle("Customer Profile Page");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 550);
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
        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        int userId = user.getId();
        JButton profile = new JButton("Profile");
        JButton rooms = new JButton("Rooms");
        JButton BookedRooms = new JButton("Booked Room");
        JButton contact = new JButton("Contact Us");
        contact.addActionListener(((e) -> {
            new Contact().setVisible(true);
        }));
        rooms.addActionListener((ActionEvent e) -> {
            new rooms(userId).setVisible(true);
            dispose();
        });
        BookedRooms.addActionListener((ActionEvent e) -> {
            new SingleUserBooking(userId).setVisible(true);
            dispose();
        });
        menuPanel.add(profile);
        menuPanel.add(rooms);
        menuPanel.add(BookedRooms);
        menuPanel.add(contact);
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
            edit.setOpaque(false);
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

        JPanel offersPanel = new JPanel();
        offersPanel.setOpaque(false);

        offersLabel = new JLabel();
        offersLabel.setForeground(Color.WHITE);
        offersLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        offersPanel.add(offersLabel);

        mainContentPanel.add(offersPanel, BorderLayout.SOUTH);
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

        // Create a timer to update the offers text
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextOffer();
            }
        });
        timer.start();
    }

    private void showNextOffer() {
        offers = userDAO.getAllOffers();

        // Filter offers with "open" status
        List<Offer> openOffers = offers.stream()
                .filter(offer -> "open".equalsIgnoreCase(offer.getStatus()))
                .collect(Collectors.toList());

        if (!openOffers.isEmpty()) {
            offersLabel.setText(openOffers.get(currentOfferIndex).getDescription());
            currentOfferIndex = (currentOfferIndex + 1) % openOffers.size();
        } else {
            // No open offers found, you can display a message or take other actions
            offersLabel.setText("No open offers available");
        }
    }

    public static void main(String[] args) {
        new customer().setVisible(true);
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }

    private customer() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
