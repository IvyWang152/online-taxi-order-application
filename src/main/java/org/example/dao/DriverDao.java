package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    // SQL query or stored procedure call to get a driver by ID

  }

  public List<Driver> getAllDrivers() {
    // SQL query or stored procedure call to get all drivers

  }

  public void updateDriver(Driver driver) {
    // SQL query or stored procedure call to update a driver
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
    return driver;
  }
}
