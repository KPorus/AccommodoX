package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.Booking;
import User_data.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SingleUserBooking extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable bookingTable;
    private DefaultTableModel tableModel;

    public SingleUserBooking(int userId) {
        mysqlConnection = new MySQLConnection(); // Initialize your MySQLConnection
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO using the connection

        setTitle("Booking Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 20)); // Increase the row count to accommodate the "Add Room" button
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        JButton BookedRoom = new JButton("Booked Room");
        JButton rooms = new JButton("Rooms");

        // Check the user's role
        User user = userDAO.getUser(userId);
        String role = user.getRole();
        if ("customer".equals(role)) {
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    User userData = userDAO.getUser(userId); // Fetch user data by ID
                    new customer(userData).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            rooms.addActionListener((ActionEvent e) -> {
                new rooms(userId).setVisible(true);
                dispose();
            });
            menuPanel.add(profile);
            menuPanel.add(rooms);
            menuPanel.add(BookedRoom);
            mainContentPanel.add(menuPanel, BorderLayout.WEST);

        }

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        // Retrieve booking data for the user
        List<Booking> bookings = userDAO.getAllRoomsOfUser(userId);

        // Create a table model for booking data
        String[] columnNames = {"Room ID", "Number of Rooms", "Price", "Booking From", "Booking To", "Check-In Time", "Check-Out Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookingTable = new JTable(tableModel);

        // Populate the table with booking data
        if (bookings != null && !bookings.isEmpty()) {
            for (Booking booking : bookings) {
                Object[] rowData = {
                        booking.getRoomId(),
                        booking.getNumberOfRooms(),
                        booking.getPrize(),
                        booking.getBookingFrom(),
                        booking.getBookingTo(),
                        booking.getCheckInTime(),
                        booking.getCheckOutTime()
                };
                tableModel.addRow(rowData);
            }
        } else {
            // Handle the case where no booking data is found for the user
            JOptionPane.showMessageDialog(this, "No booking data found for the user.");
        }

        JScrollPane tableScrollPane = new JScrollPane(bookingTable);
        welcomePanel.add(tableScrollPane, BorderLayout.CENTER);
        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        add(mainContentPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SingleUserBooking(1); // Replace '1' with the actual user ID
            frame.setVisible(true);
        });
    }
}
