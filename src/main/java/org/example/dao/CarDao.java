package org.example.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.DBConnector;
import org.example.model.Car;
import org.example.model.CarModel;
import org.example.model.Driver;

public class CarDao {
  public void addCar(Car car){
    String procedureCall = "{CALL create_car(?, ?, ?, ?, ?,?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, car.getPlate());
      stmt.setInt(2, car.getCarModelId());
      stmt.setInt(3, car.getCarCapacity());
      stmt.setString(4,car.getColor());
      stmt.setBoolean(5, car.isAccessibility());
      stmt.setString(6,car.getDriverLicense());
      stmt.execute();
      System.out.println("New car created successfully!");
    } catch (SQLException e) {
      if (e.getMessage().contains("FOREIGN KEY (`car_model_id`)")) {
        // Handle duplicate entry
        System.out.println("Error: The specified car model does not exist. Please view existing models");
     } else {
      e.printStackTrace(); // Handle other SQL exceptions
    }
    }

  }

  public void addCarModel(String model, String make) {
    String procedureCall = "{CALL add_car_model(?, ?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, model);
      stmt.setString(2, make);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace(); // Handle exception
    }
  }

  public List<CarModel> getCarModels() {
    List<CarModel> models = new ArrayList<>();
    String procedureCall = "{CALL get_car_models}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall);
         ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        models.add(new CarModel(rs.getInt("car_model_id"), rs.getString("model"), rs.getString("make")));
      }
    } catch (SQLException e) {
      e.printStackTrace(); // Handle exception
    }
    return models;
  }




  public List<Car> getCarsOfDriver(String driverLicense){
    List<Car> cars = new ArrayList<>();
    String procedureCall = "{CALL get_car_of_driver(?)}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      stmt.setString(1, driverLicense);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          // Map the result set to Car objects and add them to the list
          cars.add(mapRowToCar(rs));
        }
      }
    } catch (SQLException e) {
      if (e.getSQLState().equals("45000")) {
        // Handle the custom error message
        System.out.println("No car registered under this driver account.");
      } else {
        e.printStackTrace(); // Handle other SQL exceptions
      }
    }
    return cars;

  }

  public List<Car> getCars(){
    List<Car> cars = new ArrayList<>();
    String procedureCall = "{CALL get_cars()}";
    try (Connection conn = DBConnector.getConnection();
         CallableStatement stmt = conn.prepareCall(procedureCall)) {

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          // Map the result set to Car objects and add them to the list
          cars.add(mapRowToCar(rs));
        }
      }
    } catch (SQLException e) {
        e.printStackTrace(); // Handle other SQL exceptions
    }
    return cars;

  }



  Car mapRowToCar(ResultSet rs) throws SQLException {
    Car car = new Car();
    car.setPlate(rs.getString("plate"));
    car.setCarModelId(rs.getInt("car_model_id"));
    car.setCarCapacity(rs.getInt("car_capacity"));
    car.setColor(rs.getString("color"));
    car.setAccessibility(rs.getBoolean("accessibility"));
    car.setDriverLicense(rs.getString("driver_license"));
    return car;
  }
}
