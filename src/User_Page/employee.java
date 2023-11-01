package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.empDetails;
import User_data.User;
import Validation.isEmailValid;
import Validation.isPassValid;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.List;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class employee extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable employeeTable;
    private final DefaultTableModel tableModel; // Initialize the tableModel
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private List<empDetails> allEmployee;
    private isEmailValid emailValidator;
    private isPassValid passValidator;

    public employee(int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        emailValidator = new isEmailValid(); // Initialize the emailValidator instance
        passValidator = new isPassValid();// Initialize the passValidator instance

        setTitle("All Employees");
        setIconImage(getAppIcon());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 50, 1000, 500);
        setResizable(false);
        setContentPane(new GradientPanel());

        // Create a main content panel with BorderLayout
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);

        // Create a panel for the menu options
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 0, 20)); // Increase the row count to accommodate the "Add Employee" button
        menuPanel.setOpaque(false);

        JButton profile = new JButton("Profile");
        JButton customers = new JButton("Customers");
        JButton employees = new JButton("Employees");
        JButton rooms = new JButton("Rooms");
        JButton offer = new JButton("Offers");
        profile.addActionListener((ActionEvent e) -> {
            User userData = userDAO.getUser(userId); // Fetch user data by ID
            new admin(userData).setVisible(true); // Pass the fetched user data
            dispose();
        });
        customers.addActionListener((ActionEvent e) -> {
            new allCustomer(null, userId).setVisible(true);
            dispose();
        });
        rooms.addActionListener((ActionEvent e) -> {
            new rooms(userId).setVisible(true); // Pass the fetched user data
            dispose();
        });
        offer.addActionListener((ActionEvent e) -> {
            new Offers(userId).setVisible(true);
            dispose();
        });
        menuPanel.add(profile);
        menuPanel.add(customers);
        menuPanel.add(employees);
        menuPanel.add(rooms);
        menuPanel.add(offer);
        mainContentPanel.add(menuPanel, BorderLayout.WEST);

        // Create a panel for the welcome message and user information
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);

        // Initialize the table model with columns
        String[] columnNames = {"Name", "Phone", "Email", "Role", "Employee Type", "Address", "Salary", "Join Date", "Resign Date"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setAutoCreateRowSorter(true); // Allow sorting by columns

        // Fetch the list of employees from the database using userDAO
        allEmployee = userDAO.getAllEmployees();
        displayEmployee(currentPage);

        // Set the custom cell renderer to handle row colors
        employeeTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Enable horizontal scrolling
        welcomePanel.add(scrollPane, BorderLayout.CENTER);

        mainContentPanel.add(welcomePanel, BorderLayout.CENTER);

        // Add pagination controls
        JPanel paginationPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        prevButton.addActionListener((ActionEvent e) -> {
            if (currentPage > 1) {
                currentPage--;
                displayEmployee(currentPage);
            }
        });

        nextButton.addActionListener((ActionEvent e) -> {
            int maxPage = (int) Math.ceil((double) allEmployee.size() / pageSize);
            if (currentPage < maxPage) {
                currentPage++;
                displayEmployee(currentPage);
            }
        });

        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        mainContentPanel.add(paginationPanel, BorderLayout.SOUTH);

        // Add the main content panel to the JFrame
        getContentPane().add(mainContentPanel);

        // Create an "Add Employee" button
        JButton addEmployeeButton = new JButton("Add Employee");
        paginationPanel.add(addEmployeeButton);

        addEmployeeButton.addActionListener((ActionEvent e) -> {
            // Open a dialog to add a new employee
            AddEmployeeDialog dialog = new AddEmployeeDialog(employee.this);
            dialog.setVisible(true);
        });

        // Add an "Update" button for selected rows
        JButton updateEmployeeButton = new JButton("Update Employee");
        paginationPanel.add(updateEmployeeButton);

        updateEmployeeButton.addActionListener((ActionEvent e) -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                // Get the selected employee
                empDetails selectedEmployee = allEmployee.get(selectedRow);

                // Open a dialog to update the selected employee
                UpdateEmployeeDialog dialog = new UpdateEmployeeDialog(employee.this, selectedEmployee);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(employee.this, "Please select an employee to update.");
            }
        });
    }

    // Inside your Employee class
    private void displayEmployee(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, allEmployee.size());

        for (int i = startIdx; i < endIdx; i++) {
            empDetails employee = allEmployee.get(i);

            String joinDateStr = (employee.getJoinDate() != null) ? employee.getJoinDate().toString() : ""; // Handle nullable joinDate
            String resignDateStr = (employee.getResignDate() != null) ? employee.getResignDate().toString() : ""; // Handle nullable resignDate

            Object[] rowData = {employee.getName(), employee.getPhone(), employee.getEmail(), employee.getRole(), employee.getEmployeeType(), employee.getAddress(), employee.getSalary(), joinDateStr, resignDateStr};
            tableModel.addRow(rowData);
        }

        // Automatically adjust column widths based on content
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            packColumn(employeeTable, i, 150);
        }
    }

    // Automatically adjust the column width based on content
    private void packColumn(JTable table, int columnIndex, int width) {
        int margin = 5; // Add a small margin for aesthetics
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(columnIndex);

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

    // Inner class for the "Add Employee" dialog
    private class AddEmployeeDialog extends JDialog {

        private JTextField nameField;
        private JTextField emailField;
        private JTextField phoneField;
        private JTextField addressField;
        private JTextField roleField;
        private JTextField salaryField;
        private JTextField employeeTypeField;

        public AddEmployeeDialog(JFrame parent) {
            super(parent, "Add Employee", true);

            // Initialize dialog components
            JPanel panel = new JPanel(new GridLayout(8, 2));
            panel.setPreferredSize(new Dimension(300, 250));

            panel.add(new JLabel("Name:"));
            nameField = new JTextField();
            panel.add(nameField);

            panel.add(new JLabel("Email:"));
            emailField = new JTextField();
            panel.add(emailField);

            panel.add(new JLabel("Phone:"));
            phoneField = new JTextField();
            panel.add(phoneField);

            panel.add(new JLabel("Address:"));
            addressField = new JTextField();
            panel.add(addressField);

            panel.add(new JLabel("Role:"));
            roleField = new JTextField();
            panel.add(roleField);

            panel.add(new JLabel("Salary:"));
            salaryField = new JTextField();
            panel.add(salaryField);

            panel.add(new JLabel("Employee Type:"));
            employeeTypeField = new JTextField();
            panel.add(employeeTypeField);

            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");

            saveButton.addActionListener((ActionEvent e) -> {
                // Get the employee information from the dialog
                String name1 = nameField.getText();
                String email = emailField.getText().toLowerCase();
                String phone = phoneField.getText();
                String address = addressField.getText();
                String role = roleField.getText().toLowerCase();
                String salary = salaryField.getText();
                String employeeType = employeeTypeField.getText();
                // Call the addEmployee method to save the new employee
                boolean success = false;
                if ("receiptionist".equals(role) || "accountent".equals(role)) {
                    String pass = JOptionPane.showInputDialog(employee.this, "Enter a demo password:");
                    if (!passValidator.isValidPass(pass) && !emailValidator.isValidEmail(email)) {
                        JOptionPane.showMessageDialog(employee.this, "Invalid email & password format.");
                    } else if (!emailValidator.isValidEmail(email)) {
                        JOptionPane.showMessageDialog(employee.this, "Invalid email format.");
                    } else if (!passValidator.isValidPass(pass)) {
                        JOptionPane.showMessageDialog(employee.this, "Invalid password format.");
                    } else if (userDAO.isUserExists(name1)) {
                        JOptionPane.showMessageDialog(employee.this, "Username already exists.");
                    } else if (userDAO.isEmailExists(email)) {
                        JOptionPane.showMessageDialog(employee.this, "Email already exists.");
                    } else {
                        // Proceed with registration
                        if (userDAO.registerUser(name1, email, pass, role)) {
                            JOptionPane.showConfirmDialog(employee.this, "Employee Registration Successful", "Success Message", 0b0);
                            success = userDAO.addEmployee(name1, email, phone, address, role, salary, employeeType);
                        } else {
                            JOptionPane.showMessageDialog(employee.this, "Employee registration failed.");
                        }
                    }
                } else {
                    success = userDAO.addEmployee(name1, email, phone, address, role, salary, employeeType);
                }
                if (success) {
                    // Refresh the employee table
                    allEmployee = userDAO.getAllEmployees();
                    displayEmployee(currentPage);
                    JOptionPane.showMessageDialog(employee.this, "Employee added successfully.");
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(employee.this, "Failed to add employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener((ActionEvent e) -> {
                dispose(); // Close the dialog
            });

            panel.add(saveButton);
            panel.add(cancelButton);

            add(panel);
            pack();
            setLocationRelativeTo(parent);
        }
    }

    // Inner class for the "Update Employee" dialog
    private class UpdateEmployeeDialog extends JDialog {

        private JTextField nameField;
        private JTextField emailField;
        private JTextField phoneField;
        private JTextField addressField;
        private JTextField roleField;
        private JTextField salaryField;
        private JTextField employeeTypeField;
        private JTextField joinDateField; // Add a field for join date
        private JTextField resignDateField; // Add a field for resign date
        private JButton updateButton; // Add an "Update" button

        public UpdateEmployeeDialog(JFrame parent, empDetails employee) {
            super(parent, "Update Employee", true);

            // Initialize dialog components
            JPanel panel = new JPanel(new GridLayout(11, 2)); // Adjust the row count
            panel.setPreferredSize(new Dimension(400, 400)); // Adjust the panel size

            panel.add(new JLabel("Name:"));
            nameField = new JTextField(employee.getName());
            panel.add(nameField);

            panel.add(new JLabel("Email:"));
            emailField = new JTextField(employee.getEmail());
            panel.add(emailField);

            panel.add(new JLabel("Phone:"));
            phoneField = new JTextField(employee.getPhone());
            panel.add(phoneField);

            panel.add(new JLabel("Address:"));
            addressField = new JTextField(employee.getAddress());
            panel.add(addressField);

            panel.add(new JLabel("Role:"));
            roleField = new JTextField(employee.getRole());
            panel.add(roleField);

            panel.add(new JLabel("Salary:"));
            salaryField = new JTextField(employee.getSalary());
            panel.add(salaryField);

            panel.add(new JLabel("Employee Type:"));
            employeeTypeField = new JTextField(employee.getEmployeeType());
            panel.add(employeeTypeField);

            panel.add(new JLabel("Join Date (yyyy-MM-dd):")); // Add a label for join date
            joinDateField = new JTextField(employee.getJoinDate() != null ? employee.getJoinDate().toString() : ""); // Initialize with the existing join date
            panel.add(joinDateField);

            panel.add(new JLabel("Resign Date (yyyy-MM-dd):")); // Add a label for resign date
            resignDateField = new JTextField(employee.getResignDate() != null ? employee.getResignDate().toString() : ""); // Initialize with the existing resign date
            panel.add(resignDateField);

            updateButton = new JButton("Update"); // Add an "Update" button
            panel.add(updateButton);

            JButton cancelButton = new JButton("Cancel");

            updateButton.addActionListener((ActionEvent e) -> {
                // Get the updated employee information from the dialog
                String updatedName = nameField.getText();
                String updatedEmail = emailField.getText();
                String updatedPhone = phoneField.getText();
                String updatedAddress = addressField.getText();
                String updatedRole = roleField.getText();
                String updatedSalary = salaryField.getText();
                String updatedEmployeeType = employeeTypeField.getText();
                String updatedJoinDateStr = joinDateField.getText(); // Get the join date as a string
                String updatedResignDateStr = resignDateField.getText(); // Get the resign date as a string

                // Parse the join date and resign date from the string format
                Date updatedJoinDate = null;
                Date updatedResignDate = null;

                try {
                    if (!updatedJoinDateStr.isEmpty()) {
                        updatedJoinDate = Date.valueOf(updatedJoinDateStr);
                    }

                    if (!updatedResignDateStr.isEmpty()) {
                        updatedResignDate = Date.valueOf(updatedResignDateStr);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(employee.this, "Invalid date format. Please use yyyy-MM-dd.");
                    return;
                }

                // Call the updateEmployee method to save the updated employee
                boolean success = userDAO.updateEmployee(employee.getId(), updatedName, updatedEmail, updatedPhone, updatedAddress, updatedRole, updatedSalary, updatedEmployeeType, updatedJoinDate, updatedResignDate);

                if (success) {
                    // Refresh the employee table
                    allEmployee = userDAO.getAllEmployees();
                    displayEmployee(currentPage);
                    JOptionPane.showMessageDialog(employee.this, "Employee updated successfully.");
                    dispose(); // Close the dialog
                } else {
                    JOptionPane.showMessageDialog(employee.this, "Failed to update employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener((ActionEvent e) -> {
                dispose(); // Close the dialog
            });

            panel.add(cancelButton);

            add(panel);
            pack();
            setLocationRelativeTo(parent);
        }
    }

    // Custom table cell renderer for handling row colors
    class CustomTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Convert the view index (sorted index) to the model index (original index)
            int modelRow = table.convertRowIndexToModel(row);
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            empDetails employee = allEmployee.get(modelRow); // Use the modelRow to access the data

            if (employee.getResignDate() != null) {
                cell.setBackground(Color.RED);
                cell.setForeground(Color.WHITE);
            } else {
                cell.setBackground(Color.WHITE);
                cell.setForeground(Color.BLACK);
            }

            return cell;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // This is just for testing; you can remove this part when integrating with your application.
            JFrame frame = new employee(0);
            frame.setVisible(true);
        });
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
