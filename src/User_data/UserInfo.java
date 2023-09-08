package User_data;

public class UserInfo {
    private int id;
    private String role;

    public UserInfo(int id, String role) {
        this.id = id;
        this.role = role;
    }

    // Empty constructor
    public UserInfo() {
        // Empty constructor
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }
 
    public String getRole() {
        return role;
    }
}
