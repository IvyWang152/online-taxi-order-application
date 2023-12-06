package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.DBConnector;
import org.example.model.Car;
import org.example.model.CommuteDistance;
import org.example.model.Order;

public class OrderDao {

  private CarDao carDao = new CarDao();
  public void orderManager(Order order){
    String startCity = order.getStartCity();
    List<Car> availableCars = showAvailableCars(startCity);
    if (availableCars.size()==0){
      System.out.println(String.format("No available cars in %s",order.getStartCity()));
      // to do: add delete order function here
      System.out.println("Sorry, the order is canceled.");
      return;
    }
    List<Car> matchedCars = showMatchedCars(startCity,order.getDesiredCapacity(),
        order.getAccessibility());
    if (matchedCars.size()==0){
      System.out.println("No matched cars for desired capacity or accessibility.");
      System.out.println(String.format("Please view available cars in %s",startCity));
      //add delete or update logic here
    } else {
      Car curr = matchedCars.get(0);
      processOrderWithMatchedCar(order.getId(), curr.getPlate(), order.getStartCity());
    }


  }
  public void processOrderWithMatchedCar(int orderId, String carPlate, String startCity){
    String procedureCall = "{CALL process_order(?,?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setInt(1, orderId);
      stmt.setString(2,carPlate);
      stmt.setString(3,startCity);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    updateOrderStatus(orderId,"in progress");
    System.out.println("Order is in progress!");
  }


  public List<Car> showAvailableCars(String startCity){
    List<Car> cars = new ArrayList<>();
    String procedureCall = "{CALL get_available_cars_in_city(?)}";
    try (Connection conn = DBConnector.getConnection();

         CallableStatement stmt = conn.prepareCall(procedureCall)){
      stmt.setString(1, startCity);
      try(ResultSet rs = stmt.executeQuery()){
        while (rs.next()) {
          Car car = carDao.mapRowToCar(rs);
          cars.add(car);
        }
      }
    } catch (SQLException e){
      e.printStackTrace();

    }
    return cars;
  }

  private void updateOrderStatus(int orderId, String newStatus) {
    String procedureCall = "{CALL update_order_status(?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setInt(1, orderId);
      stmt.setString(2, newStatus);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Car> showMatchedCars(String startCity, int capacity, boolean accessibility){
    List<Car> cars = new ArrayList<>();
    String procedureCall = "{CALL get_cars_with_cap_or_access(?,?,?)}";
    try (Connection conn = DBConnector.getConnection();

         CallableStatement stmt = conn.prepareCall(procedureCall)){
      stmt.setString(1, startCity);
      stmt.setInt(2, capacity);
      stmt.setBoolean(3,accessibility);
      try(ResultSet rs = stmt.executeQuery()){
        while (rs.next()) {
          Car car = carDao.mapRowToCar(rs);
          cars.add(car);
        }
      }
    } catch (SQLException e){
      e.printStackTrace();

    }
    return cars;
  }

  public List<CommuteDistance> viewRoutes(){
    List<CommuteDistance> res = new ArrayList<>();
    String procedureCall = "{CALL view_routes}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          CommuteDistance cd = mapRowToRoutes(rs);
          res.add(cd);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    return res;
  }


  private CommuteDistance mapRowToRoutes(ResultSet rs) throws SQLException {
    CommuteDistance cd = new CommuteDistance();
    cd.setStartCity(rs.getString("start_city"));
    cd.setEndCity(rs.getString("end_city"));
    cd.setDistance(rs.getDouble("distance"));
    return cd;
  }

  public Order getOrderById(int orderId) {
    Order order = null;
    String query = "SELECT * FROM ride_order WHERE id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, orderId);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          order = mapRowToOrder(rs);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    return order;
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
    // Add other mappings as needed
    return order;
  }

  public void updateOrderCapacity(int orderId, int newCapacity) {
    String query = "UPDATE ride_order SET desired_capacity = ? WHERE id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, newCapacity);
      pstmt.setInt(2, orderId);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
  }

  public void updateOrderAccessibility(int orderId, boolean newAccessibility) {
    String query = "UPDATE ride_order SET accessibility = ? WHERE id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setBoolean(1, newAccessibility);
      pstmt.setInt(2, orderId);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
  }

  public void updateOrderRoute(int orderId, String newStartCity, String newEndCity) {
    String query = "UPDATE ride_order SET start_city = ?, end_city = ? WHERE id = ?";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, newStartCity);
      pstmt.setString(2, newEndCity);
      pstmt.setInt(3, orderId);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
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
      //System.out.println("Order created successfully!");
      order.setId(getLatestOrderId(order.getAccountNumber()));
      orderManager(order);

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

  public int getLatestOrderId(String accountNumber) {
    int latestOrderId = 0;
    String query = "SELECT get_latest_order_id(?)";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setString(1, accountNumber);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          latestOrderId = rs.getInt(1);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return latestOrderId;
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

  public void deleteOrder(int orderId) {
    String deleteOrderQuery = "DELETE FROM ride_order WHERE id = ?";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(deleteOrderQuery)) {

      stmt.setInt(1, orderId);
      stmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
      throw new RuntimeException("Error deleting order: " + e.getMessage());
    }
  }
}
