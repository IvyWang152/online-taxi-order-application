package org.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import org.example.dao.DriverDao;
import org.example.dao.PassengerDao;
import org.example.model.Driver;
import org.example.model.Passenger;

public class CLI {
  private final Scanner scanner;
  private final DriverDao driverDao;
  private final PassengerDao passengerDao;

  public CLI() {
    this.scanner = new Scanner(System.in);
    this.driverDao = new DriverDao();
    this.passengerDao = new PassengerDao();
  }

  public void start() throws Exception {
    System.out.println("***** Welcome to Taxi Order Dashboard! *****");
    System.out.println("To get started, please choose your role (1 or 2)");
    System.out.println("1. driver");
    System.out.println("2. passenger");
    System.out.println("3. Exit");

    boolean validInput = false;

    do {
      System.out.print("Enter your choice number or role name: ");
      String userChoice = scanner.next().toLowerCase().trim();
      scanner.nextLine();

      switch (userChoice) {
        case "1", "driver" -> {
          showDriverMenu();
          validInput = true;
        }
        case "2", "passenger" -> {
          showPassengerMenu();
          validInput = true;
        }
        case "3", "exit" -> {
          if (closingPrompt()) {
            validInput = true;
          }
        }
        default ->
                System.out.println("Invalid choice or role name. Please enter '1', '2', 'driver', or 'passenger'.");

        // Loop continues to prompt the user until a valid input is provided
      }
    } while (!validInput);
  }

  public void showDriverMenu() {
    boolean exit = false;

    while (!exit) {
      System.out.println("***** Welcome to Driver Dashboard! *****");
      System.out.println("1. Add a new driver");
      System.out.println("2. View drivers");
      System.out.println("3. Exit");
      System.out.print("Enter command number: ");

      try {
        String choice = scanner.next().toLowerCase().trim();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
          case "1", "add a new driver" -> addNewDriver();
          case "2", "view drivers" -> showAllDrivers();
          case "3", "exit" -> exit = closingPrompt();
          default -> System.out.println("Invalid choice. Please enter a valid value.");
        }
      } catch (Exception e) {
        System.out.println("Invalid input. Please enter a valid value.");
        scanner.nextLine(); // Consume the invalid input
      }
    }
  }


  public void showPassengerMenu() {
    boolean exit = false;

    while (!exit) {
      System.out.println("***** Welcome to Passenger Dashboard! *****");
      System.out.println("1. Add a new passenger");
      System.out.println("2. View passengers");
      System.out.println("3. Exit");
      System.out.print("Enter command number: ");

      try {
        String choice = scanner.next().toLowerCase().trim();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
          case "1", "add a new passenger" -> addNewPassenger();
          case "2", "view passengers" -> showAllPassengers();
          case "3", "exit" -> exit = closingPrompt();
          default -> System.out.println("Invalid choice. Please enter a valid value.");
        }
      } catch (Exception e) {
        System.out.println("Invalid input. Please enter a valid value.");
        scanner.nextLine(); // Consume the invalid input
      }
    }
  }



  public void handleDriverOperation(String  choice) throws Exception {
    switch (choice) {
      case "1", "Add a new driver" -> addNewDriver();
      case "2", "View drivers" -> showAllDrivers();
      case "3", "Exit" -> closingPrompt();
      default -> System.out.println("Invalid choice. Please enter a valid value.");
    }
  }
  public void handlePassengerOperation(String choice) throws Exception {
    switch (choice) {
      case "1", "Add a new passenger" -> addNewPassenger();
      case "2", "View passengers" -> showAllPassengers();
      case "3", "Exit" -> closingPrompt();
      default -> System.out.println("Invalid choice. Please enter a value.");
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

  //command line prompts for creating a passenger
  public void addNewPassenger(){
    System.out.println("Enter the account_number: ");
    String accountNumber = scanner.nextLine().trim();

    System.out.println("Enter name: ");
    String name = scanner.nextLine();

    System.out.println("Enter gender (female/male): ");
    String gender = scanner.nextLine();

    System.out.println("Enter birth date (yy-mm-dd): ");
    String birth_date = scanner.nextLine();
    java.sql.Date birthDate = java.sql.Date.valueOf(birth_date);

    Passenger newPassenger = new Passenger(accountNumber,name,gender,birthDate);
    try {
      passengerDao.addPassenger(newPassenger);
      System.out.println("New passenger created successfully!");
    } catch (Exception e) {
      System.out.println("Error creating passenger: " + e.getMessage());
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
  public void showAllPassengers() throws SQLException {
    List<Passenger> passengers = passengerDao.getPassengers();
    if(passengers.isEmpty()){
      System.out.println("No passengers found!");
    } else {
      for(Passenger each: passengers){
        System.out.println(each.toString());
      }
    }
  }

  // Methods to handle input for other operations
  // e.g., public Driver getNewDriverDetails() {...}

  // Method to close the scanner
  public boolean closingPrompt() {
    System.out.println("Close dashboard? (y/n)");
    System.out.print("Enter your choice: ");

    String yesOption = "y";
    String noOption = "n";

    String res = scanner.nextLine().trim().toLowerCase();

    while (!res.equals(yesOption) && !res.equals(noOption)) {
      System.out.println("Invalid choice. Please enter 'y' or 'n'.");
      System.out.print("Enter your choice: ");
      res = scanner.nextLine().trim().toLowerCase();
    }

    return res.equals(yesOption);
  }

}
