package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.DBConnector;
import org.example.model.Driver;

//Each method in the DriverDao corresponds to a specific database operation for the Driver entity.
//addDriver, getDriver, getAllDrivers, updateDriver, and deleteDriver are straightforward methods
// that execute SQL commands or stored procedures using JDBC.
//mapRowToDriver is a utility method to convert a ResultSet row into a Driver object.
// This is helpful to avoid duplicating this code in methods that read drivers from the database
public class DriverDao {

  public void addDriver(Driver driver) {
    // SQL query or stored procedure call to add a driver
    String procedureCall = "{CALL create_driver(?, ?, ?, ?, ?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, driver.getDriverLicense());
      stmt.setString(2, driver.getName());
      stmt.setString(3, driver.getGender());
      stmt.setDate(4, new java.sql.Date(driver.getBirthDate().getTime()));
      stmt.setString(5, driver.getAddress());

      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
  }

  public Driver getDriver(String driverLicense) {
    // SQL query or stored procedure call to get a driver by driverLicense
    String procedureCall = "{CALL get_driver_by_license(?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setString(1, driverLicense);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return mapRowToDriver(rs);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    return null;
  }

  public Driver updateDriver(Driver driver){
    String procedureCall = "{CALL update_driver_details(?, ?, ?, ?, ?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {
      stmt.setString(1, driver.getDriverLicense());
      stmt.setString(2, driver.getName());
      stmt.setString(3, driver.getGender());
      stmt.setDate(4, new java.sql.Date(driver.getBirthDate().getTime()));
      stmt.setString(5, driver.getAddress());
      stmt.setBoolean(6,driver.getAvailable());
      stmt.execute();
      return getDriver(driver.getDriverLicense());
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle or log the error appropriately
    }
    return null;
  }

  public void deleteDriver(String driverLicense){
    String procedureCall = "{CALL delete_driver_account(?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)){
         stmt.setString(1,driverLicense);
         stmt.execute();
    } catch (SQLException e){
      e.printStackTrace();
    }
  }


  public List<Driver> getAllDrivers() {
    // SQL query or stored procedure call to get all drivers
    String procedureCall = "{CALL get_drivers}";
    List<Driver> res = new ArrayList<>();
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)){
         try (ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
              res.add(mapRowToDriver(rs));
            }
            return res;
         }
    } catch (SQLException e){
      e.printStackTrace();

    }
    return null;

  }


  public void deleteDriver(int id) {
    // SQL query or stored procedure call to delete a driver

  }

  private Driver mapRowToDriver(ResultSet rs) throws SQLException {
    Driver driver = new Driver();
    driver.setDriverLicense(rs.getString("driver_license"));
    driver.setName(rs.getString("name"));
    driver.setGender(rs.getString("gender"));
    driver.setBirthDate(rs.getDate("birth_date"));
    driver.setAddress(rs.getString("address"));
    driver.setAvailable(rs.getBoolean("is_available"));
    return driver;
  }
}
