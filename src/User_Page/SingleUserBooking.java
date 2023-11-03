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
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class SingleUserBooking extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable bookingTable;
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private DefaultTableModel tableModel;
    private List<Booking> bookings;

    public SingleUserBooking(int userId) {
        mysqlConnection = new MySQLConnection(); // Initialize your MySQLConnection
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO using the connection

        setTitle("Booking Information");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout()); // Use BorderLayout for the main frame

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options on the top
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        profile.setForeground(Color.white);
        profile.setBackground(new Color(24, 63, 102));
        profile.setFocusPainted(false); // Disable focus border 

        JButton BookedRoom = new JButton("Booked Rooms");
        BookedRoom.setForeground(Color.white);
        BookedRoom.setBackground(new Color(24, 63, 102));
        BookedRoom.setFocusPainted(false); // Disable focus border

        JButton rooms = new JButton("Rooms");
        rooms.setForeground(Color.white);
        rooms.setBackground(new Color(24, 63, 102));
        rooms.setFocusPainted(false); // Disable focus border

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
        }

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
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Add a gap between the logo and title
        title.setFont(new Font("SansSerif", Font.BOLD, 24)); // Increase the font size
        // Add the title to the logoTitlePanel
        logoTitlePanel.add(title);

        // Create a panel for the logo and title
        JPanel logoBodyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Change alignment to RIGHT
        logoBodyPanel.setOpaque(false);
        // Add headerPanel and userInfoPanel to the frame
        headerPanel.add(logoTitlePanel, BorderLayout.WEST);
        headerPanel.add(menuPanel, BorderLayout.CENTER);

        // Retrieve booking data for the user
        bookings = userDAO.getAllRoomsOfUser(userId);

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
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Enable horizontal scrolling
        tableScrollPane.setPreferredSize(new Dimension(900, 350)); // Set the preferred size for the scroll pane
        mainContentPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Add pagination controls
        JPanel paginationPanel = new JPanel();
        paginationPanel.setPreferredSize(new Dimension(500, 50)); // Set the preferred size for the pagination panel

        JButton prevButton = new JButton("Previous");
        prevButton.setForeground(Color.white);
        prevButton.setBackground(new Color(24, 63, 102));
        prevButton.setFocusPainted(false); // Disable focus border

        JButton nextButton = new JButton("Next");
        nextButton.setForeground(Color.white);
        nextButton.setBackground(new Color(24, 63, 102));
        nextButton.setFocusPainted(false); // Disable focus border

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    displayRooms(currentPage);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int maxPage = (int) Math.ceil((double) bookings.size() / pageSize);
                if (currentPage < maxPage) {
                    currentPage++;
                    displayRooms(currentPage);
                }
            }
        });
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        mainContentPanel.add(paginationPanel, BorderLayout.SOUTH);
        // Center the logoBodyPanel and userInfoPanel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        centerPanel.add(mainContentPanel, gbc);

        // Add headerPanel and mainContentPanel to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void displayRooms(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, bookings.size());

        for (int i = startIdx; i < endIdx; i++) {
            Booking booking = bookings.get(i);
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

        // Automatically adjust column widths based on content
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            packColumn(bookingTable, i, 150);
        }
    }

    private void packColumn(JTable table, int columnIndex, int margin) {
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(columnIndex);

        int width = 0;

        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }

        Component headerComp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        width = headerComp.getPreferredSize().width;

        for (int row = 0; row < table.getRowCount(); row++) {
            renderer = table.getCellRenderer(row, columnIndex);
            Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(row, columnIndex), false, false, row, columnIndex);
            width = Math.max(comp.getPreferredSize().width + margin, width);
        }

        col.setPreferredWidth(width);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SingleUserBooking(1); // Replace '1' with the actual user ID
            frame.setVisible(true);
        });
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
