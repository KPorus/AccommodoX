package UserAuthantication;

import DB.UserDAO;
import DB.MySQLConnection;
import User_Page.customer;
import Design.FocusListener;
import Design.GradientPanel;
import User_Page.Account;
import User_Page.admin;
import User_Page.receiptionist;
import User_data.UserInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class login extends JFrame {

    private MySQLConnection mysqlConnection;
    private UserDAO userDAO;
    private UserInfo info;

    public login() {
        mysqlConnection = new MySQLConnection(); // Initialize MySQLConnection
        userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        setTitle("Login Page");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        JPanel logoTitlePanel = new JPanel(new BorderLayout());
        logoTitlePanel.setOpaque(false);

        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\Login.jpeg");
            Image scaledImage = logoIcon.getImage().getScaledInstance(500, 500, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
            logo.setIcon(logoIcon);
        } catch (Exception ex) {
            System.err.println("Error loading image: " + ex.getMessage());
        }
        logoTitlePanel.add(logo, BorderLayout.WEST);

        // Title Label
        JLabel title = new JLabel("Login Form");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        title.setForeground(new Color(255, 255, 255));
        logoTitlePanel.add(title, BorderLayout.CENTER);

        add(logoTitlePanel, BorderLayout.NORTH);
        // User Name Input Field
        JTextField userName = new JTextField("Enter your user name");
        userName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        userName.setBackground(Color.WHITE);
        userName.addFocusListener(new FocusListener("Enter your user name"));

        // Password Input Field
        JPasswordField pass = new JPasswordField("Enter your Password");
        pass.setEchoChar((char) 0);
        pass.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        pass.setBackground(new Color(255, 255, 255));
        pass.addFocusListener(new FocusListener("Enter your Password"));

        // Create an Account Text
        JTextPane lbu = new JTextPane();
        lbu.setContentType("text/html"); // Set content type to HTML
        lbu.setText("<html><body><span style='font-family: SansSerif; font-size: 10pt; color: white; background: transparent; border: none; cursor: pointer;'>Create an account!!</span></body></html>");
        lbu.setEditable(false);
        lbu.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        lbu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new register().setVisible(true);
            }
        });

        // Login Button
        JButton login = new JButton("Login");
        login.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        login.setForeground(Color.white);
        login.setBackground(new Color(24, 63, 102));
        login.setFocusPainted(false); // Remove focus border
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String User = userName.getText();
                String Pass = new String(pass.getPassword());
                if (userDAO.loginUser(User, Pass)) {
                    JOptionPane.showMessageDialog(login.this, "Login Successful", "Success Message", JOptionPane.NO_OPTION);
                    info = userDAO.getUserInfo(User);
                    User_data.User user = userDAO.getUser(info.getId());
                    if ("customer".equals(info.getRole())) {
                        // Open the customer page
                        customer customerPage = new customer(user);
                        customerPage.setVisible(true);
                    }
                    if ("admin".equals(info.getRole())) {
                        admin adminPage = new admin(user);
                        adminPage.setVisible(true);

                    }
                    if ("receiptionist".equals(info.getRole())) {
                        receiptionist receiption = new receiptionist(user);
                        receiption.setVisible(true);
                    }
                    if ("accountent".equals(info.getRole())) {
                        Account accountent = new Account(user);
                        accountent.setVisible(true);
                    }

                    // Close the login page
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(login.this, "User login failed.");
                }
            }
        });

        // Apply hover effect
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                login.setBackground(new Color(4, 94, 115));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                login.setBackground(new Color(14, 129, 152));
            }
        });

        // Create the layout panel
        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 10));
        panel.setOpaque(false);
        panel.add(title);
        panel.add(userName);
        panel.add(pass);
        panel.add(lbu);
        panel.add(login);

        JPanel titleAndLoginPanel = new JPanel();
        titleAndLoginPanel.setOpaque(false);
        titleAndLoginPanel.setLayout(new BoxLayout(titleAndLoginPanel, BoxLayout.Y_AXIS));
        titleAndLoginPanel.add(title);
        titleAndLoginPanel.add(Box.createVerticalStrut(20));
        titleAndLoginPanel.add(panel);

        add(titleAndLoginPanel, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mysqlConnection.closeConnection(); // Close MySQL connection when window closes
            }
        });
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\Icon.jpeg");
        return icon.getImage();
    }

}
