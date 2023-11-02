package User_Page;

import DB.MySQLConnection;
import Design.GradientPanel;
import User_data.User;
import User_data.UserDetails;
import java.awt.BorderLayout;
import java.awt.Color;
import DB.UserDAO;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
    private UserDetails details;

    public editPage(User user) {
        this.userData = user;
        mysqlConnection = new MySQLConnection(); // Initialize MySQLConnection
        userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        int userId = user.getId();
        String role = user.getRole();
        setTitle("Edit profile page");
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

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuPanel.setOpaque(false);

        details = userDAO.getUserDetails(userId);
        JPanel userInfoPanel = new JPanel(new GridLayout(6, 1, 0, 10)); // 3 rows, 1 column
        userInfoPanel.setOpaque(false);
        userInfoPanel.setForeground(new Color(255, 255, 255));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        
        JLabel name = new JLabel("Name");
        name.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        name.setForeground(Color.WHITE); // Set text color to white
        JTextField nameLabel = new JTextField(user.getUsername()); // Replace with actual method to get name
        JPanel namepanel = new JPanel(new GridLayout(1, 1, 0, 0));
        namepanel.add(name);
        namepanel.add(nameLabel);
        namepanel.setOpaque(false);
        
        JLabel email = new JLabel("Email");
        email.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        email.setForeground(Color.WHITE); // Set text color to white
        JTextField emailLabel = new JTextField(user.getEmail()); // Replace with actual method to get email
        JPanel emailpanel = new JPanel(new GridLayout(1, 1, 1, 1));
        emailpanel.add(email);
        emailpanel.add(emailLabel);
        emailpanel.setOpaque(false);
        
        JLabel pass = new JLabel("Password");
        pass.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        pass.setForeground(Color.WHITE); // Set text color to white
        JTextField passLabel = new JTextField(user.getPassword()); // Replace with actual method to get role
        JPanel passpanel = new JPanel(new GridLayout(1, 1, 1, 1));
        passpanel.add(pass);
        passpanel.add(passLabel);
        passpanel.setOpaque(false);

        JLabel address = new JLabel("Address");
        address.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        address.setForeground(Color.WHITE); // Set text color to white
        JTextField addressText = new JTextField(details.getAddress());
        JPanel addresspanel = new JPanel(new GridLayout(1, 1, 1, 1));
        addresspanel.add(address);
        addresspanel.add(addressText);
        addresspanel.setOpaque(false);

        JLabel phone = new JLabel("Phone");
        phone.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        phone.setForeground(Color.WHITE); // Set text color to white
        JTextField phoneText = new JTextField(details.getPhone());
        JPanel phonepanel = new JPanel(new GridLayout(1, 1, 1, 1));
        phonepanel.add(phone);
        phonepanel.add(phoneText);
        phonepanel.setOpaque(false);

        JButton save = new JButton("Save");
        save.setFont(new Font("SansSerif", Font.PLAIN, 16)); // Increase the font size
        save.setForeground(Color.white);
        save.setBackground(new Color(24, 63, 102));
        save.setFocusPainted(false); // Disable focus border
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
                    JOptionPane.showMessageDialog(editPage.this, "User info updated", "Success Message", 1);
                } else {
                    JOptionPane.showMessageDialog(editPage.this, "Failed.");
                }
            }
        });

        JButton profile = new JButton("Profile");
        profile.setForeground(Color.white);
        profile.setBackground(new Color(24, 63, 102));
        profile.setFocusPainted(false); // Disable focus border
        
        JButton users = new JButton("Customers");
        users.setForeground(Color.white);
        users.setBackground(new Color(24, 63, 102));
        users.setFocusPainted(false); // Disable focus border
        
        JButton emp = new JButton("Employees");
        emp.setForeground(Color.white);
        emp.setBackground(new Color(24, 63, 102));
        emp.setFocusPainted(false); // Disable focus border
        
        JButton BookedRoom = new JButton("Booked Room");
        BookedRoom.setForeground(Color.white);
        BookedRoom.setBackground(new Color(24, 63, 102));
        BookedRoom.setFocusPainted(false); // Disable focus border
        
        JButton rooms = new JButton("Rooms");
        rooms.setForeground(Color.white);
        rooms.setBackground(new Color(24, 63, 102));
        rooms.setFocusPainted(false); // Disable focus border
        
        JButton offer = new JButton("Offers");
        offer.setForeground(Color.white);
        offer.setBackground(new Color(24, 63, 102));
        offer.setFocusPainted(false); // Disable focus border
        
        if ("admin".equals(role)) {
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
            users.addActionListener((ActionEvent e) -> {
                new allCustomer(user, userId).setVisible(true);
                dispose();
            });
            emp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new employee(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            rooms.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new rooms(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            offer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Offers(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            menuPanel.add(profile);
            menuPanel.add(users);
            menuPanel.add(emp);
            menuPanel.add(rooms);
            menuPanel.add(offer);

        }

        if ("customer".equals(role)) {
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Name = nameLabel.getText();
                    String Email = emailLabel.getText();
                    String Pass = passLabel.getText();
                    int Id = user.getId();
                    User initialUserData = new User(Id, Name, Email, Pass, user.getRole()); // Provide actual user data
                    new customer(initialUserData).setVisible(true);
                    dispose();
                }
            });
            rooms.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new rooms(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            BookedRoom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new SingleUserBooking(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            menuPanel.add(profile);
            menuPanel.add(rooms);
            menuPanel.add(BookedRoom);

        }

        if ("accountent".equals(role)) {
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Name = nameLabel.getText();
                    String Email = emailLabel.getText();
                    String Pass = passLabel.getText();
                    int Id = user.getId();
                    User initialUserData = new User(Id, Name, Email, Pass, user.getRole()); // Provide actual user data
                    new Account(initialUserData).setVisible(true);
                    dispose();
                }
            });
            menuPanel.add(profile);
        }
        if ("receiptionist".equals(role)) {
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String Name = nameLabel.getText();
                    String Email = emailLabel.getText();
                    String Pass = passLabel.getText();
                    int Id = user.getId();
                    User initialUserData = new User(Id, Name, Email, Pass, user.getRole()); // Provide actual user data
                    new receiptionist(initialUserData).setVisible(true);
                    dispose();
                }
            });
            rooms.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new rooms(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            BookedRoom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new BookedRooms(null,userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            menuPanel.add(profile);
            menuPanel.add(rooms);
            menuPanel.add(BookedRoom);
        }
        headerPanel.add(logoTitlePanel, BorderLayout.WEST);
        headerPanel.add(menuPanel, BorderLayout.EAST);

        userInfoPanel.add(namepanel);
        userInfoPanel.add(emailpanel);
        userInfoPanel.add(passpanel);
        userInfoPanel.add(addresspanel);
        userInfoPanel.add(phonepanel);
        userInfoPanel.add(save);

        mainContentPanel.add(userInfoPanel, BorderLayout.CENTER);
        // Create a panel for the logo and user information
        JPanel logoBodyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoBodyPanel.setOpaque(false);

        ImageIcon logoIconBody = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\Avater.png");
        Image scaledImageBody = logoIconBody.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        logoIconBody = new ImageIcon(scaledImageBody);
        JLabel logoBodyLabel = new JLabel(logoIconBody);

        // Add the logo to the logoBodyPanel
        logoBodyPanel.add(logoBodyLabel);
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

    public static void main(String[] args) {
        new editPage().setVisible(true);
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }

    private editPage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
