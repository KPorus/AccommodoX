package Design;
import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class CustomDatePicker extends JPanel {

    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> dayComboBox;
    private JButton selectButton;

    public CustomDatePicker() {
        setLayout(new FlowLayout());

        // Initialize year combo box with years from 1900 to the current year
        yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 1900; year <= currentYear; year++) {
            yearComboBox.addItem(year);
        }

        // Initialize month combo box with months from 1 to 12
        monthComboBox = new JComboBox<>();
        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(month);
        }

        // Initialize day combo box with days from 1 to 31
        dayComboBox = new JComboBox<>();
        for (int day = 1; day <= 31; day++) {
            dayComboBox.addItem(day);
        }

        // Add components to the panel
        add(yearComboBox);
        add(monthComboBox);
        add(dayComboBox);
    }

    // You can add a getter method to retrieve the selected date as needed
    public Date getSelectedDate() {
        int selectedYear = (int) yearComboBox.getSelectedItem();
        int selectedMonth = (int) monthComboBox.getSelectedItem();
        int selectedDay = (int) dayComboBox.getSelectedItem();

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDay); // Month is 0-based
        return calendar.getTime();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Custom Date Picker Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new CustomDatePicker());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
