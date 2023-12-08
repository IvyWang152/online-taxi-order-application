package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.DBConnector;
import org.example.model.Car;
import org.example.model.CommuteDistance;
import org.example.model.Order;

public class OrderDao {
  final double BASE_FARE = 3.50;
  final double PER_MILE_RATE = 0.50;
  private final CarDao carDao = new CarDao();
  public void orderManager(Order order){
    String startCity = order.getStartCity();
    List<Car> availableCars = showAvailableCars(startCity);
    if (availableCars.isEmpty()){
      System.out.printf("No available cars in %s%n",startCity);
      deleteOrder(order.getId());
      System.out.println("Sorry, the order is canceled.");
      return;
    }
    List<Car> matchedCars = showMatchedCars(startCity,order.getDesiredCapacity(),
        order.getAccessibility());
    if (matchedCars.isEmpty()) {
      System.out.println("No matched cars for desired capacity or accessibility.");
      boolean continueUpdate = true;
      while (continueUpdate) {
        System.out.printf("1. Please view available cars in %s%n", startCity);
        System.out.println("2. Update capacity and accessibility");
        System.out.println("3. Delete order");

        int userChoice = getUserInput();

        switch (userChoice) {
          case 1 -> {
            if (!availableCars.isEmpty()) {
              System.out.println("Available cars in " + startCity + ":");
              for (Car car : availableCars) {
                System.out.println(car);
              }
            } else {
              System.out.printf("No available cars in %s%n", startCity);
            }
          }
          case 2 -> {
            int newCapacity = getNewCapacity();
            boolean newAccessibility = getNewAccessibility();
            updateOrderCapacity(order.getId(), newCapacity);
            updateOrderAccessibility(order.getId(), newAccessibility);
            // Retry matching with the updated order
            matchedCars = showMatchedCars(startCity, newCapacity, newAccessibility);
            if (!matchedCars.isEmpty()) {
              Car updatedCar = matchedCars.get(0);
              processOrderWithMatchedCar(order.getId(), updatedCar.getPlate(), startCity);
              continueUpdate = false;
            }
            else {
            continueUpdate = false;
          }
          }
          case 3 -> {
            deleteOrder(order.getId());
            System.out.println("Order deleted. Sorry, the order is canceled.");
            continueUpdate = false;
          }
          default -> System.out.println("Invalid choice. Please choose a valid option.");
        }
      }
    } else {
      Car curr = matchedCars.get(0);
      processOrderWithMatchedCar(order.getId(), curr.getPlate(), startCity);
    }

  }
  private int getUserInput() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your choice: ");
    return scanner.nextInt();
  }

  private int getNewCapacity() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter the new capacity: ");
    return scanner.nextInt();
  }

  private boolean getNewAccessibility() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter the new accessibility (true/false): ");
    return scanner.nextBoolean();
  }

  public void processOrderWithMatchedCar(int orderId, String carPlate,String startCity){
    String procedureCall = "{CALL process_order(?,?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setInt(1, orderId);
      stmt.setString(2,carPlate);
      stmt.setString(3,startCity);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
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

  public void updateOrderStatus(int orderId, String newStatus) {
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
    order.setOrderStatus(rs.getString("order_status"));
    order.setFare(rs.getDouble("fare"));
    order.setPassengerReview(rs.getInt("passenger_review"));
    order.setDriverReview(rs.getInt("driver_review"));
    return order;
  }

  public void updateOrderCapacity(int orderId, int newCapacity) {
//    String query = "UPDATE ride_order SET desired_capacity = ? WHERE id = ?";
    String query = "{CALL update_order_capacity(?,?)}";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, orderId);
      pstmt.setInt(2, newCapacity);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void updateOrderAccessibility(int orderId, boolean newAccessibility) {
//    String query = "UPDATE ride_order SET accessibility = ? WHERE id = ?";
    String query = "{ CALL update_order_accessibility(?,?)}";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setInt(1, orderId);
      pstmt.setBoolean(2, newAccessibility);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void updateOrderRoute(int orderId, String newStartCity, String newEndCity) {
//    String query = "UPDATE ride_order SET start_city = ?, end_city = ? WHERE id = ?";
    String query = "{CALL update_order_route(?,?,?)}";
    try (Connection conn = DBConnector.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setInt(1, orderId);
      pstmt.setString(2, newStartCity);
      pstmt.setString(3, newEndCity);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
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

//  public Order getOrderById(int orderId) {
//    Order order = null;
//    String query = "SELECT * FROM ride_order WHERE id = ?";
//
//    try (Connection conn = DBConnector.getConnection();
//         PreparedStatement pstmt = conn.prepareStatement(query)) {
//
//      pstmt.setInt(1, orderId);
//
//      try (ResultSet rs = pstmt.executeQuery()) {
//        if (rs.next()) {
//          order = mapRowToOrder(rs);
//        }
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//    return order;
//  }
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
    }

    return orders;
  }
  public void updateOrderfare(int orderId){
    String procedureCall = "{CALL update_order_fare(?,?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setInt(1, orderId);
      stmt.setDouble(2, BASE_FARE);
      stmt.setDouble(3,PER_MILE_RATE);
      stmt.execute();
      System.out.println("Order completed!");
    } catch (SQLException e) {
      if ("45000".equals(e.getSQLState())){
        System.out.println("Invalid order id. Please retry it");
        return;
      }
      e.printStackTrace();
    }

  }
  //Driver order operations
  public List<Order> getOrdersByStatus(String driverLicense, String orderStatus){
    List<Order> orders = new ArrayList<>();
    String procedureCall = "{CALL get_orders_by_driver(?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, driverLicense);
      stmt.setString(2,orderStatus);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Order order = mapRowToOrder(rs);
          orders.add(order);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return orders;
  }
  public void deleteOrder(int orderId) {
//    String deleteOrderQuery = "DELETE FROM ride_order WHERE id = ?";
    String deleteOrderQuery = "{CALL delete_order(?)}";

    try (Connection conn = DBConnector.getConnection();
         PreparedStatement stmt = conn.prepareStatement(deleteOrderQuery)) {

      stmt.setInt(1, orderId);
      stmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException("Error deleting order: " + e.getMessage());
    }
  }
}
