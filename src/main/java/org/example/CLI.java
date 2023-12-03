package org.example;

import java.util.Date;
import java.util.InputMismatchException;
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

  boolean isAuthenticatedDriver = false;
  Driver currentDriver = null;

  public void showDriverMenu() {
    boolean exit = false;
    while(!exit) {
      System.out.println("***** Welcome to Driver Dashboard! *****");
      if(!isAuthenticatedDriver) {
        System.out.println("1. register");
        System.out.println("2. log in");
        System.out.println("11. exit");
      } else{
        System.out.println("3. view profile");
        System.out.println("4. update profile");
        System.out.println("5. delete account");
        System.out.println("6. view all drivers");
        System.out.println("10. log out");
      }

      try {
        System.out.print("Enter command number: ");
        String choice = scanner.nextLine().toLowerCase();
        switch(choice) {
          case "1", "register":
            addNewDriver();
            break;
          case "2", "log in":
            loginDriver();
            break;
          case "3", "view profile":
            showDriverProfile();
            break;
          case "4", "update profile":
            updateDriverProfile();
            break;
          case "5", "delete account":
            deleteDriverAccount();
            break;
          case "6", "view all drivers":
            showAllDrivers();
            break;
          case "10", "log out":
            logoutDriver();
            break;
          case "11","exit":
            exit = closingPrompt();
            break;
          default:
            System.out.println("Invalid choice. Please enter a valid value.");
            break;
        }
//        if(choice=="11" || choice.equalsIgnoreCase("exit")){
//          exit = closingPrompt();
//        }
       // handleDriverOperation(choice);
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number.");
        //scanner.nextLine(); // Consume the invalid input
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

  public void loginDriver() {
    System.out.println("Enter driver license: ");
    String driverLicense = scanner.nextLine().trim();
    currentDriver = driverDao.getDriver(driverLicense);
    if(currentDriver==null){
      System.out.println("This account doesn't exist. Please register first or type right driver license");
      return;
    }
    isAuthenticatedDriver = true;


  }

  public void logoutDriver() {

    if(!isAuthenticatedDriver || currentDriver==null){
      System.out.println("Please register first or log in first");
      return;
    }
    isAuthenticatedDriver = false;
    currentDriver = null;
    System.out.println("You've successfully logged out!");

  }

  public void showDriverProfile() {
    if(!isAuthenticatedDriver || currentDriver==null){
      System.out.println("Please register or login first");
      return;
    }
    System.out.println(currentDriver.toString());
  }

  public void updateDriverProfile(){
    if(!isAuthenticatedDriver || currentDriver==null){
      System.out.println("Please register or login first");
      return;
    }
    String driverLicense = currentDriver.getDriverLicense();
    String name = currentDriver.getName();
    String gender = currentDriver.getGender();
    Date birthDate = currentDriver.getBirthDate();
    String address = currentDriver.getAddress();
    boolean isAvailable = currentDriver.getAvailable();

    System.out.println("Edit name: (Press Enter to skip)");
    String tmp = scanner.nextLine().trim();
    if(!tmp.isEmpty()){
      name = tmp;
    }

    System.out.println("Edit gender (female/male): (Press Enter to skip) ");
    tmp = scanner.nextLine().trim();
    if(!tmp.isEmpty()){
      gender = tmp;
    }

    System.out.println("Edit birth date (yy-mm-dd): (Press Enter to skip)");
    tmp = scanner.nextLine().trim();
    if(!tmp.isEmpty()){
      try {
        birthDate = java.sql.Date.valueOf(tmp);
      } catch (IllegalArgumentException e) {
        System.out.println("Invalid date format. Please use yy-mm-dd format.");
        return; // Exit the method if invalid date is entered
      }
    }

    System.out.println("Edit address: (Press Enter to skip)");
    tmp = scanner.nextLine().trim();
    if(!tmp.isEmpty()){
      address = tmp;
    }

    System.out.println("Edit availability (true or false): (Press Enter to skip)");
    tmp = scanner.nextLine().trim();
    if(!tmp.isEmpty()){
      isAvailable = Boolean.parseBoolean(tmp);
    }

    currentDriver = driverDao.updateDriver(new Driver(driverLicense,name,gender,birthDate,address,isAvailable));
    System.out.println("Update driver profile successfully!");
    System.out.println(currentDriver.toString());
  }

  public void deleteDriverAccount(){
    if(!isAuthenticatedDriver || currentDriver == null){
      System.out.println("Only a registered member can delete their account when they log in");
      return;
    }
    driverDao.deleteDriver(currentDriver.getDriverLicense());
    isAuthenticatedDriver = false;
    currentDriver=null;
    System.out.println("Driver account successfully deleted!");

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
