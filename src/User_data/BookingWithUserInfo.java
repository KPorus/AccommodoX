package User_data;

import java.sql.Date;
import java.sql.Time;

public class BookingWithUserInfo {

    private String userName;
    private String roomType;
    private Date bookingTo;
    private Date bookingFrom;
    private Time checkInTime;
    private Time checkOutTime;
    private int numberOfRooms;
    private int prize;

    public BookingWithUserInfo(String userName, String roomType, Date bookingTo, Date bookingFrom, Time checkInTime, Time checkOutTime, int numberOfRooms, int prize) {
        this.userName = userName;
        this.roomType = roomType;
        this.bookingTo = bookingTo;
        this.bookingFrom = bookingFrom;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.numberOfRooms = numberOfRooms;
        this.prize = prize;
    }

    public BookingWithUserInfo() {
        // Empty constructor
    }

    public String getUserName() {
        return userName;
    }

    public String getRoomType() {
        return roomType;
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

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public int getPrize() {
        return prize;
    }
}
