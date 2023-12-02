package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
  // Database URL, username and password could be externalized in a properties file
  private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/mydatabase";
  private static final String DATABASE_USER = "username";
  private static final String DATABASE_PASSWORD = "password";

  // Static block for registering the JDBC driver
  static {
    try {
      // Assuming you are using a MySQL database; change this to match your database type
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      // Handle error (could be a runtime exception)
    }
  }

  // Method to establish and get database connection
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
  }

  // You might also want to add a utility method to close connections
  public static void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
        // Handle or log the error
      }
    }
  }
}
