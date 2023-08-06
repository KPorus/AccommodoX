package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import User_data.User;

/**
 *
 * @author User
 */
public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean registerUser(String username, String email, String password, String role) {
        String insertUserQuery = "INSERT INTO users (name, email, pass,role) VALUES (?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public String getUserRole(String username) {
        String selectUserRoleQuery = "SELECT role FROM users WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserRoleQuery)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the role is not found
    }

    public User getUser(String username) {
    String selectUserQuery = "SELECT * FROM users WHERE name = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery)) {
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String email = resultSet.getString("email");
            String password = resultSet.getString("pass");
            String role = resultSet.getString("role");
            
            // Create a User object and return it
            User user = new User(username, email, password, role);
            return user;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Return null if the user is not found
}


}
