package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.BookingWithUserInfo;
import User_data.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class RoomAccount extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private final JTable bookingTable;
    private final DefaultTableModel tableModel;
    private List<BookingWithUserInfo> bookedRooms;
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private double totalAmountBkash = 0.0;
    private double totalAmountManual = 0.0;

    public RoomAccount(int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection());

        setTitle("Booked Rooms Page");
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

        // Create a panel for the logo, title, and menu buttons
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        profile.setForeground(Color.white);
        profile.setBackground(new Color(24, 63, 102));
        profile.setFocusPainted(false); // Disable focus border

        JButton Book = new JButton("Booked Rooms");
        Book.setForeground(Color.white);
        Book.setBackground(new Color(24, 63, 102));
        Book.setFocusPainted(false); // Disable focus border

        JButton account = new JButton("Account Info");
        account.setForeground(Color.white);
        account.setBackground(new Color(24, 63, 102));
        account.setFocusPainted(false); // Disable focus border

        profile.addActionListener((ActionEvent e) -> {
            User userData = userDAO.getUser(userId); // Fetch user data by ID
            new Account(userData).setVisible(true); // Pass the fetched user data
            dispose();
        });
        account.addActionListener((ActionEvent e) -> {
            new AllAccountInfo(userId).setVisible(true);
            dispose();
        });
        menuPanel.add(profile);
        menuPanel.add(Book);
        menuPanel.add(account);

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

        String[] columnNames = {"User Name", "Room Type", "Booking To", "Booking From", "Check-In Time", "Check-Out Time", "Prize", "Offer"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);
        bookingTable.setAutoCreateRowSorter(true);

        bookedRooms = userDAO.getAllBookingsWithUserInfo();// Implement this method to fetch data from your SQL query
        displayBookedRooms(currentPage);

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Enable horizontal scrolling
        scrollPane.setPreferredSize(new Dimension(900, 350)); // Set the preferred size for the scroll pane
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

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

        prevButton.addActionListener((ActionEvent e) -> {
            if (currentPage > 1) {
                currentPage--;
                displayBookedRooms(currentPage);
            }
        });

        nextButton.addActionListener((ActionEvent e) -> {
            int maxPage = (int) Math.ceil((double) bookedRooms.size() / pageSize);
            if (currentPage < maxPage) {
                currentPage++;
                displayBookedRooms(currentPage);
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

    private void displayBookedRooms(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, bookedRooms.size());

        totalAmountBkash = 0.0;
        totalAmountManual = 0.0;

        for (int i = startIdx; i < endIdx; i++) {
            BookingWithUserInfo room = bookedRooms.get(i);
            Object[] rowData = {
                room.getUserName(),
                room.getRoomType(),
                room.getBookingTo(),
                room.getBookingFrom(),
                room.getCheckInTime(),
                room.getCheckOutTime(),
                room.getPrize(),
                room.getOffer(),
                room.getPayment(),};
            tableModel.addRow(rowData);

            if ("bkash".equals(room.getPayment())) {
                totalAmountBkash += room.getPrize();
            } else if ("manual".equals(room.getPayment())) {
                totalAmountManual += room.getPrize();
            }
        }

        // Add a row for "Total Amount (bkash)"
        Object[] totalAmountBkashRow = {
            "Total Amount (bkash)",
            "",
            "",
            "",
            "",
            "",
            totalAmountBkash,
            "",
            "",};
        tableModel.addRow(totalAmountBkashRow);

        // Add a row for "Total Amount (manual)"
        Object[] totalAmountManualRow = {
            "Total Amount (manual)",
            "",
            "",
            "",
            "",
            "",
            totalAmountManual,
            "",
            "",};
        tableModel.addRow(totalAmountManualRow);

        packColumns(bookingTable);
    }

    private void packColumns(JTable table) {
        int margin = 5;
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            TableColumn col = colModel.getColumn(i);
            TableCellRenderer headerRenderer = col.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }

            Component headerComp = headerRenderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            int width = headerComp.getPreferredSize().width;

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, i);
                Component comp = table.prepareRenderer(renderer, row, i);
                width = Math.max(comp.getPreferredSize().width + margin, width);
            }

            col.setPreferredWidth(width + margin);
        }
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }

}
