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

    public allCustomer(User user, int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO

        setTitle("All customers page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        JButton users = new JButton("Customers");
        JButton emp = new JButton("Employees");
        profile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User userData = userDAO.getUser(userId); // Fetch user data by ID
                new admin(userData).setVisible(true); // Pass the fetched user data
                dispose();
            }
        });
        menuPanel.add(profile);
        menuPanel.add(users);
        menuPanel.add(emp);

        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        // Initialize the table model with columns
        String[] columnNames = {"Name", "Email", "Role", "Phone", "Address", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
            @Override
            public Object getValueAt(int row, int column) {
                if (column == 4) { // Check if it's the "Address" column
                    int customerId = allCustomers.get(row).getId();
                    UserDetails userDetails = userDAO.getUserDetails(customerId);
                    return userDetails.getAddress();
                } else if (column == 5) { // Check if it's the "Action" column
                    return "Delete";
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
        welcomePanel.add(scrollPane, BorderLayout.CENTER);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add pagination controls
        JPanel paginationPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    displayCustomers(currentPage);
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
        paginationPanel.add(nextButton);
        mainContentPanel.add(paginationPanel, BorderLayout.SOUTH);

        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);
    }

    private void displayCustomers(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, allCustomers.size());

        for (int i = startIdx; i < endIdx; i++) {
            User customer = allCustomers.get(i);
            UserDetails userDetails = userDAO.getUserDetails(customer.getId());

            // Create a "Delete" button for each row
            JButton deleteButton = new JButton("Delete");
            deleteButton.setActionCommand(Integer.toString(customer.getId())); // Set action command to customer ID
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int customerId = Integer.parseInt(e.getActionCommand()); // Get customer ID from action command
                    System.out.println("Delete button clicked for customer ID: " + customerId);
                    // Implement your delete logic here
                }
            });

            Object[] rowData = {customer.getUsername(), customer.getEmail(), customer.getRole(), userDetails.getPhone(), userDetails.getAddress(), deleteButton};
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
        // This is just for testing, you can remove this part when integrating with your application.
        JFrame frame = new allCustomer(null, 0);
        frame.setVisible(true);
    }
}
