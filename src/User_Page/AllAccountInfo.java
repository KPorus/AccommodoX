package User_Page;

import DB.MySQLConnection;
import DB.UserDAO;
import Design.GradientPanel;
import User_data.FundData;
import User_data.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllAccountInfo extends JFrame {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private List<FundData> accountDataList;
    private List<String> uniqueDates;
    private int currentPage = 1;
    private int pageSize = 10; // Number of rows per page
    private JButton backButton;
    private int Id;

    public AllAccountInfo(int userId) {
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection());

        setTitle("All Account");
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
        Book.addActionListener((ActionEvent e) -> {
            new RoomAccount(userId).setVisible(true);
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
        uniqueDates = new ArrayList<>();
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        accountTable = new JTable(tableModel);
        accountTable.setAutoCreateRowSorter(true);

        accountDataList = userDAO.getAllAccountData();
        displayAccountData(currentPage);

        JScrollPane scrollPane = new JScrollPane(accountTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(900, 350));
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel paginationPanel = new JPanel();
        paginationPanel.setPreferredSize(new Dimension(500, 50));

        JButton prevButton = new JButton("Previous");
        prevButton.setForeground(Color.white);
        prevButton.setBackground(new Color(24, 63, 102));
        prevButton.setFocusPainted(false);

        JButton nextButton = new JButton("Next");
        nextButton.setForeground(Color.white);
        nextButton.setBackground(new Color(24, 63, 102));
        nextButton.setFocusPainted(false);

        // Add an "Update" button for selected rows
        JButton updateDataButton = new JButton("Update Data");
        updateDataButton.setForeground(Color.white);
        updateDataButton.setBackground(new Color(24, 63, 102));
        updateDataButton.setFocusPainted(false);
        paginationPanel.add(updateDataButton);

        JButton addRowButton = new JButton("Add Row");
        addRowButton.setForeground(Color.white);
        addRowButton.setBackground(new Color(24, 63, 102));
        addRowButton.setFocusPainted(false);

        addRowButton.addActionListener((ActionEvent e) -> {
            DefaultTableModel model = (DefaultTableModel) accountTable.getModel();

            // Create a new row with default values or empty values
            Vector<Object> newRow = new Vector<>();
            newRow.add("Id");
            newRow.add(new java.sql.Date(System.currentTimeMillis())); // Date
            newRow.add(0.0); // Fund
            newRow.add(0.0); // Total Salary
            newRow.add(0.0); // Total paid in bkash
            newRow.add(0.0); // Total paid in manual

            model.addRow(newRow);

            // Now insert this new row into the database
            boolean insertSuccess = userDAO.insertNewRowIntoDatabase(new java.sql.Date(System.currentTimeMillis()), 0.0, 0.0, 0.0, 0.0);

            if (insertSuccess) {
                displayAccountData(currentPage);
                JOptionPane.showMessageDialog(this, "New row added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert the new row into the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateDataButton.addActionListener((ActionEvent e) -> {
            int selectedRow = accountTable.getSelectedRow();
            int selectedColumn = accountTable.getSelectedColumn();

            if (selectedRow >= 0 && selectedColumn >= 1) {
                int selectedRowId = getSelectedRowId(selectedRow);

                if (selectedRowId > 0) {
                    UpdateDataDialog dialog = new UpdateDataDialog(this, selectedRow, selectedColumn, selectedRowId);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to retrieve the ID for the selected row.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a cell to update.");
            }
        });

        prevButton.addActionListener((ActionEvent e) -> {
            if (currentPage > 1) {
                currentPage--;
                displayAccountData(currentPage);
            }
        });

        nextButton.addActionListener((ActionEvent e) -> {
            int maxPage = (int) Math.ceil((double) accountDataList.size() / pageSize);
            if (currentPage < maxPage) {
                currentPage++;
                displayAccountData(currentPage);
            }
        });

        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);
        paginationPanel.add(updateDataButton);
        paginationPanel.add(addRowButton);
        mainContentPanel.add(paginationPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        centerPanel.add(mainContentPanel, gbc);

        add(menuPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

    }

    private int getSelectedRowId(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < accountDataList.size()) {
            // Assuming your FundData class has a getId() method
            return accountDataList.get(selectedRow).getId();
        }
        return -1; // Return a value indicating no valid ID
    }

    private void displayAccountData(int page) {
        tableModel.setRowCount(0); // Clear existing rows
        int startIdx = (page - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, accountDataList.size());

        if (!accountDataList.isEmpty()) {
            uniqueDates = extractUniqueDates(accountDataList.get(0));
        }

        // Create a vector to hold the column names, starting with "Account" and then the unique dates
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Id");

        // Add sub-columns under "Account" for the desired values
        columnNames.add("Date");
        columnNames.add("Fund");
        columnNames.add("Total Salary");
        columnNames.add("Total paid in bkash");
        columnNames.add("Total paid in manual");

        // Set the column names for the table model
        tableModel.setColumnIdentifiers(columnNames);

        double[] columnSums = new double[columnNames.size() - 2]; // Exclude "Account" and "Date"

        for (int i = startIdx; i < endIdx; i++) {
            FundData accountData = accountDataList.get(i);

            Vector<Object> rowData = new Vector<>();

            rowData.add(accountData.getId());
            rowData.add(accountData.getDate());
            rowData.add(accountData.getFund());
            rowData.add(accountData.getTotalSalary());
            rowData.add(accountData.getRoomPrizeBkash());
            rowData.add(accountData.getRoomPrizeManual());

            // Update the column sums
            for (int j = 2; j < columnNames.size(); j++) {
                columnSums[j - 2] += Double.parseDouble(rowData.get(j).toString());
            }

            tableModel.addRow(rowData);
        }

        // Add a row for the column sums
        Vector<Object> sumRow = new Vector<>();
        sumRow.add("Sum");
        sumRow.add(""); // Empty value for "Date" in the sum row
        for (double columnSum : columnSums) {
            sumRow.add(columnSum);
        }
        tableModel.addRow(sumRow);

        // Add a row for "Total Income"
        double totalIncome = columnSums[0] + columnSums[1] + columnSums[2] + columnSums[3];
        Vector<Object> totalIncomeRow = new Vector<>();
        totalIncomeRow.add("Total Income");
        totalIncomeRow.add(totalIncome);
        tableModel.addRow(totalIncomeRow);
    }

    private List<String> extractUniqueDates(FundData fundData) {
        List<String> dates = new ArrayList<>();
        dates.add(fundData.getDate());
        for (FundData data : accountDataList) {
            if (!dates.contains(data.getDate())) {
                dates.add(data.getDate());
            }
        }
        return dates;
    }

    private double getValueForDate(FundData fundData, String date) {
        for (FundData data : accountDataList) {
            if (data.getDate().equals(date)) {
                return fundData.getFund();
            }
        }
        return 0.0; // Handle the case where data for the date is not found
    }

    public class UpdateDataDialog extends JDialog {

        private JTextField updatedDataField;
        private JButton updateButton;
        private int selectedRow;
        private int selectedColumn;
        private int id;

        public UpdateDataDialog(JFrame parent, int selectedRow, int selectedColumn, int id) {
            super(parent, "Update Data", true);
            this.selectedRow = selectedRow;
            this.selectedColumn = selectedColumn;
            this.id = id;

            // Initialize dialog components
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.setPreferredSize(new Dimension(400, 150));

            panel.add(new JLabel("Updated Data:"));
            updatedDataField = new JTextField();
            panel.add(updatedDataField);

            updateButton = new JButton("Update");
            updateButton.setForeground(Color.white);
            updateButton.setBackground(new Color(24, 63, 102));
            updateButton.setFocusPainted(false);
            panel.add(updateButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setForeground(Color.white);
            cancelButton.setBackground(new Color(24, 63, 102));
            cancelButton.setFocusPainted(false);

            updateButton.addActionListener((ActionEvent e) -> {
                String updatedData = updatedDataField.getText();
                if (!updatedData.isEmpty()) {
                    tableModel.setValueAt(updatedData, selectedRow, selectedColumn);

                    if (tableModel.getColumnName(selectedColumn).equals("Date")) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date utilDate = dateFormat.parse(updatedData);
                            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                            boolean updateSuccess = userDAO.updateDatabase(sqlDate,tableModel.getColumnName(selectedColumn),id);

                            if (updateSuccess) {
                                updateSumAndTotalIncome(selectedRow, selectedColumn);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to update data in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (ParseException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.");
                        }
                    } else {
                        String columnName = "";
                        
                        if(tableModel.getColumnName(selectedColumn).equals("Fund")){
                            columnName = "fund"; 
                        }
                        if(tableModel.getColumnName(selectedColumn).equals("Total Salary")){
                            columnName = "total_salary"; 
                        }
                        if(tableModel.getColumnName(selectedColumn).equals("Total paid in bkash")){
                            columnName = "room_prize_bkash"; 
                        }
                        if(tableModel.getColumnName(selectedColumn).equals("Total paid in manual")){
                            columnName = "room_prize_manual"; 
                        }
                        boolean updateSuccess = userDAO.updateDatabaseOne(updatedData, columnName, id);

                        if (updateSuccess) {
                            updateSumAndTotalIncome(selectedRow, selectedColumn);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to update data in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter updated data.");
                }
            });

            cancelButton.addActionListener((ActionEvent e) -> {
                dispose();
            });

            panel.add(cancelButton);

            add(panel);
            pack();
            setLocationRelativeTo(parent);
        }
    }

    private void updateSumAndTotalIncome(int row, int column) {
        int rowCount = tableModel.getRowCount();

        if (column >= 2) {
            double totalSum = 0.0;

            for (int r = 0; r < rowCount - 1; r++) {
                for (int c = 2; c < tableModel.getColumnCount(); c++) {
                    Object value = tableModel.getValueAt(r, c);
                    if (value instanceof Double) {
                        totalSum += (double) value;
                    } else if (value instanceof String) {
                        // Convert the string to a double before adding
                        try {
                            double doubleValue = Double.parseDouble((String) value);
                            totalSum += doubleValue;
                        } catch (NumberFormatException e) {
                            // Handle parsing errors or invalid strings
                            System.out.println("Invalid value: " + value);
                        }
                    }
                }
            }

            // Update the "Sum" row
            tableModel.setValueAt(totalSum, rowCount - 1, column);

            // Calculate "Total Income" without "Total paid in manual"
            double totalIncomeWithoutManual = 0.0;
            for (int c = 2; c < tableModel.getColumnCount() - 1; c++) {
                Object value = tableModel.getValueAt(rowCount - 1, c);
                if (value instanceof Double) {
                    totalIncomeWithoutManual += (double) value;
                } else if (value instanceof String) {
                    // Convert the string to a double before adding
                    try {
                        double doubleValue = Double.parseDouble((String) value);
                        totalIncomeWithoutManual += doubleValue;
                    } catch (NumberFormatException e) {
                        // Handle parsing errors or invalid strings
                        System.out.println("Invalid value: " + value);
                    }
                }
            }

            // Update "Total Income" row
            tableModel.setValueAt(totalIncomeWithoutManual, rowCount - 2, column);
        }
    }

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AllAccountInfo accountInfo = new AllAccountInfo(1);
            accountInfo.setVisible(true);
        });
    }

}
