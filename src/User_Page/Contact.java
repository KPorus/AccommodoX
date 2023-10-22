package User_Page;

import com.sun.mail.util.MailSSLSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataSource;

public class Contact extends JFrame {

    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea bodyTextArea;

    public Contact() {
        setTitle("Email Sender App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 600, 400);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a panel for email input
        JPanel emailPanel = new JPanel(new GridLayout(4, 1));

        recipientField = new JTextField();
        subjectField = new JTextField();
        bodyTextArea = new JTextArea(10, 30);
        JButton sendButton = new JButton("Send Email");

        emailPanel.add(new JLabel("Recipient Email:"));
        emailPanel.add(recipientField);
        emailPanel.add(new JLabel("Subject:"));
        emailPanel.add(subjectField);
        emailPanel.add(new JLabel("Email Body:"));
        emailPanel.add(new JScrollPane(bodyTextArea));
        emailPanel.add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendEmail(
                        recipientField.getText(),
                        subjectField.getText(),
                        bodyTextArea.getText()
                );
            }
        });

        mainPanel.add(emailPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void sendEmail(String recipient, String subject, String body) {
        final String username = "mdfardinkhan1952@gmail.com"; // Your Gmail username
        final String password = "vuihbzbbvyfdrljp"; // Your Gmail password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

//        // Enable SSL/TLS for the connection (added this part)
//        try {
//            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//            sf.setTrustAllHosts(true);
//            props.put("mail.smtp.ssl.socketFactory", sf);
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//        
        Session session;
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            JOptionPane.showMessageDialog(this, "Email sent successfully!");
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "Error sending email: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Contact app = new Contact();
            app.setVisible(true);
        });
    }
}
