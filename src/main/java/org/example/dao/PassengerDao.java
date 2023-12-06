package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.example.DBConnector;
import org.example.model.Order;
import org.example.model.Passenger;

public class PassengerDao {

  public void addPassenger(Passenger passenger) {
    // SQL query or stored procedure call to add a passenger
    String procedureCall = "{CALL create_passenger(?, ?, ?, ?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, passenger.getAccountNumber());
      stmt.setString(2, passenger.getName());
      stmt.setString(3, passenger.getGender());
      stmt.setDate(4, new java.sql.Date(passenger.getBirthDate().getTime()));

      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
  }
  public Passenger getPassenger(String accountNumber) {
    // SQL query or stored procedure call to get a passenger by their accountNumber
    String procedureCall = "{CALL get_passenger_by_account_number(?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setString(1, accountNumber);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToPassenger(rs);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    return null;
  }
  public Passenger updatePassenger(Passenger passenger){
    String procedureCall = "{CALL update_user_profile(?, ?, ?, ?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setString(1, passenger.getAccountNumber());
      stmt.setString(2, passenger.getName());
      stmt.setString(3, passenger.getGender());
      stmt.setDate(4, new java.sql.Date(passenger.getBirthDate().getTime()));
      stmt.execute();
      return getPassenger(passenger.getAccountNumber());
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    return null;
  }

  public void deletePassenger(String accountNumber){
    String procedureCall = "{CALL delete_passenger_account(?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)){
      stmt.setString(1,accountNumber);
      stmt.execute();
    } catch (SQLException e){
      e.printStackTrace();
    }
  }
  public List<Passenger> getAllPassengers() throws SQLException {
    List<Passenger> passengers = new ArrayList<>();
    String sql = "SELECT * FROM Passenger";

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

  private Passenger mapRowToPassenger(ResultSet rs) throws SQLException {
    Passenger passenger = new Passenger();
    passenger.setAccountNumber(rs.getString("account_number"));
    passenger.setName(rs.getString("name"));
    passenger.setGender(rs.getString("gender"));
    passenger.setBirthDate(rs.getDate("birth_date"));
    return passenger;
  }
  public void createOrder(Order order) {
    String procedureCall = "{CALL create_order(?, ?, ?, ?, ?, ?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setDate(1, new java.sql.Date(order.getOrderDate().getTime()));
      stmt.setInt(2, order.getDesiredCapacity());
      stmt.setBoolean(3, order.getAccessibility());
      stmt.setString(4, order.getAccountNumber());
      stmt.setString(5, order.getStartCity());
      stmt.setString(6, order.getEndCity());

      stmt.execute();
      System.out.println("Order created successfully!");
    } catch (SQLException e) {
      if (e.getMessage().contains("FOREIGN KEY (`start_city`, `end_city`)")) {
        // Handle duplicate entry
        System.out.println(
            String.format("Error: There's no route from %s to %s. Please view existing city routes",
                order.getStartCity(),order.getEndCity()));
      }
      else{
          System.err.println("Error creating order: " + e.getMessage());

          throw new RuntimeException("Failed to create order", e);
        }
    }
  }

  public List<Order> getOrdersForPassenger(String accountNumber) {
    List<Order> orders = new ArrayList<>();
    String procedureCall = "{CALL get_orders_for_passenger(?)}";

    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, accountNumber);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Order order = mapRowToOrder(rs);
          orders.add(order);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }

    return orders;
  }

  private Order mapRowToOrder(ResultSet rs) throws SQLException {
    Order order = new Order();
    order.setId(rs.getInt("id"));
    order.setOrderDate(rs.getDate("order_date"));
    order.setAccountNumber(rs.getString("account_number"));
    order.setDesiredCapacity(rs.getInt("desired_capacity"));
    order.setAccessibility(rs.getBoolean("accessibility"));
    order.setStartCity(rs.getString("start_city"));
    order.setEndCity(rs.getString("end_city"));
    order.setCarPlate(rs.getString("car_plate"));


    // Set other attributes of the Order class based on your model
    order.setDriverReview(rs.getInt("driver_review"));
    order.setPassengerReview(rs.getInt("passenger_review"));
    order.setOrderStatus(rs.getString("order_status"));
    order.setFare(rs.getDouble("fare"));

    return order;
  }


}
