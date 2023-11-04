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
    private int offer;
    private String payment;

    public BookingWithUserInfo(String userName, String roomType, Date bookingTo, Date bookingFrom, Time checkInTime, Time checkOutTime, int numberOfRooms, int prize, int offer,String payment) {
        this.userName = userName;
        this.roomType = roomType;
        this.bookingTo = bookingTo;
        this.bookingFrom = bookingFrom;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.numberOfRooms = numberOfRooms;
        this.prize = prize;
        this.offer = offer;
        this.payment = payment;
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
    public int getOffer() {
        return offer;
    }
    
    public String getPayment() {
        return payment;
    }
}
