package User_Page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import DB.MySQLConnection;
import DB.UserDAO;
import User_data.Rooms;
import Design.CustomDatePicker;
import Design.CustomTimePicker;

public class BookingDialog extends JDialog {

    private UserDAO userDAO;
    private MySQLConnection mysqlConnection;
    private Rooms Room;
    private CustomTimePicker checkInTimePicker;
    private CustomTimePicker checkOutTimePicker;
    private CustomDatePicker bookingToDatePicker;
    private CustomDatePicker bookingFromDatePicker;
    private JTextField numberOfRoom;
    private JButton bookButton;

    public BookingDialog(JFrame parent, Rooms room, int userId) {

        super(parent, "Book Room", true);
        mysqlConnection = new MySQLConnection();
        this.userDAO = new UserDAO(mysqlConnection.getConnection()); // Initialize UserDAO
        // Initialize dialog components
        JPanel panel = new JPanel(new GridLayout(8, 2));

        panel.add(new JLabel("Check-in Time:"));
        checkInTimePicker = new CustomTimePicker();
        panel.add(checkInTimePicker);

        panel.add(new JLabel("Check-out Time:"));
        checkOutTimePicker = new CustomTimePicker();
        panel.add(checkOutTimePicker);

        panel.add(new JLabel("Booking From:"));
        CustomDatePicker bookingFromDatePicker = new CustomDatePicker(); // Use your custom date picker here
        panel.add(bookingFromDatePicker);

        panel.add(new JLabel("Booking To:"));
        CustomDatePicker bookingToDatePicker = new CustomDatePicker(); // Use your custom date picker here
        panel.add(bookingToDatePicker);

        panel.add(new JLabel("Number of Rooms:"));
        numberOfRoom = new JTextField();
        panel.add(numberOfRoom);

        bookButton = new JButton("Book");
        panel.add(bookButton);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve values entered by the user
                String number = numberOfRoom.getText();
                int numberOfRooms = 0; // Initialize to 0
                try {
                    numberOfRooms = Integer.parseInt(number);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BookingDialog.this, "Enter a valid number of rooms.");
                    return; // Stop further processing if the number is invalid
                }

                // Retrieve check-in and check-out times as formatted strings
                String checkInTimeFormatted = formatTime(checkInTimePicker.getTime());
                String checkOutTimeFormatted = formatTime(checkOutTimePicker.getTime());
//                System.out.println(checkInTimePicker.getTime());
//                System.out.println(checkOutTimePicker.getTime());
                // Retrieve selected dates
                java.util.Date bookingToUtil = bookingToDatePicker.getSelectedDate();
                java.util.Date bookingFromUtil = bookingFromDatePicker.getSelectedDate();

                // Convert java.util.Date to java.sql.Date
                Date bookingTo = new Date(bookingToUtil.getTime());
                Date bookingFrom = new Date(bookingFromUtil.getTime());

                // Compare only the dates, not the times
                Calendar calBookingTo = Calendar.getInstance();
                calBookingTo.setTime(bookingTo);
                calBookingTo.set(Calendar.HOUR_OF_DAY, 0);
                calBookingTo.set(Calendar.MINUTE, 0);
                calBookingTo.set(Calendar.SECOND, 0);
                calBookingTo.set(Calendar.MILLISECOND, 0);

                Calendar calBookingFrom = Calendar.getInstance();
                calBookingFrom.setTime(bookingFrom);
                calBookingFrom.set(Calendar.HOUR_OF_DAY, 0);
                calBookingFrom.set(Calendar.MINUTE, 0);
                calBookingFrom.set(Calendar.SECOND, 0);
                calBookingFrom.set(Calendar.MILLISECOND, 0);

                if (calBookingFrom.get(Calendar.DAY_OF_MONTH) == calBookingTo.get(Calendar.DAY_OF_MONTH)) {
                    JOptionPane.showMessageDialog(BookingDialog.this, "You have to stay a minimum of one day.");
                } else if (numberOfRooms <= 0) {
                    JOptionPane.showMessageDialog(BookingDialog.this, "Enter a valid number of rooms.");
                    dispose(); // Close the dialog
                } else {
                    Room = userDAO.getOneRoom(room.getId());
                    if (Room.getAvailableRooms() >= numberOfRooms) {
                        int prize = Room.getPrizePerDay() * numberOfRooms;
                        int available = Room.getAvailableRooms() - numberOfRooms;
                        int booked = Room.getBookedRooms() + numberOfRooms;
                        int offer = Room.getOffer();
                        if (Room.getOffer() > 0) {
                            int percent = Room.getOffer();
                            double p = percent / 100.0;
                            prize = (int) (prize * p);
                        }
                        // Call the insertBooking method to save the booking
                        boolean success = userDAO.insertBooking(userId, room.getId(), numberOfRooms, prize, bookingTo, bookingFrom, checkInTimeFormatted, checkOutTimeFormatted, offer);
                        boolean roomSuccess = userDAO.updateAvailableRoom(available, booked, room.getId());
                        if (success && roomSuccess) {
                            JOptionPane.showMessageDialog(BookingDialog.this, "Room booked successfully.");
                            dispose(); // Close the dialog
                        } else {
                            JOptionPane.showMessageDialog(BookingDialog.this, "Failed to book room.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(BookingDialog.this, "Room are not available.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    // Helper function to format time as HH:mm:ss
    private String formatTime(Time time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(time);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Booking Dialog Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JButton openDialogButton = new JButton("Open Dialog");
        openDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookingDialog dialog = new BookingDialog(frame, new Rooms(), 1);
                dialog.setVisible(true);
            }
        });

        frame.add(openDialogButton);
        frame.setVisible(true);
    }
}
