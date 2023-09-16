package User_data;

import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int userId;
    private int roomId ;
    private int NumberOfRooms;
    private int prize;
    private Date bookingTo;
    private Date bookingFrom;
    private Time checkInTime;
    private Time checkOutTime;


    public Booking(int userId, int roomId, int NumberOfRooms, int prize, Date bookingTo, Date bookingFrom, Time checkInTime, Time checkOutTime) {
        this.userId = userId;
        this.roomId = roomId;
        this.NumberOfRooms = NumberOfRooms;
        this.prize = prize;
        this.bookingTo = bookingTo;
        this.bookingFrom = bookingFrom;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }
// Empty constructor

    public Booking() {
        // Empty constructor
    }

    public int getUserId() {
        return userId;
    }
    
    public int getRoomId(){
        return roomId;
    }
    
    public int getNumberOfRoom(){
        return NumberOfRooms;
    }
    
    public int getTotalPrize(){
        return prize;
    }
    
    public Date getBookingTo()
    {
        return bookingTo;
    }
    
    public Date getBookingFrom()
    {
        return bookingFrom;
    }
    
    public Time getCheckInTime()
    {
        return checkInTime;
    }
 
    public Time getCheckOutTime()
    {
        return checkOutTime;
    }
}
