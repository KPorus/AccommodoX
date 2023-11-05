package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.Rooms;
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

public class rooms extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable roomTable;
    private DefaultTableModel tableModel; // Initialize the tableModel
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private List<Rooms> allRooms;

    public rooms(int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("All Rooms");
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

        JButton customers = new JButton("Customers");
        customers.setForeground(Color.white);
        customers.setBackground(new Color(24, 63, 102));
        customers.setFocusPainted(false); // Disable focus border

        JButton BookedRoom = new JButton("Booked Room");
        BookedRoom.setForeground(Color.white);
        BookedRoom.setBackground(new Color(24, 63, 102));
        BookedRoom.setFocusPainted(false); // Disable focus border

        JButton employees = new JButton("Employees");
        employees.setForeground(Color.white);
        employees.setBackground(new Color(24, 63, 102));
        employees.setFocusPainted(false); // Disable focus border

        JButton rooms = new JButton("Rooms");
        rooms.setForeground(Color.white);
        rooms.setBackground(new Color(24, 63, 102));
        rooms.setFocusPainted(false); // Disable focus border

        JButton offer = new JButton("Offers");
        offer.setForeground(Color.white);
        offer.setBackground(new Color(24, 63, 102));
        offer.setFocusPainted(false); // Disable focus border
        // Check the user's role
        User user = userDAO.getUser(userId);
        String role = user.getRole();
        if ("admin".equals(role)) {
            JButton account = new JButton("Account Info");
            account.setForeground(Color.white);
            account.setBackground(new Color(24, 63, 102));
            account.setFocusPainted(false); // Disable focus border
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    User userData = userDAO.getUser(userId); // Fetch user data by ID
                    new admin(userData).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            customers.addActionListener((ActionEvent e) -> {
                new allCustomer(null, userId).setVisible(true);
                dispose();
            });
            rooms.addActionListener((ActionEvent e) -> {
                new rooms(userId).setVisible(true);
                dispose();
            });
            employees.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new employee(userId).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            offer.addActionListener((ActionEvent e) -> {
                new Offers(userId).setVisible(true);
                dispose();
            });
            account.addActionListener((ActionEvent e) -> {
                new AllAccountInfo(userId).setVisible(true);
                dispose();
            });
            menuPanel.add(profile);
            menuPanel.add(customers);
            menuPanel.add(employees);
            menuPanel.add(rooms);
            menuPanel.add(account);
            menuPanel.add(offer);
            headerPanel.add(menuPanel, BorderLayout.CENTER);
        }

        if ("customer".equals(role)) {
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    User userData = userDAO.getUser(userId); // Fetch user data by ID
                    new customer(userData).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            BookedRoom.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new SingleUserBooking(userId).setVisible(true); // Pass the fetched user data
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
            headerPanel.add(menuPanel, BorderLayout.CENTER);
        }

        if ("receiptionist".equals(role)) {
            profile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    User userData = userDAO.getUser(userId); // Fetch user data by ID
                    new receiptionist(userData).setVisible(true); // Pass the fetched user data
                    dispose();
                }
            });
            BookedRoom.addActionListener((ActionEvent e) -> {
                new BookedRooms(null, userId).setVisible(true);
                dispose();
            });
            rooms.addActionListener((ActionEvent e) -> {
                new rooms(userId).setVisible(true);
                dispose();
            });
            menuPanel.add(profile);
            menuPanel.add(BookedRoom);
            menuPanel.add(rooms);
            headerPanel.add(menuPanel, BorderLayout.CENTER);
        }

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

        // Initialize the table model with columns
        String[] columnNames = null;
        if ("admin".equals(role)) {
            columnNames = new String[]{"Room Type", "Prize", "Available Rooms", "Booked Rooms", "Free Breakfast", "Parking", "Flowers", "Free WiFi", "Private Bus"};
        }
        if ("customer".equals(role)) {
            columnNames = new String[]{"Room Type", "Offer", "Prize", "Free Breakfast", "Parking", "Flowers", "Free WiFi", "Private Bus"};
        }
        if ("receiptionist".equals(role)) {
            columnNames = new String[]{"Room Type", "Available Rooms", "Free Breakfast", "Parking", "Flowers", "Free WiFi", "Private Bus"};
        }
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        roomTable = new JTable(tableModel);
        roomTable.setAutoCreateRowSorter(true); // Allow sorting by columns

        // Fetch the list of rooms from the database using userDAO
        allRooms = userDAO.getAllRooms();
        displayRooms(currentPage, role);

        JScrollPane scrollPane = new JScrollPane(roomTable);
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
                    displayRooms(currentPage, role);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int maxPage = (int) Math.ceil((double) allRooms.size() / pageSize);
                if (currentPage < maxPage) {
                    currentPage++;
                    displayRooms(currentPage, role);
                }
            }
        });

        if ("customer".equals(role)) {
            // Create an "Booking room" button
            JButton roomBookButton = new JButton("Room Book");
            roomBookButton.setForeground(Color.white);
            roomBookButton.setBackground(new Color(24, 63, 102));
            roomBookButton.setFocusPainted(false); // Disable focus border
            paginationPanel.add(roomBookButton);

            roomBookButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = roomTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        Rooms selectedRoom = allRooms.get(selectedRow);
                        // Open a dialog to add a new employee
                        BookingDialog dialog = new BookingDialog(rooms.this, selectedRoom, userId);
                        dialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(rooms.this, "Please select an room to booking.");
                    }

                }
            });
        }

        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        mainContentPanel.add(paginationPanel, BorderLayout.SOUTH);

        // Add an "Update Rooms" button for selected rows
        JButton updateRoomsButton = new JButton("Update Room");
        updateRoomsButton.setForeground(Color.white);
        updateRoomsButton.setBackground(new Color(24, 63, 102));
        updateRoomsButton.setFocusPainted(false); // Disable focus border

        if ("admin".equals(role)) {
            paginationPanel.add(updateRoomsButton);

            updateRoomsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = roomTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Get the selected room
                        Rooms selectedRoom = allRooms.get(selectedRow);

                        // Open a dialog to update the selected room
                        UpdateRoomDialog dialog = new UpdateRoomDialog(rooms.this, selectedRoom, role);
                        dialog.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(rooms.this, "Please select a room to update.");
                    }
                }
            });
        }
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

    private void displayRooms(int page, String role) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, allRooms.size());

        for (int i = startIdx; i < endIdx; i++) {
            Rooms room = allRooms.get(i);
            Object[] rowData;

            if ("admin".equals(role)) {
                rowData = new Object[]{
                    room.getRoomType(),
                    room.getPrizePerDay(),
                    room.getAvailableRooms(),
                    room.getBookedRooms(),
                    room.isFreeBreakfast() ? "Yes" : "No",
                    room.isParking() ? "Yes" : "No",
                    room.isFlowers() ? "Yes" : "No",
                    room.isFreeWifi() ? "Yes" : "No",
                    room.isPrivateBus() ? "Yes" : "No"
                };
            } else if ("customer".equals(role)) {
                rowData = new Object[]{
                    room.getRoomType(),
                    room.getOffer() + "%",
                    room.getPrizePerDay(),
                    room.isFreeBreakfast() ? "Yes" : "No",
                    room.isParking() ? "Yes" : "No",
                    room.isFlowers() ? "Yes" : "No",
                    room.isFreeWifi() ? "Yes" : "No",
                    room.isPrivateBus() ? "Yes" : "No"
                };
            } else if ("receiptionist".equals(role)) {
                rowData = new Object[]{
                    room.getRoomType(),
                    room.getAvailableRooms(),
                    room.isFreeBreakfast() ? "Yes" : "No",
                    room.isParking() ? "Yes" : "No",
                    room.isFlowers() ? "Yes" : "No",
                    room.isFreeWifi() ? "Yes" : "No",
                    room.isPrivateBus() ? "Yes" : "No"
                };
            } else {
                // Handle other roles or scenarios here
                rowData = new Object[]{};
            }

            tableModel.addRow(rowData);
        }

        // Automatically adjust column widths based on content
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            packColumn(roomTable, i, 150);
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

    // Inner class for the "Update Room" dialog
    private class UpdateRoomDialog extends JDialog {

        private JTextField typeField;
        private JTextField availableRoomsField;
        private JTextField bookedRoomsField;
        private JCheckBox freeBreakfastCheckBox;
        private JCheckBox parkingCheckBox;
        private JCheckBox flowersCheckBox;
        private JCheckBox freeWifiCheckBox;
        private JCheckBox privateBusCheckBox;
//        private JButton updateButton;

        public UpdateRoomDialog(JFrame parent, Rooms room, String role) {
            super(parent, "Update Room", true);

            // Initialize dialog components
            JPanel panel = new JPanel(new GridLayout(11, 2));
            panel.setPreferredSize(new Dimension(400, 400));

            panel.add(new JLabel("Room Type:"));
            typeField = new JTextField(room.getRoomType());
            panel.add(typeField);

            panel.add(new JLabel("Available Rooms:"));
            availableRoomsField = new JTextField(Integer.toString(room.getAvailableRooms()));
            panel.add(availableRoomsField);

            panel.add(new JLabel("Booked Rooms:"));
            bookedRoomsField = new JTextField(Integer.toString(room.getBookedRooms()));
            panel.add(bookedRoomsField);

            panel.add(new JLabel("Free Breakfast:"));
            freeBreakfastCheckBox = new JCheckBox();
            freeBreakfastCheckBox.setSelected(room.isFreeBreakfast());
            panel.add(freeBreakfastCheckBox);

            panel.add(new JLabel("Parking:"));
            parkingCheckBox = new JCheckBox();
            parkingCheckBox.setSelected(room.isParking());
            panel.add(parkingCheckBox);

            panel.add(new JLabel("Flowers:"));
            flowersCheckBox = new JCheckBox();
            flowersCheckBox.setSelected(room.isFlowers());
            panel.add(flowersCheckBox);

            panel.add(new JLabel("Free WiFi:"));
            freeWifiCheckBox = new JCheckBox();
            freeWifiCheckBox.setSelected(room.isFreeWifi());
            panel.add(freeWifiCheckBox);

            panel.add(new JLabel("Private Bus:"));
            privateBusCheckBox = new JCheckBox();
            privateBusCheckBox.setSelected(room.isPrivateBus());
            panel.add(privateBusCheckBox);
            
            JButton updateButton = new JButton("Update");
            updateButton.setForeground(Color.white);
            updateButton.setBackground(new Color(24, 63, 102));
            updateButton.setFocusPainted(false); // Disable focus border
            panel.add(updateButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setForeground(Color.white);
            cancelButton.setBackground(new Color(24, 63, 102));
            cancelButton.setFocusPainted(false); // Disable focus border
            panel.add(cancelButton);

            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the updated room information from the dialog
                    String updatedRoomType = typeField.getText();
                    int updatedAvailableRooms = Integer.parseInt(availableRoomsField.getText());
                    int updatedBookedRooms = Integer.parseInt(bookedRoomsField.getText());
                    boolean updatedFreeBreakfast = freeBreakfastCheckBox.isSelected();
                    boolean updatedParking = parkingCheckBox.isSelected();
                    boolean updatedFlowers = flowersCheckBox.isSelected();
                    boolean updatedFreeWifi = freeWifiCheckBox.isSelected();
                    boolean updatedPrivateBus = privateBusCheckBox.isSelected();

                    // Update the room object
                    room.setRoomType(updatedRoomType);
                    room.setAvailableRooms(updatedAvailableRooms);
                    room.setBookedRooms(updatedBookedRooms);
                    room.setFreeBreakfast(updatedFreeBreakfast);
                    room.setParking(updatedParking);
                    room.setFlowers(updatedFlowers);
                    room.setFreeWifi(updatedFreeWifi);
                    room.setPrivateBus(updatedPrivateBus);

                    // Call the updateRoom method to save the updated room
                    boolean success = userDAO.updateRoom(room);

                    if (success) {
                        // Refresh the room table
                        allRooms = userDAO.getAllRooms();
                        displayRooms(currentPage, role);
                        JOptionPane.showMessageDialog(rooms.this, "Room updated successfully.");
                        dispose(); // Close the dialog
                    } else {
                        JOptionPane.showMessageDialog(rooms.this, "Failed to update room.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the dialog
                }
            });

            add(panel);
            pack();
            setLocationRelativeTo(parent);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // This is just for testing; you can remove this part when integrating with your application.
                JFrame frame = new rooms(0);
                frame.setVisible(true);
            }
        });
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
