package User_data;

public class Offer {

    private int id; // You might want to add an ID if offers have unique identifiers
    private String title;
    private String description;
    private String status;
    private String room_type;
    private int percentage;

    // Constructors
    public Offer() {
    }

    public Offer(int id, String title, String description, String status, String room_type, int percentage) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.room_type = room_type;
        this.percentage = percentage;
    }

    public int getOfferId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getRoomType() {
        return room_type;
    }

    public int getPercentage() {
        return percentage;
    }
}
