package User_data;

public class Offer {
    private int id; // You might want to add an ID if offers have unique identifiers
    private String title;
    private String description;
    private String status;

    // Constructors
    public Offer() {
    }

    public Offer(int id,String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
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

}
