package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import User_data.User;
import User_data.UserDetails;
import User_data.UserInfo;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author User
 */
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

}
