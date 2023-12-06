package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.DBConnector;
import org.example.model.CommuteDistance;
import org.example.model.Order;

public class OrderDao {

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
}
