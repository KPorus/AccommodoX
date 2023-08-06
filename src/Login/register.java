package Login;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public register() {
        mysqlConnection = new MySQLConnection(); // Initialize MySQLConnection
        userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("Register Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 800, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Title Label
        JLabel title = new JLabel("Welcome to sweet Hotal");
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
        SignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String User = userName.getText();
                String Email = email.getText();
                String Pass = new String(pass.getPassword());
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                mysqlConnection.closeConnection(); // Close MySQL connection when window closes
            }
        });
    }
}

class MySQLConnection {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/users";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;

    public MySQLConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean registerUser(String username, String email, String password, String role) {
        String insertUserQuery = "INSERT INTO users (name, email, pass,role) VALUES (?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        String selectUserQuery = "SELECT * FROM users WHERE name = ? AND pass = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a matching user is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
