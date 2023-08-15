package User_data;

public class UserDetails {
    private int id;
    private int user_id;
    private String address;
    private String phone;

    public UserDetails(int id, int user_id, String address, String phone) {
        this.id = id;
        this.user_id = user_id;
        this.address = address;
        this.phone = phone;
    }

    // Empty constructor
    public UserDetails() {
        // Empty constructor
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }
    
    public int getUserId() {
        return user_id;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
