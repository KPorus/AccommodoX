package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.BookingWithUserInfo;
import User_data.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableColumnModel;

public class BookedRooms extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private List<BookingWithUserInfo> bookedRooms;
    private JButton backButton;

    public BookedRooms(User user, int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection());

        setTitle("Booked Rooms Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 20));
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        JButton Booked = new JButton("Booked Rooms");
        JButton rooms = new JButton("Rooms");
        rooms.addActionListener((ActionEvent e) -> {
            new rooms(userId).setVisible(true);
            dispose();
        });
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User userData = userDAO.getUser(userId); // Fetch user data by ID
                new receiptionist(userData).setVisible(true); // Pass the fetched user data
                dispose();
            }
        });

        menuPanel.add(profile);
        menuPanel.add(Booked);
        menuPanel.add(rooms);
        mainContentPanel.add(menuPanel, BorderLayout.WEST);
        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setOpaque(false);

        String[] columnNames = {"User Name", "Room Type", "Booking To", "Booking From", "Check-In Time", "Check-Out Time", "Number of Rooms", "Prize","Offer"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);
        bookingTable.setAutoCreateRowSorter(true);

        bookedRooms = userDAO.getAllBookingsWithUserInfo();// Implement this method to fetch data from your SQL query
        displayBookedRooms();

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        bookingPanel.add(scrollPane, BorderLayout.CENTER);

        mainContentPanel.add(bookingPanel, BorderLayout.CENTER);

        getContentPane().add(mainContentPanel);
    }

    private void displayBookedRooms() {
        tableModel.setRowCount(0);

        for (BookingWithUserInfo room : bookedRooms) {
            Object[] rowData = {
                room.getUserName(),
                room.getRoomType(),
                room.getBookingTo(),
                room.getBookingFrom(),
                room.getCheckInTime(),
                room.getCheckOutTime(),
                room.getNumberOfRooms(),
                room.getPrize(),
                room.getOffer()
            };
            tableModel.addRow(rowData);
        }

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            packColumn(bookingTable, i);
        }
    }

    private void packColumn(JTable table, int columnIndex) {
        int margin = 5;
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(columnIndex);
        int width = 0;

        TableCellRenderer headerRenderer = col.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = table.getTableHeader().getDefaultRenderer();
        }

        Component headerComp = headerRenderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        width = headerComp.getPreferredSize().width;

        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, columnIndex);
            Component comp = table.prepareRenderer(renderer, row, columnIndex);
            width = Math.max(comp.getPreferredSize().width + margin, width);
        }

        col.setPreferredWidth(width + margin);
    }

    public static void main(String[] args) {
        // This is just for testing; you can remove this part when integrating with your application.
        User user = new User(); // Replace with your User object
        int userId = 1; // Replace with the actual user ID
        JFrame frame = new BookedRooms(user, userId);
        frame.setVisible(true);
    }
}
