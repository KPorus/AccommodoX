package Login;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    public login() {
        mysqlConnection = new MySQLConnection(); // Initialize MySQLConnection
        userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 800, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Title Label
        JLabel title = new JLabel("Login Form");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        title.setForeground(new Color(255, 255, 255));

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
        login.setBackground(new Color(14, 129, 152));
        login.setForeground(Color.WHITE); // Set text color to white
        login.setFocusPainted(false); // Remove focus border
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String User = userName.getText();
                String Pass = new String(pass.getPassword());
                if (userDAO.loginUser(User, Pass)) {
                    JOptionPane.showConfirmDialog(login.this, "Login Successful", "Success Message", 0b0);

                    // Open the customer page
                    customer customerPage = new customer();
                    customerPage.setVisible(true);

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

        getContentPane().add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mysqlConnection.closeConnection(); // Close MySQL connection when window closes
            }
        });
    }

}

