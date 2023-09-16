package Design;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.sql.Time;
import java.util.Calendar;

public class CustomTimePicker extends JPanel {

    private JSpinner timeSpinner;
    private JSpinner amPmSpinner;

    public CustomTimePicker() {
        setLayout(new FlowLayout());

        // Create a time spinner for hours and minutes
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
        timeSpinner.setEditor(timeEditor);
        add(new JLabel("Time:"));
        add(timeSpinner);

        // Create a spinner for AM/PM
        amPmSpinner = new JSpinner(new SpinnerListModel(new String[]{"AM", "PM"}));
        add(new JLabel("AM/PM:"));
        add(amPmSpinner);
    }

    public Time getTime() {
        Date selectedTime = (Date) timeSpinner.getValue();
        String amPm = (String) amPmSpinner.getValue();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedTime);

        if (amPm.equals("PM")) {
            // If PM is selected, add 12 hours to the hour value
            calendar.add(Calendar.HOUR_OF_DAY, 12);
        }

        // Create a Time object from the modified Calendar instance
        return new Time(calendar.getTimeInMillis());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Time Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.getContentPane().add(new CustomTimePicker());
        frame.setVisible(true);
    }
}
