package UserAuthantication;

import DB.UserDAO;
import Validation.isEmailValid;
import DB.MySQLConnection;
import Design.FocusListener;
import Design.GradientPanel;
import Validation.isPassValid;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/**
 *
 * @author User
 */
public class register extends JFrame {

    private MySQLConnection mysqlConnection;
    private UserDAO userDAO;
    private isEmailValid emailValidator;
    private isPassValid passValidator;

    public register() {
        mysqlConnection = new MySQLConnection(); // Initialize MySQLConnection
        userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        emailValidator = new isEmailValid(); // Initialize the emailValidator instance
        passValidator = new isPassValid();// Initialize the passValidator instance
        
        
        setTitle("Register Page");
        setIconImage(getAppIcon()); // Set the application icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setLayout(new GridLayout(1, 2));
        setContentPane(new GradientPanel());

        // Add an image to the left side
        JLabel logo = new JLabel();
        ImageIcon logoIcon = new ImageIcon("Image/Icon.jpg"); // Replace "logo.png" with your image file
        logo.setIcon(logoIcon);

        // Title Label
        JLabel title = new JLabel("Welcome to AccommodoX");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SAN", Font.BOLD, 28));
        // User Name Input Field
        JTextField userName = new JTextField("Enter your user name");
        userName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        userName.setBackground(Color.WHITE);
        userName.addFocusListener(new FocusListener("Enter your user name"));

        // Email Input Field
        JTextField email = new JTextField("Enter your Email");
        email.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        email.setBackground(new Color(255, 255, 255));
        email.addFocusListener(new FocusListener("Enter your Email"));

        // Password Input Field
        JPasswordField pass = new JPasswordField("Enter your Password");
        pass.setEchoChar((char) 0);
        pass.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        pass.setBackground(new Color(255, 255, 255));
        pass.addFocusListener(new FocusListener("Enter your Password"));

        // Already Created Account Text
        JTextPane lbu = new JTextPane();
        lbu.setContentType("text/html"); // Set content type to HTML
        lbu.setText("<html><body><span style='font-family: SansSerif; font-size: 10pt; color: white; background: transparent; border: none; cursor: pointer;'>Already created an account!!</span></body></html>");
        lbu.setEditable(false);
        lbu.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        lbu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new login().setVisible(true);
            }
        });

        // Sign Up Button
        JButton SignUp = new JButton("Sign Up");
        SignUp.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        SignUp.setBackground(new Color(14, 129, 152));
        SignUp.setForeground(Color.WHITE); // Set text color to white
        SignUp.setFocusPainted(false); // Remove focus border
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
                // Proceed with registration
                if (userDAO.registerUser(User, Email, Pass, "customer")) {
                    JOptionPane.showConfirmDialog(register.this, "Registration Successful", "Success Message", 0b0);
                } else {
                    JOptionPane.showMessageDialog(register.this, "User registration failed.");
                }
            }
        });

        // Apply hover effect
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

        // Create the layout panel
        JPanel panel = new JPanel(new GridLayout(6, 1, 15, 10));
        panel.setOpaque(false);
        panel.add(title);
        panel.add(userName);
        panel.add(email);
        panel.add(pass);
        panel.add(lbu);
        panel.add(SignUp);

        getContentPane().add(panel);
         getContentPane().add(logo);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mysqlConnection.closeConnection(); // Close MySQL connection when window closes
            }
        });
        
        
    }
       private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("Icon.jpg"); // Replace "icon.png" with your application icon file
        return icon.getImage();
    }
}
