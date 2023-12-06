package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
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

}
