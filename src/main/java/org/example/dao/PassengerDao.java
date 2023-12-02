package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.DBConnector;
import org.example.model.Passenger;

public class PassengerDao {
  public List<Passenger> getPassengers() throws SQLException {
    List<Passenger> passengers = new ArrayList<>();
    String sql = "SELECT * FROM Passenger"; // Adjusted to retrieve all passenger details

    try (Connection conn = DBConnector.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Passenger passenger = mapRowToPassenger(rs);
        passengers.add(passenger);
      }
    }
    return passengers;
  }

  public void createPassenger(Passenger passenger) throws SQLException {
    String sql = "INSERT INTO Passenger (account_number, name, gender, birth_date) VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, passenger.getAccountNumber());
      pstmt.setString(2, passenger.getName());
      pstmt.setString(3, passenger.getGender());
      pstmt.setDate(4, new java.sql.Date(passenger.getBirthDate().getTime()));

      pstmt.executeUpdate();
    }
  }

  private Passenger mapRowToPassenger(ResultSet rs) throws SQLException {
    Passenger passenger = new Passenger();
    passenger.setAccountNumber(rs.getString("account_number"));
    passenger.setName(rs.getString("name"));
    passenger.setGender(rs.getString("gender"));
    passenger.setBirthDate(rs.getDate("birth_date"));
    return passenger;
  }

  // Additional methods for update and delete operations
}
