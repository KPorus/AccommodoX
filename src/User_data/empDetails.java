package User_data;

import java.util.Date;

public class empDetails {

    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private Date joinDate;
    private Date resignDate;
    private String role;
    private String salary;
    private String employeeType;

    public empDetails(int id, String name, String email, String address, String phone, Date joinDate, Date resignDate, String role, String salary, String employeeType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.joinDate = joinDate;
        this.resignDate = resignDate;
        this.role = role;
        this.salary = salary;
        this.employeeType = employeeType;
    }

    // Empty constructor
    public empDetails() {
        // Empty constructor
    }

    // Getters and setters for the fields
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public Date getResignDate() {
        return resignDate;
    }

    public String getRole() {
        return role;
    }

    public String getSalary() {
        return salary;
    }

    public String getEmployeeType() {
        return employeeType;
    }
}
