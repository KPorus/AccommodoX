package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.User;
import User_data.UserDetails;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableColumnModel;

public class allCustomer extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private List<User> allCustomers;
    private JButton deleteButton; // Moved the deleteButton declaration here

    public allCustomer(User user, int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("All customers page");
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

        JButton users = new JButton("Customers");
        users.setForeground(Color.white);
        users.setBackground(new Color(24, 63, 102));
        users.setFocusPainted(false); // Disable focus border

        JButton emp = new JButton("Employees");
        emp.setForeground(Color.white);
        emp.setBackground(new Color(24, 63, 102));
        emp.setFocusPainted(false); // Disable focus border

        JButton rooms = new JButton("Rooms");
        rooms.setForeground(Color.white);
        rooms.setBackground(new Color(24, 63, 102));
        rooms.setFocusPainted(false); // Disable focus border

        JButton offer = new JButton("Offers");
        offer.setForeground(Color.white);
        offer.setBackground(new Color(24, 63, 102));
        offer.setFocusPainted(false); // Disable focus border

        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User userData = userDAO.getUser(userId); // Fetch user data by ID
                new admin(userData).setVisible(true); // Pass the fetched user data
                dispose();
            }
        });

        emp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new employee(userId).setVisible(true); // Pass the fetched user data
                dispose();
            }
        });

        rooms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new rooms(userId).setVisible(true); // Pass the fetched user data
                dispose();
            }
        });
        offer.addActionListener((ActionEvent e) -> {
            new Offers(userId).setVisible(true);
            dispose();
        });
        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(emp);
        menuPanel.add(rooms);
        menuPanel.add(offer);
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

        // Initialize the table model with columns
        String[] columnNames = {"Name", "Email", "Role", "Phone", "Address"};
        deleteButton = new JButton("Delete"); // Initialize the delete button
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (column == 3 || column == 4) { // Check if it's the "Phone" column
                    int customerId = allCustomers.get(row).getId();
                    UserDetails userDetails = userDAO.getUserDetails(customerId);
                    if (userDetails != null) {
                        return userDetails.getPhone(); // Check for null userDetails
                    }
                }
                return super.getValueAt(row, column);
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setAutoCreateRowSorter(true); // Allow sorting by columns

        // Fetch the list of customers from the database using userDAO
        allCustomers = userDAO.getAllCustomers();
        displayCustomers(currentPage);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setPreferredSize(new Dimension(500, 350)); // Set the preferred size for the scroll pane
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add pagination controls
        JPanel paginationPanel = new JPanel();
        paginationPanel.setPreferredSize(new Dimension(500, 50)); // Set the preferred size for the pagination panel

        JButton prevButton = new JButton("Previous");
        prevButton.setForeground(Color.white);
        prevButton.setBackground(new Color(24, 63, 102));
        prevButton.setFocusPainted(false); // Disable focus border

        JButton deleteSelectedButton = new JButton("Delete"); // New button
        deleteSelectedButton.setForeground(Color.white);
        deleteSelectedButton.setBackground(new Color(24, 63, 102));
        deleteSelectedButton.setFocusPainted(false); // Disable focus border

        JButton nextButton = new JButton("Next");
        nextButton.setForeground(Color.white);
        nextButton.setBackground(new Color(24, 63, 102));
        nextButton.setFocusPainted(false); // Disable focus border
        
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    displayCustomers(currentPage);
                }
            }
        });

        deleteSelectedButton.addActionListener(new ActionListener() { // New button action
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = customerTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int customerId = allCustomers.get(selectedRow).getId();
                    UserDetails userDetails = userDAO.getUserDetails(customerId);
                    int userId = userDetails.getUserId();

                    int confirm = JOptionPane.showConfirmDialog(
                            allCustomer.this,
                            "Are you sure you want to delete this user?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deletedUserDetails = userDAO.deleteUserDetails(userId);
                        boolean deletedUser = userDAO.deleteUser(customerId);

                        if (deletedUserDetails && deletedUser) {
                            JOptionPane.showMessageDialog(
                                    allCustomer.this,
                                    "User deleted successfully.",
                                    "Delete Successful",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            // Refresh the displayed customers
                            displayCustomers(currentPage);
                        } else {
                            JOptionPane.showMessageDialog(
                                    allCustomer.this,
                                    "Error deleting user.",
                                    "Delete Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            allCustomer.this,
                            "Please select a user to delete.",
                            "Delete Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int maxPage = (int) Math.ceil((double) allCustomers.size() / pageSize);
                if (currentPage < maxPage) {
                    currentPage++;
                    displayCustomers(currentPage);
                }
            }
        });

        paginationPanel.add(prevButton);
        paginationPanel.add(deleteSelectedButton); // Add the new delete button
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

    private void displayCustomers(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, allCustomers.size());

        for (int i = startIdx; i < endIdx; i++) {
            User customer = allCustomers.get(i);
            UserDetails userDetails = userDAO.getUserDetails(customer.getId());

            Object[] rowData = {customer.getUsername(), customer.getEmail(), customer.getRole(), userDetails != null ? userDetails.getPhone() : "", userDetails != null ? userDetails.getAddress() : ""};
            tableModel.addRow(rowData);
        }

        // Automatically adjust column widths based on content
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            packColumn(customerTable, i);
        }
    }

    // Automatically adjust the column width based on content
    private void packColumn(JTable table, int columnIndex) {
        int margin = 5; // Add a small margin for aesthetics
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(columnIndex);
        int width = 0;

        // Get the header renderer to calculate the width
        TableCellRenderer headerRenderer = col.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = table.getTableHeader().getDefaultRenderer();
        }

        Component headerComp = headerRenderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
        width = headerComp.getPreferredSize().width;

        // Iterate over the rows to find the maximum cell width
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, columnIndex);
            Component comp = table.prepareRenderer(renderer, row, columnIndex);
            width = Math.max(comp.getPreferredSize().width + margin, width);
        }

        // Set the column width with some additional margin
        col.setPreferredWidth(width + margin);
    }

    public static void main(String[] args) {
        // This is just for testing; you can remove this part when integrating with your application.
        JFrame frame = new allCustomer(null, 0);
        frame.setVisible(true);
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
