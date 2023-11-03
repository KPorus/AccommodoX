package User_data;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int userId;
    private int roomId;
    private int NumberOfRooms;
    private int prize;
    private Date bookingTo;
    private Date bookingFrom;
    private Time checkInTime;
    private Time checkOutTime;
    private String payment;

    public Booking(int userId, int roomId, int NumberOfRooms, int prize, Date bookingTo, Date bookingFrom, Time checkInTime, Time checkOutTime,String payment) {
        this.userId = userId;
        this.roomId = roomId;
        this.NumberOfRooms = NumberOfRooms;
        this.prize = prize;
        this.bookingTo = bookingTo;
        this.bookingFrom = bookingFrom;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.payment = payment;
    }

    // Empty constructor
    public Booking() {
        // Empty constructor
    }

    public int getUserId() {
        return userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getNumberOfRooms() {
        return NumberOfRooms;
    }

    public int getPrize() {
        return prize;
    }

    public Date getBookingTo() {
        return bookingTo;
    }

    public Date getBookingFrom() {
        return bookingFrom;
    }

    public Time getCheckInTime() {
        return checkInTime;
    }

    public Time getCheckOutTime() {
        return checkOutTime;
    }
    public String getpayment() {
        return payment;
    }
}
