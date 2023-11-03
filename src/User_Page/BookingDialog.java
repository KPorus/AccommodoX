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
import User_data.User;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class BookingDialog extends JDialog {

    private UserDAO userDAO;
    private User userData;
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
        setTitle("Register Page");
        setIconImage(getAppIcon());

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

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        bookButton = new JButton("Book");
        bookButton.setForeground(Color.white);
        bookButton.setBackground(new Color(24, 63, 102));
        bookButton.setFocusPainted(false); // Disable focus border
        buttonPanel.add(bookButton);
      
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
                            generateBookingConfirmationPDF(userId, room, numberOfRooms, prize, offer);
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

        add(panel,BorderLayout.WEST);
        add(buttonPanel,BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private void generateBookingConfirmationPDF(int userId, Rooms room, int numberOfRooms, int prize, int offer) {
        // Create a PageFormat for the printer
        PageFormat pageFormat = new PageFormat();
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        this.userData = userDAO.getUser(userId);

        // Create a Printable object
        Printable printable = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex == 0) {
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Hotel Name
                    String hotelName = "Accomodox";
                    String invoiceSymbol = "************ Invoice for Room Booking ************";

                    // Create a text string to be printed
                    String text = "Booking Confirmation ************\n\n"
                            + "Customer Name: " + userData.getUsername() + "\n"
                            + " ************ ************ ************ ************ \n"
                            + "Email: " + userData.getEmail() + "\n"
                            + " ************ ************ ************ ************ \n"
                            + "Room Type: " + room.getRoomType() + "\n"
                            + " ************ ************ ************ ************ \n"
                            + "Number of Rooms: " + numberOfRooms + "\n"
                            + " ************ ************ ************ ************ \n"
                            + "Offer: " + offer + "%\n"
                            + " ************ ************ ************ ************ \n"
                            + "Prize: $" + prize + "\n\n"
                            + "\t\t\t\t\t\t\t\t admin\n"
                            + "\t\t\t\t\t\t\t\t ---------\n"
                            + "\t\t\t\t\t\t\t\t Signature";

                    // Set font and size for hotel name
                    g2d.setFont(new Font("Serif", Font.BOLD, 16));
                    g2d.drawString(hotelName, 100, 80);

                    // Set font and size for invoice symbol
                    g2d.setFont(new Font("Serif", Font.BOLD, 12));
                    g2d.drawString(invoiceSymbol, 100, 100);

                    // Set font and size for the rest of the text
                    g2d.setFont(new Font("Serif", Font.PLAIN, 12));

                    // Split the text into lines
                    String[] lines = text.split("\n");

                    int y = 140; // Initial y-coordinate

                    // Print each line of text
                    for (String line : lines) {
                        g2d.drawString(line, 100, y);
                        y += 20; // Move to the next line
                    }

                    return Printable.PAGE_EXISTS;
                } else {
                    return Printable.NO_SUCH_PAGE;
                }
            }
        };

        // Create a PrinterJob
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(printable, pageFormat);

        // Show a print dialog to choose printer and settings
        if (printerJob.printDialog()) {
            try {
                printerJob.print();
                JOptionPane.showMessageDialog(this, "Booking confirmation PDF printed successfully.");
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Failed to print booking confirmation PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    private Image getAppIcon() {
        ImageIcon icon = new ImageIcon("D:\\Java Project\\AccommodoX\\src\\Images\\hotel.jpeg");
        return icon.getImage();
    }
}
