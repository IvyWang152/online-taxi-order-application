package org.example;

import java.util.Date;
import java.util.InputMismatchException;
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

  boolean isAuthenticatedDriver = false;
  Driver currentDriver = null;

  public void showDriverMenu() {
    boolean ifContinued = true;
    while(ifContinued) {
      System.out.println("***** Welcome to Driver Dashboard! *****");
      if(!isAuthenticatedDriver) {
        System.out.println("1. Register as a new driver");
        System.out.println("2. Log in as a driver");
        System.out.println("11. Close driver dashboard");
      } else{
        System.out.println("3. View driver profile");
        System.out.println("4. Update driver profile");
        System.out.println("5. Delete driver account");
        System.out.println("6. View all drivers");
        System.out.println("10. log out");
      }

//      System.out.println("1. Add new driver");
//      System.out.println("2. View drivers");
//      System.out.println("3. View driver profile");
      // add more operations later
//      System.out.print("Enter command number: ");
//      int choice = scanner.nextInt();
//      scanner.nextLine(); // Consume the newline character
//      handleDriverOperation(choice);

      try {

        System.out.print("Enter command number: ");
        int choice = scanner.nextInt();
        if(choice==11){
          ifContinued = false;
        }
        scanner.nextLine(); // Consume the newline character
        handleDriverOperation(choice);
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number.");
        scanner.nextLine(); // Consume the invalid input
      }
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
        break;
      case 2:
        loginDriver();
        break;
      case 3:
        showDriverProfile();
        break;
      case 4:
        updateDriverProfile();
        break;
      case 5:
        deleteDriverAccount();
        break;
      case 6:
        showAllDrivers();
        break;
      case 10:
        logoutDriver();
        break;
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
