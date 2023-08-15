package User_data;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;

    public User(int id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Empty constructor
    public User() {
        // Empty constructor
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
