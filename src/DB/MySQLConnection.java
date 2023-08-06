
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/users";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;

    public MySQLConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


