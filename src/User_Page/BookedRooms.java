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
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private JButton backButton;

    public BookedRooms(User user, int userId) {
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

        JButton Booked = new JButton("Booked Rooms");
        Booked.setForeground(Color.white);
        Booked.setBackground(new Color(24, 63, 102));
        Booked.setFocusPainted(false); // Disable focus border

        JButton rooms = new JButton("Rooms");
        rooms.setForeground(Color.white);
        rooms.setBackground(new Color(24, 63, 102));
        rooms.setFocusPainted(false); // Disable focus border

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

        String[] columnNames = {"User Name", "Room Type", "Booking To", "Booking From", "Check-In Time", "Check-Out Time", "Number of Rooms", "Prize", "Offer"};
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

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    displayBookedRooms(currentPage);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int maxPage = (int) Math.ceil((double) bookedRooms.size() / pageSize);
                if (currentPage < maxPage) {
                    currentPage++;
                    displayBookedRooms(currentPage);
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

    private void displayBookedRooms(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, bookedRooms.size());

        for (int i = startIdx; i < endIdx; i++) {
            BookingWithUserInfo room = bookedRooms.get(i);
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

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
