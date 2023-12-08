package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
  // Database URL, username and password
  private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/city_ride";
  private static final String DATABASE_USER = "root";
  //Change the database username if yours is not "root"
  private static final String DATABASE_PASSWORD = "";
  //Enter your own mysql password


  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
  }


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
