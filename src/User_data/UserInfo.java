/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User_data;

/**
 *
 * @author User
 */
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
