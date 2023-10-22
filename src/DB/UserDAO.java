package DB;

import User_data.Booking;
import User_data.BookingWithUserInfo;
import User_data.Offer;
import User_data.Rooms;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import User_data.User;
import User_data.UserDetails;
import User_data.UserInfo;
import User_data.empDetails;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.sql.Types;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean isUserExists(String username) {
        String query = "SELECT * FROM users WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If there is a result, username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    public boolean isEmailExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If there is a result, email exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    public boolean registerUser(String username, String email, String password, String role) {
        String insertUserQuery = "INSERT INTO users (name, email, pass, role) VALUES (?, ?, ?, ?)";
        String insertUserDetailsQuery = "INSERT INTO userDetails (user_id, address, phone) VALUES (?, NULL, NULL)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1); // Retrieve the generated user id

                    try (PreparedStatement userDetailsStatement = connection.prepareStatement(insertUserDetailsQuery)) {
                        userDetailsStatement.setInt(1, userId); // Set the user id
                        userDetailsStatement.executeUpdate();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loginUser(String username, String password) {
        String selectUserQuery = "SELECT * FROM users WHERE name = ? AND pass = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If a matching user is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserInfo getUserInfo(String username) {
        String selectUserRoleQuery = "SELECT role,id FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserRoleQuery)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                int id = resultSet.getInt("id");
                return new UserInfo(id, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the role is not found
    }

    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        String selectUserRoleQuery = "SELECT * FROM users WHERE role = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserRoleQuery)) {
            preparedStatement.setString(1, "customer"); // Set the role value
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("pass");
                String role = resultSet.getString("role");

                // Create a User object and add it to the list
                User user = new User(id, username, email, password, role);
                customers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public boolean updateUserAndDetails(int userId, String name, String pass, String email, String address, String phone) {
        String updateUserQuery = "UPDATE users SET name = ?, pass = ?, email = ? WHERE id = ?";
        String updateUserDetailsQuery = "UPDATE userDetails SET address = ?, phone = ? WHERE user_id = ?";

        try {
            connection.setAutoCommit(false); // Start transaction

            // Update users table
            try (PreparedStatement userStatement = connection.prepareStatement(updateUserQuery)) {
                userStatement.setString(1, name);
                userStatement.setString(2, pass);
                userStatement.setString(3, email);
                userStatement.setInt(4, userId);
                userStatement.executeUpdate();
            }

            // Update userDetails table
            try (PreparedStatement userDetailsStatement = connection.prepareStatement(updateUserDetailsQuery)) {
                userDetailsStatement.setString(1, address);
                userDetailsStatement.setString(2, phone);
                userDetailsStatement.setInt(3, userId);
                userDetailsStatement.executeUpdate();
            }

            connection.commit(); // Commit the transaction
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback if there's an exception
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    public User getUser(int Id) {
        String selectUserQuery = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setInt(1, Id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("pass");
                String role = resultSet.getString("role");

                // Create a User object and return it
                User user = new User(id, username, email, password, role);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user is not found
    }

    public UserDetails getUserDetails(int user_id) {
        String selectUserQuery = "SELECT * FROM userDetails WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id"); // Change variable name to avoid conflicts
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");

                // Create a UserDetails object and return it
                UserDetails userDetails = new UserDetails(id, userId, address, phone);
                return userDetails;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the user details are not found
    }

    public boolean deleteUser(int user_id) {
        String deleteUserQuery = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUserQuery)) {
            preparedStatement.setInt(1, user_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserDetails(int user_id) {
        String deleteUserDetailsQuery = "DELETE FROM userDetails WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteUserDetailsQuery)) {
            preparedStatement.setInt(1, user_id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addEmployee(String name, String email, String phone, String address, String role, String salary, String employeeType) {
        String insertUserQuery = "INSERT INTO employee (name, email, phone, address, employeeType, role, salary, joinDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, employeeType);
            preparedStatement.setString(6, role);
            preparedStatement.setString(7, salary);
            preparedStatement.setDate(8, new java.sql.Date(System.currentTimeMillis())); // Assuming you want to set the joinDate to the current date

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1); // Retrieve the generated user id
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployee(int employeeId, String name, String email, String phone, String address, String role, String salary, String employeeType, Date joinDate, Date resignDate) {
        String updateEmployeeQuery = "UPDATE employee SET name=?, email=?, phone=?, address=?, employeeType=?, role=?, salary=?, joinDate=?, resignDate=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateEmployeeQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, employeeType);
            preparedStatement.setString(6, role);
            preparedStatement.setString(7, salary);

            // Check if joinDate is null before setting it
            if (joinDate != null) {
                preparedStatement.setDate(8, new java.sql.Date(joinDate.getTime())); // Correct casting
            } else {
                preparedStatement.setNull(8, Types.DATE); // Set joinDate as NULL in the database
            }

            // Check if resignDate is null before setting it
            if (resignDate != null) {
                preparedStatement.setDate(9, new java.sql.Date(resignDate.getTime())); // Correct casting
            } else {
                preparedStatement.setNull(9, Types.DATE); // Set resignDate as NULL in the database
            }

            preparedStatement.setInt(10, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<empDetails> getAllEmployees() {
        List<empDetails> employees = new ArrayList<>();
        String selectAllEmployeesQuery = "SELECT * FROM employee";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllEmployeesQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                Date joinDate = resultSet.getDate("joinDate");
                Date resignDate = resultSet.getDate("resignDate");
                String role = resultSet.getString("role");
                String salary = resultSet.getString("salary");
                String employeeType = resultSet.getString("employeeType");

                // Check for null resignDate and handle it appropriately
                if (resultSet.wasNull()) {
                    resignDate = null;
                }

                // Create an empDetails object and add it to the list
                empDetails details = new empDetails(id, name, email, address, phone, joinDate, resignDate, role, salary, employeeType);
                employees.add(details);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Rooms> getAllRooms() {
        List<Rooms> rooms = new ArrayList<>();
        String selectAllRoomsQuery = "SELECT * FROM rooms";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllRoomsQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("room_Type"); // Use correct column name
                int availableRooms = resultSet.getInt("available_room"); // Use correct column name
                int bookedRooms = resultSet.getInt("booked_rooms"); // Use correct column name
                boolean freeBreakfast = resultSet.getBoolean("free_breakfast"); // Use correct column name
                boolean parking = resultSet.getBoolean("parking"); // Use correct column name
                boolean flowers = resultSet.getBoolean("flowers"); // Use correct column name
                boolean freeWifi = resultSet.getBoolean("free_wifi"); // Use correct column name
                boolean privateBus = resultSet.getBoolean("private_bus"); // Use correct column name
                int prizePerDay = resultSet.getInt("prize_per_day"); // Use correct column name

                // Create a Rooms object and add it to the list
                Rooms room = new Rooms(id, type, availableRooms, bookedRooms, freeBreakfast, parking, flowers, freeWifi, privateBus, prizePerDay);
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public boolean updateRoom(Rooms room) {
        String updateRoomQuery = "UPDATE rooms SET room_Type=?, available_room=?, booked_rooms=?, free_breakfast=?, parking=?, free_wifi=?, private_bus=?, flowers=?, prize_per_day=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateRoomQuery)) {
            preparedStatement.setString(1, room.getRoomType());
            preparedStatement.setInt(2, room.getAvailableRooms());
            preparedStatement.setInt(3, room.getBookedRooms());
            preparedStatement.setBoolean(4, room.isFreeBreakfast());
            preparedStatement.setBoolean(5, room.isParking());
            preparedStatement.setBoolean(6, room.isFreeWifi());
            preparedStatement.setBoolean(7, room.isPrivateBus());
            preparedStatement.setBoolean(8, room.isFlowers());
            preparedStatement.setInt(9, room.getPrizePerDay());
            preparedStatement.setInt(10, room.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAvailableRoom(int available_room, int booked_rooms, int roomId) {
        String updateRoomQuery = "UPDATE rooms SET available_room=?, booked_rooms=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateRoomQuery)) {
            preparedStatement.setInt(1, available_room); // Set the value for the first parameter
            preparedStatement.setInt(2, booked_rooms);   // Set the value for the second parameter
            preparedStatement.setInt(3, roomId);        // Set the value for the third parameter

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertBooking(int userId, int roomId, int NumberOfRooms, int prize, Date bookingTo, Date bookingFrom, String checkInTime, String checkOutTime) {
        String insertBookingQuery = "INSERT INTO booking (userId, roomId, NumberOfRooms,prize, booking_to, booking_from, checkInTime, checkOutTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertBookingQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, NumberOfRooms);
            preparedStatement.setInt(4, prize);
            preparedStatement.setDate(5, bookingTo);
            preparedStatement.setDate(6, bookingFrom);
            preparedStatement.setString(7, checkInTime);
            preparedStatement.setString(8, checkOutTime);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Rooms getOneRoom(int roomId) {
        String selectBookingQuery = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBookingQuery)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("room_Type"); // Use correct column name
                int availableRooms = resultSet.getInt("available_room"); // Use correct column name
                int bookedRooms = resultSet.getInt("booked_rooms"); // Use correct column name
                boolean freeBreakfast = resultSet.getBoolean("free_breakfast"); // Use correct column name
                boolean parking = resultSet.getBoolean("parking"); // Use correct column name
                boolean flowers = resultSet.getBoolean("flowers"); // Use correct column name
                boolean freeWifi = resultSet.getBoolean("free_wifi"); // Use correct column name
                boolean privateBus = resultSet.getBoolean("private_bus"); // Use correct column name
                int prizePerDay = resultSet.getInt("prize_per_day"); // Use correct column name

                // Create a Rooms object and add it to the list
                Rooms room = new Rooms(id, type, availableRooms, bookedRooms, freeBreakfast, parking, flowers, freeWifi, privateBus, prizePerDay);
                return room;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the booking is not found
    }

    public List<Booking> getAllRoomsOfUser(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String selectBookingQuery = "SELECT * FROM booking WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBookingQuery)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("userId");
                int roomId = resultSet.getInt("roomId");
                int NumberOfRooms = resultSet.getInt("NumberOfRooms");
                int prize = resultSet.getInt("prize");
                Date bookingTo = resultSet.getDate("booking_to");
                Date bookingFrom = resultSet.getDate("booking_from");
                String checkInTimeStr = resultSet.getString("checkInTime");
                String checkOutTimeStr = resultSet.getString("checkOutTime");

                // Parse the formatted time strings into Time objects
                Time checkInTime = Time.valueOf(checkInTimeStr);
                Time checkOutTime = Time.valueOf(checkOutTimeStr);

                // Create a Booking object and add it to the list
                Booking booking = new Booking(id, roomId, NumberOfRooms, prize, bookingTo, bookingFrom, checkInTime, checkOutTime);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking getBooking(int userId) {
        String selectBookingQuery = "SELECT * FROM booking WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBookingQuery)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("userId");
                int roomId = resultSet.getInt("roomId");
                int NumberOfRooms = resultSet.getInt("NumberOfRooms");
                int prize = resultSet.getInt("prize");
                Date bookingTo = resultSet.getDate("booking_to");
                Date bookingFrom = resultSet.getDate("booking_from");
                String checkInTimeStr = resultSet.getString("checkInTime");
                String checkOutTimeStr = resultSet.getString("checkOutTime");

                // Parse the formatted time strings into Time objects
                Time checkInTime = Time.valueOf(checkInTimeStr);
                Time checkOutTime = Time.valueOf(checkOutTimeStr);

                // Create a Booking object and return it
                Booking booking = new Booking(id, roomId, NumberOfRooms, prize, bookingTo, bookingFrom, checkInTime, checkOutTime);
                return booking;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the booking is not found
    }

    public List<BookingWithUserInfo> getAllBookingsWithUserInfo() {
        String selectBookingQuery = "SELECT u.name AS user_name, r.room_Type, b.booking_to, b.booking_from, b.checkInTime, b.checkOutTime, b.NumberOfRooms, b.prize "
                + "FROM booking b "
                + "JOIN users u ON u.id = b.userId "
                + "JOIN rooms r ON r.id = b.roomId";

        List<BookingWithUserInfo> bookings = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBookingQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String roomType = resultSet.getString("room_Type");
                Date bookingTo = resultSet.getDate("booking_to");
                Date bookingFrom = resultSet.getDate("booking_from");
                String checkInTimeStr = resultSet.getString("checkInTime");
                String checkOutTimeStr = resultSet.getString("checkOutTime");
                int numberOfRooms = resultSet.getInt("NumberOfRooms");
                int prize = resultSet.getInt("prize");

                // Parse the formatted time strings into Time objects
                Time checkInTime = Time.valueOf(checkInTimeStr);
                Time checkOutTime = Time.valueOf(checkOutTimeStr);

                // Create a BookingWithUserInfo object and add it to the list
                BookingWithUserInfo bookingWithUserInfo = new BookingWithUserInfo(userName, roomType, bookingTo, bookingFrom, checkInTime, checkOutTime, numberOfRooms, prize);
                bookings.add(bookingWithUserInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }

    public boolean insertOffer(String title, String description, String status) {
        String insertOfferQuery = "INSERT INTO offers (title,description,status) VALUES (?, ?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOfferQuery)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, status);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Offer> getAllOffers() {
        String selectOffersQuery = "SELECT id,title, description,status FROM offers";
        List<Offer> offersList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectOffersQuery); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String status = resultSet.getString("status");
                int id = resultSet.getInt("id");
                Offer offer = new Offer(id, title, description, status);
                offersList.add(offer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offersList;
    }

    public boolean updateOffer(int id, String status) {
        String updateOfferQuery = "UPDATE offers SET status=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateOfferQuery)) {
            preparedStatement.setString(1, status); // Set the value for the first parameter
            preparedStatement.setInt(2, id); // Set the value for the second parameter

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOffer(int id) {
        String deleteOfferQuery = "DELETE FROM offers WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteOfferQuery)) {
            preparedStatement.setInt(1, id); // Set the ID as a parameter

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertBooking(int userId, int roomId, java.util.Date bookingDate, java.util.Date bookingDate0, Time checkInTime, Time checkOutTime) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
