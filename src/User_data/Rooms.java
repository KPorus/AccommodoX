package User_data;

public class Rooms {

    private int id;
    private String roomType;
    private int availableRooms;
    private int bookedRooms;
    private boolean freeBreakfast;
    private boolean parking;
    private boolean flowers;
    private boolean freeWifi;
    private boolean privateBus;
    private int prizePerDay;

    public Rooms(int id, String roomType, int availableRooms, int bookedRooms, boolean freeBreakfast,
            boolean parking, boolean flowers, boolean freeWifi, boolean privateBus, int prizePerDay) {
        this.id = id;
        this.roomType = roomType;
        this.availableRooms = availableRooms;
        this.bookedRooms = bookedRooms;
        this.freeBreakfast = freeBreakfast;
        this.parking = parking;
        this.flowers = flowers;
        this.freeWifi = freeWifi;
        this.privateBus = privateBus;
        this.prizePerDay = prizePerDay;
    }
// Empty constructor

    public Rooms() {
        // Empty constructor
    }

    public int getId() {
        return id;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public int getBookedRooms() {
        return bookedRooms;
    }

    public boolean isFreeBreakfast() {
        return freeBreakfast;
    }

    public boolean isParking() {
        return parking;
    }

    public boolean isFlowers() {
        return flowers;
    }

    public boolean isFreeWifi() {
        return freeWifi;
    }

    public boolean isPrivateBus() {
        return privateBus;
    }

    public int getPrizePerDay() {
        return prizePerDay;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public void setBookedRooms(int bookedRooms) {
        this.bookedRooms = bookedRooms;
    }

    public void setFreeBreakfast(boolean freeBreakfast) {
        this.freeBreakfast = freeBreakfast;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public void setFlowers(boolean flowers) {
        this.flowers = flowers;
    }

    public void setFreeWifi(boolean freeWifi) {
        this.freeWifi = freeWifi;
    }

    public void setPrivateBus(boolean privateBus) {
        this.privateBus = privateBus;
    }

    public void setPrizePerDay(int prizePerDay) {
        this.prizePerDay = prizePerDay;
    }

}
