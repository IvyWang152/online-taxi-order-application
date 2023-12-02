package org.example;

import java.util.List;
import java.util.Scanner;
import org.example.dao.DriverDao;
import org.example.model.Driver;

public class CLI {
  private final Scanner scanner;
  private DriverDao driverDao;

  public CLI() {
    this.scanner = new Scanner(System.in);
    this.driverDao = new DriverDao();
  }

  public void start() {
    //we can start as "choose to be a driver or passenger"
    // then provide matching operation prompts
    System.out.println("***** Welcome to Taxi Order Dashboard! *****");
    System.out.println("To get started, please choose your role (1 or 2)");
    System.out.println("1. driver");
    System.out.println("2. passenger");

    System.out.print("Enter your choice: ");
    int choice = scanner.nextInt();
    switch(choice){
      case 1:
        showDriverMenu();
        break;
      case 2:
        showPassengerMenu();
        break;
    }
  }

  public void showDriverMenu() {
    while(true) {
      System.out.println("***** Welcome to Driver Dashboard! *****");
      System.out.println("1. Add new driver");
      System.out.println("2. View drivers");
      // add more operations later
      System.out.print("Enter command number: ");
      int choice = scanner.nextInt();
      scanner.nextLine(); // Consume the newline character
      handleDriverOperation(choice);
    }
  }

  public int showPassengerMenu() {
    System.out.println("***** Welcome to Passenger Dashboard! *****");
    System.out.println("1. Add new passenger");
    System.out.println("2. View passenger profile");
    System.out.print("Enter your choice: ");
    return scanner.nextInt();
  }


  public void handleDriverOperation(int choice) {
    switch(choice){
      case 1:
        addNewDriver();
      case 2:
        showAllDrivers();
      default:
        break;
    }
  }

  //command line prompts for creating a driver
  public void addNewDriver(){
    System.out.println("Enter driver license: ");
    String driverLicense = scanner.nextLine().trim();

    System.out.println("Enter name: ");
    String name = scanner.nextLine();

    System.out.println("Enter gender (female/male): ");
    String gender = scanner.nextLine();

    System.out.println("Enter birth date (yy-mm-dd): ");
    String birth_date = scanner.nextLine();
    java.sql.Date birthDate = java.sql.Date.valueOf(birth_date);

    System.out.println("Enter address: ");
    String address = scanner.nextLine();

    Driver newDriver = new Driver(driverLicense,name,gender,birthDate,address);
    try {
      driverDao.addDriver(newDriver);
      System.out.println("New driver created successfully!");
    } catch (Exception e) {
      System.out.println("Error creating driver: " + e.getMessage());
    }
  }

  public void showAllDrivers(){
    List<Driver> drivers = driverDao.getAllDrivers();
    if(drivers.isEmpty()){
      System.out.println("No drivers found!");
    } else {
      for(Driver each: drivers){
        System.out.println(each.toString());
      }
    }

  }








  // Methods to handle input for other operations
  // e.g., public Driver getNewDriverDetails() {...}

  // Method to close the scanner
  public void close() {
    scanner.close();
  }
}
