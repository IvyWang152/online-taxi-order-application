package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
  // Database URL, username and password
  private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/city_ride";
  private static final String DATABASE_USER = "root";
  private static final String DATABASE_PASSWORD = "root";

  // Static block for registering the JDBC driver
//  static {
//    try {
//      Class.forName("com.mysql.cj.jdbc.Driver");
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//      // Handle error (could be a runtime exception)
//    }
//  }

  // Method to establish and get database connection
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
  }

  //a utility method to close connections
  public static void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
