package UserAuthantication;

import DB.UserDAO;
import Validation.isEmailValid;
import DB.MySQLConnection;
import Design.FocusListener;
import Design.GradientPanel;
import Validation.isPassValid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;

public class register extends JFrame {

    private MySQLConnection mysqlConnection;
    private UserDAO userDAO;
    private isEmailValid emailValidator;
    private isPassValid passValidator;

    public register() {
        mysqlConnection = new MySQLConnection();
        userDAO = new UserDAO(mysqlConnection.getConnection());
        emailValidator = new isEmailValid();
        passValidator = new isPassValid();

        setTitle("Register Page");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(true);

        setLayout(new BorderLayout());
        setContentPane(new GradientPanel());

        JPanel logoTitlePanel = new JPanel(new BorderLayout());
        logoTitlePanel.setOpaque(false);

        JLabel logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
            Image scaledImage = logoIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
            logo.setIcon(logoIcon);
        } catch (Exception ex) {
            System.err.println("Error loading image: " + ex.getMessage());
        }
        logoTitlePanel.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel("Welcome to AccommodoX");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SAN", Font.BOLD, 28));
        logoTitlePanel.add(title, BorderLayout.CENTER);

        add(logoTitlePanel, BorderLayout.NORTH);

        JTextField userName = new JTextField("Enter your user name");
        userName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        userName.setBackground(Color.WHITE);
        userName.addFocusListener(new FocusListener("Enter your user name"));

        JTextField email = new JTextField("Enter your Email");
        email.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        email.setBackground(new Color(255, 255, 255));
        email.addFocusListener(new FocusListener("Enter your Email"));

        JPasswordField pass = new JPasswordField("Enter your Password");
        pass.setEchoChar((char) 0);
        pass.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        pass.setBackground(new Color(255, 255, 255));
        pass.addFocusListener(new FocusListener("Enter your Password"));

        JTextPane lbu = new JTextPane();
        lbu.setContentType("text/html");
        lbu.setText("<html><body><span style='font-family: SansSerif; font-size: 10pt; color: white; background: transparent; border: none; cursor: pointer;'>Already created an account!!</span></body></html>");
        lbu.setEditable(false);
        lbu.setBackground(new Color(0, 0, 0, 0));

        lbu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new login().setVisible(true);
            }
        });

        JButton SignUp = new JButton("Sign Up");
        SignUp.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        SignUp.setBackground(new Color(14, 129, 152));
        SignUp.setForeground(Color.WHITE);
        SignUp.setFocusPainted(false);

        SignUp.addActionListener((ActionEvent e) -> {
            String User = userName.getText();
            String Email = email.getText();
            String Pass = new String(pass.getPassword());
            if (!passValidator.isValidPass(Pass) && !emailValidator.isValidEmail(Email)) {
                JOptionPane.showMessageDialog(register.this, "Invalid email & password format.");
            } else if (!emailValidator.isValidEmail(Email)) {
                JOptionPane.showMessageDialog(register.this, "Invalid email format.");
            } else if (!passValidator.isValidPass(Pass)) {
                JOptionPane.showMessageDialog(register.this, "Invalid password format.");
            } else if (userDAO.isUserExists(User)) {
                JOptionPane.showMessageDialog(register.this, "Username already exists.");
            } else if (userDAO.isEmailExists(Email)) {
                JOptionPane.showMessageDialog(register.this, "Email already exists.");
            } else {
                if (userDAO.registerUser(User, Email, Pass, "customer")) {
                    JOptionPane.showConfirmDialog(register.this, "Registration Successful", "Success Message", 0);
                } else {
                    JOptionPane.showMessageDialog(register.this, "User registration failed.");
                }
            }
        });

        SignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SignUp.setBackground(new Color(4, 94, 115));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SignUp.setBackground(new Color(14, 129, 152));
            }
        });

        JPanel registrationPanel = new JPanel(new GridLayout(6, 1, 15, 10));
        registrationPanel.setOpaque(false);
        registrationPanel.add(userName);
        registrationPanel.add(email);
        registrationPanel.add(pass);
        registrationPanel.add(lbu);
        registrationPanel.add(SignUp);

        JPanel titleAndRegistrationPanel = new JPanel();
        titleAndRegistrationPanel.setOpaque(false);
        titleAndRegistrationPanel.setLayout(new BoxLayout(titleAndRegistrationPanel, BoxLayout.Y_AXIS));
        titleAndRegistrationPanel.add(title);
        titleAndRegistrationPanel.add(Box.createVerticalStrut(20));
        titleAndRegistrationPanel.add(registrationPanel);

        add(titleAndRegistrationPanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mysqlConnection.closeConnection();
            }
        });
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\Icon.jpg");
        return icon.getImage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            register registration = new register();
            registration.setVisible(true);
        });
    }
}
