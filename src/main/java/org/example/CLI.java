package org.example;

import java.time.LocalDate;
import java.util.Date;
import java.util.InputMismatchException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import org.example.dao.CarDao;
import org.example.dao.DriverDao;
import org.example.dao.OrderDao;
import org.example.dao.PassengerDao;
import org.example.model.Car;
import org.example.model.CarModel;
import org.example.model.CommuteDistance;
import org.example.model.Driver;
import org.example.model.Order;
import org.example.model.Passenger;

public class CLI {
  private final Scanner scanner;
  private final DriverDao driverDao;
  private final PassengerDao passengerDao;
  private final CarDao carDao;
  private final OrderDao orderDao;

  public CLI() {
    this.scanner = new Scanner(System.in);
    this.driverDao = new DriverDao();
    this.passengerDao = new PassengerDao();
    this.carDao = new CarDao();
    this.orderDao = new OrderDao();

  }

  public void start() throws Exception {
    System.out.println("***** Welcome to Taxi Order Dashboard! *****");
    System.out.println("To get started, please choose your role!");
    System.out.println("1. driver");
    System.out.println("2. passenger");
    System.out.println("3. Exit");

    boolean validInput = false;

    do {
      System.out.print("Enter your choice number or role name: ");
      String userChoice = scanner.nextLine().toLowerCase().trim();
      //scanner.nextLine();

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
        System.out.println("7. add car");
        System.out.println("8. view car");
        System.out.println("9. view car models");
        System.out.println("10. log out");
      }

      try {
        System.out.print("Enter command number: ");
        String choice = scanner.nextLine().toLowerCase();
        switch (choice) {
          case "1", "register" -> addNewDriver();
          case "2", "log in" -> loginDriver();
          case "3", "view profile" -> showDriverProfile();
          case "4", "update profile" -> updateDriverProfile();
          case "5", "delete account" -> deleteDriverAccount();
          case "6", "view all drivers" -> showAllDrivers();
          case "7", "add car" -> addNewCar();
          case "8", "view car" -> showCar();
          case "9", "view car models" -> showCarModels();
          case "10", "log out" -> logoutDriver();
          case "11", "exit" -> exit = closingPrompt();
          default -> System.out.println("Invalid choice. Please enter a valid value.");
        }

      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number.");
      }
    }
  }
  boolean isAuthenticatedPassenger = false;
  Passenger currentPassenger = null;
  public void showPassengerMenu() {
    boolean exit = false;

    while (!exit) {
      if(!isAuthenticatedPassenger) {
        System.out.println("***** Welcome to Passenger Dashboard! *****");
        System.out.println("1. Register");
        System.out.println("2. log in");
        System.out.println("3. Exit");
      } else{
        System.out.println("***** Hi " + currentPassenger.getName() + "! *****");
        System.out.println("What do you want to do today?");
        System.out.println("4. View profile");
        System.out.println("5. Update profile");
        System.out.println("6. Delete account");
        System.out.println("7. View all passengers");
        System.out.println("8. View your orders!");
        System.out.println("9. Create an order (Please check the available routes first (11. view routes)");
        System.out.println("10. log out");
        System.out.println("11. view routes");
        System.out.println("12. Update orders");
      }
      try {
        System.out.print("Enter command number: ");
        String choice = scanner.nextLine().toLowerCase();
        switch (choice) {
          case "1", "register" -> addNewPassenger();
          case "2", "log in" -> loginPassenger();
          case "4", "view profile" -> showPassengerProfile();
          case "5", "update profile" -> updatePassengerProfile();
          case "6", "delete account" -> deletePassengerAccount();
          case "7", "view all passengers" -> showAllPassengers();
          case "8", "view your orders" -> viewOrders();
          case "9", "create an order" -> createOrder();
          case "10", "log out" -> logoutPassenger();
          case "3", "exit" -> exit = closingPrompt();
          case "11","view routes"-> showRoutes();
          case "12", "update orders" -> updateOrder();
          default -> System.out.println("Invalid choice. Please enter a valid value.");
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number.");
      } catch (SQLException e) {
        throw new RuntimeException(e);
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
    String driverLicense;
    while (true) {
      System.out.println("Enter driver license: ");
      driverLicense = scanner.nextLine().trim();
     if (!driverLicense.isEmpty()){
       break;
     } else {
       System.out.println("Driver license cannot be empty");
     }
    }

    String name;
    while (true) {
      System.out.println("Enter name: ");
      name = scanner.nextLine().trim();
      if (!name.isEmpty()){
        break;
      } else {
        System.out.println("Name cannot be empty");
      }
    }

    String gender;
    while (true) {
      System.out.println("Enter gender: ");
      gender = scanner.nextLine().trim();
      if (!gender.isEmpty()){
        break;
      } else {
        System.out.println("Gender cannot be empty");
      }
    }
    String tmp;
    java.sql.Date birthDate;
    while (true) {
      System.out.println("Edit birth date (yy-mm-dd):");
      tmp = scanner.nextLine().trim();
      if (!tmp.isEmpty()){
        try {
          birthDate = java.sql.Date.valueOf(tmp);
          break;
        } catch (IllegalArgumentException e) {
          System.out.println("Invalid date format. Please use yy-mm-dd format.");
        }
      } else {
        System.out.println("Birthdate cannot be empty");
      }
    }
    String address;
    while (true) {
      System.out.println("Enter address: ");
      address = scanner.nextLine().trim();
      if (!address.isEmpty()){
        break;
      } else {
        System.out.println("Address cannot be empty");
      }
    }


    Driver newDriver = new Driver(driverLicense,name,gender,birthDate,address);
    try {
      driverDao.addDriver(newDriver);
    } catch (Exception e) {
      System.out.println("Error creating driver: " + e.getMessage());
    }
  }

  //command line prompts for creating a passenger
  public void addNewPassenger() {
    try {
      System.out.println("Enter the account_number: ");
      String accountNumber = scanner.nextLine().trim();

      // Validate account number
      if (accountNumber.isEmpty()) {
        throw new IllegalArgumentException("Account number cannot be empty");
      }

      System.out.println("Enter name: ");
      String name = scanner.nextLine();

      // Validate name
      if (name.isEmpty()) {
        throw new IllegalArgumentException("Name cannot be empty");
      }

      System.out.println("Enter gender ('male', 'female', 'other', or 'custom'): ");
      String gender = scanner.nextLine().toLowerCase();

      // Validate gender
      if (!isValidGender(gender)) {
        throw new IllegalArgumentException("Invalid gender. Please enter 'male', 'female', 'other', or 'custom'");
      }

      // If custom gender is chosen, allow the user to input their own value
      if (gender.equals("custom")) {
        System.out.println("Enter custom gender: ");
        gender = scanner.nextLine();
      }

      System.out.println("Enter birth date (yy-mm-dd): ");
      String birthDateStr = scanner.nextLine();

      // Validate and parse birthdate
      java.sql.Date birthDate = null;
      try {
        birthDate = java.sql.Date.valueOf(birthDateStr);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid date format. Please use 'yy-mm-dd'");
      }

      Passenger newPassenger = new Passenger(accountNumber, name, gender, birthDate);
      passengerDao.addPassenger(newPassenger);
      System.out.println("New passenger created successfully!");
    } catch (Exception e) {
      System.out.println("Error creating passenger: " + e.getMessage());
    }
  }

  private boolean isValidGender(String gender) {
    // Add more accepted gender values as needed
    return gender.equals("male") || gender.equals("female") || gender.equals("other") || gender.equals("custom");
  }

  public void loginDriver() {
    System.out.println("Enter your driver's license: ");
    String driverLicense = scanner.nextLine().trim();
    currentDriver = driverDao.getDriver(driverLicense);
    if(currentDriver==null){
      System.out.println("This account doesn't exist. Please register first or type right driver's license");
      return;
    }
    isAuthenticatedDriver = true;

  }
  public void loginPassenger() {
    System.out.println("Enter your account number: ");
    String accountNumber = scanner.nextLine().trim();
    currentPassenger = passengerDao.getPassenger(accountNumber);
    if(currentPassenger==null){
      System.out.println("This account doesn't exist. Please register first or type right account number!");
      return;
    }
    isAuthenticatedPassenger = true;

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

  public void logoutPassenger() {

    if(!isAuthenticatedPassenger || currentPassenger==null){
      System.out.println("Please register first or log in first");
      return;
    }
    isAuthenticatedPassenger = false;
    currentPassenger = null;
    System.out.println("You've successfully logged out!");

  }

  public void showDriverProfile() {
    if(!isAuthenticatedDriver || currentDriver==null){
      System.out.println("Please register or login first");
      return;
    }
    System.out.println(currentDriver);
  }

  public void showPassengerProfile() {
    if(!isAuthenticatedPassenger || currentPassenger==null){
      System.out.println("Please register or login first");
      return;
    }
    System.out.println(currentPassenger.toString());
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

    System.out.println("Edit gender: (Press Enter to skip)");
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

    while (true) {
      System.out.println("Edit availability (true or false): (Press Enter to skip)");
      String availability = scanner.nextLine().trim().toLowerCase();
      if(availability.isEmpty()){
        break;
      }
      if (availability.equals("true") || availability.equals("false")) {
        isAvailable = Boolean.parseBoolean(availability);
        break; // Break out of the loop if input is "true" or "false"
      } else {
        System.out.println("Invalid input. Please enter true or false for accessibility.");
      }
    }

    currentDriver = driverDao.updateDriver(new Driver(driverLicense,name,gender,birthDate,address,isAvailable));
    System.out.println(currentDriver.toString());
  }

  public void updatePassengerProfile(){
    if(!isAuthenticatedPassenger || currentPassenger==null){
      System.out.println("Please register or login first");
      return;
    }
    String accountNumber = currentPassenger.getAccountNumber();
    String name = currentPassenger.getName();
    String gender = currentPassenger.getGender();
    Date birthDate = currentPassenger.getBirthDate();

    System.out.println("Edit name: (Press Enter to skip)");
    String tmp = scanner.nextLine().trim();
    if(!tmp.isEmpty()){
      name = tmp;
    }

    System.out.println("Edit gender: (Press Enter to skip) ");
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

    currentPassenger = passengerDao.updatePassenger(new Passenger(accountNumber,name,gender,birthDate));
    System.out.println("Update driver profile successfully!");
    System.out.println(currentPassenger.toString());
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

  public void deletePassengerAccount(){
    if(!isAuthenticatedPassenger || currentPassenger == null){
      System.out.println("Only a registered member can delete their account when they log in");
      return;
    }
    passengerDao.deletePassenger(currentPassenger.getAccountNumber());
    isAuthenticatedPassenger = false;
    currentPassenger=null;
    System.out.println("Passenger account successfully deleted!");

  }

  //Car logic
  public void addNewCar(){
    if(!isAuthenticatedDriver || currentDriver==null){
      System.out.println("Please register or login first");
      return;
    }
    System.out.println("Enter car plate: ");
    String plate = scanner.nextLine().trim();

    int carModelId = 0;
    int carCapacity = 0;
    boolean accessibility;

    // Handling car model id input
    while (true) {
      System.out.println("Enter car model id: ");
      String carModelIdS = scanner.nextLine().trim();
      try {
        carModelId = Integer.valueOf(carModelIdS);
        break; // Break out of the loop if conversion is successful
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter an integer for car model id.");
      }
    }

    // Handling car capacity input
    while (true) {
      System.out.println("Enter car capacity: ");
      String carCapacityS = scanner.nextLine().trim();
      try {
        carCapacity = Integer.parseInt(carCapacityS);
        break; // Break out of the loop if conversion is successful
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter an integer for car capacity.");
      }
    }

    System.out.println("Enter car color: ");
    String color = scanner.nextLine().trim();

    while (true) {
      System.out.println("Enter car accessibility (true or false): ");
      String accessibilityS = scanner.nextLine().trim().toLowerCase();

      if (accessibilityS.equals("true") || accessibilityS.equals("false")) {
        accessibility = Boolean.parseBoolean(accessibilityS);
        break; // Break out of the loop if input is "true" or "false"
      } else {
        System.out.println("Invalid input. Please enter true or false for accessibility.");
      }
    }
    Car car = new Car(plate,carModelId,carCapacity,color,accessibility,
        currentDriver.getDriverLicense());
    try {
      carDao.addCar(car);
    } catch (Exception e) {
      System.out.println("Error creating car: " + e.getMessage());
    }

  }

  public void showCar(){
    if(!isAuthenticatedDriver || currentDriver==null){
      System.out.println("Please register or login first");
      return;
    }
    List<Car> cars = carDao.getCarsOfDriver(currentDriver.getDriverLicense());
    for(Car each: cars){
      System.out.println(each.toString());
    }
  }

  public void showCarModels(){
    if(!isAuthenticatedDriver || currentDriver==null) {
      System.out.println("Please register or login first");
      return;
    }
    List<CarModel> models = carDao.getCarModels();
    for(CarModel model: models){
      System.out.println(model.toString());
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
    List<Passenger> passengers = passengerDao.getAllPassengers();
    if(passengers.isEmpty()){
      System.out.println("No passengers found!");
    } else {
      for(Passenger each: passengers){
        System.out.println(each.toString());
      }
    }
  }

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
  public void viewOrders() {
    if (!isAuthenticatedPassenger || currentPassenger == null) {
      System.out.println("Please register or log in first");
      return;
    }

    //List<Order> orders = passengerDao.getOrdersForPassenger(currentPassenger.getAccountNumber());
    List<Order> orders = orderDao.getOrdersForPassenger(currentPassenger.getAccountNumber());
    if (orders.isEmpty()) {
      System.out.println("No orders found for this passenger.");
    } else {
      for (Order order : orders) {
        System.out.println(order.toString());
      }
    }
  }

  public void showRoutes(){
    if (!isAuthenticatedPassenger || currentPassenger == null) {
      System.out.println("Please register or log in first");
      return;
    }
    List<CommuteDistance> res = orderDao.viewRoutes();
    for(CommuteDistance each: res){
      System.out.println(each.toString());
    }
  }

  public void createOrder() {
    if (!isAuthenticatedPassenger || currentPassenger == null) {
      System.out.println("Please log in first");
      loginPassenger(); // Prompt the user to log in
      if (currentPassenger == null) {
        System.out.println("Login failed. Aborting order creation.");
        return;
      }
    }

    String accountNumber = currentPassenger.getAccountNumber();
    LocalDate currentDate = LocalDate.now(); // This gets the current date
    java.sql.Date orderDate = java.sql.Date.valueOf(currentDate); // Convert to sql.Date

    String startCity;
    do {
      System.out.println("Enter pick up city: ");
      startCity = scanner.nextLine().trim();
      if (startCity.isEmpty()) {
        System.out.println("Pick up name cannot be empty. Please enter a valid name.");
      }
    } while (startCity.isEmpty());

    String endCity;
    do {
      System.out.println("Enter drop off city: ");
      endCity = scanner.nextLine().trim();
      if (endCity.isEmpty()) {
        System.out.println("Drop off name cannot be empty. Please enter a valid name.");
      } else if (startCity.equalsIgnoreCase(endCity)) {
        System.out.println("Pick-up and drop-off cities cannot be the same. Please enter different cities.");
      }
    } while (endCity.isEmpty() || startCity.equalsIgnoreCase(endCity));

    int desiredCapacity;
    do {
      System.out.println("Enter desired capacity: ");
      try {
        desiredCapacity = Integer.parseInt(scanner.nextLine().trim());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter an integer for desired capacity.");
        desiredCapacity = -1; // Set an invalid value to continue the loop
      }
    } while (desiredCapacity < 0);

    boolean accessibility = false;
    String accessibilityInput;
    do {
      System.out.println("Enter accessibility (true or false): ");
      accessibilityInput = scanner.nextLine().trim().toLowerCase();
      if (accessibilityInput.equalsIgnoreCase("true") || accessibilityInput.equalsIgnoreCase("false")) {
        accessibility = Boolean.parseBoolean(accessibilityInput);
      } else {
        System.out.println("Invalid input. Please enter true or false for accessibility.");
      }
    } while (!accessibilityInput.equalsIgnoreCase("true") && !accessibilityInput.equalsIgnoreCase("false"));

    Order newOrder = new Order(orderDate, desiredCapacity, accessibility, startCity, endCity, accountNumber);
    //passengerDao.createOrder(newOrder);
    orderDao.createOrder(newOrder);
  }
  public void updateOrder() {
    if (!isAuthenticatedPassenger || currentPassenger == null) {
      System.out.println("Please log in first");
      loginPassenger(); // Prompt the user to log in
      if (currentPassenger == null) {
        System.out.println("Login failed. Aborting order update.");
        return;
      }
    }

    // Display passenger's orders
    //List<Order> orders = passengerDao.getOrdersForPassenger(currentPassenger.getAccountNumber());
    List<Order> orders = orderDao.getOrdersForPassenger(currentPassenger.getAccountNumber());
    if (orders.isEmpty()) {
      System.out.println("No orders found for this passenger.");
      return;
    } else {
      System.out.println("Your Orders:");
      for (Order order : orders) {
        System.out.println(order.toString());
      }
    }

    // Prompt passenger to choose an order to update
    System.out.println("Enter the Order ID you want to update: ");
    int orderId;
    try {
      orderId = Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a valid Order ID.");
      return;
    }

    // Check if the provided Order ID exists and is associated with the current passenger
    boolean isValidOrderId = orders.stream().anyMatch(order -> order.getId() == orderId);
    if (!isValidOrderId) {
      System.out.println("Invalid Order ID. Please enter a valid Order ID.");
      return;
    }

    // Prompt passenger to choose what to update
    System.out.println("Select what you want to update:");
    System.out.println("1. Update desired capacity");
    System.out.println("2. Update accessibility");
    System.out.println("3. Update start and end cities");
    System.out.println("4. Cancel update");

    String updateChoice = scanner.nextLine().trim();

    switch (updateChoice) {
      case "1" ->
        // Update desired capacity
              updateOrderCapacity(orderId);
      case "2" ->
        // Update accessibility
              updateOrderAccessibility(orderId);
      case "3" ->
        // Update start and end cities
              updateOrderRoute(orderId);
      case "4" ->
        // Cancel the update
              System.out.println("Update canceled.");
      default -> System.out.println("Invalid choice. Please enter a valid option.");
    }
  }

  private void updateOrderCapacity(int orderId) {
    System.out.println("Enter new desired capacity: ");
    int newCapacity;
    try {
      newCapacity = Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter an integer for desired capacity.");
      return;
    }
    orderDao.updateOrderCapacity(orderId, newCapacity);
    System.out.println("Desired capacity updated successfully.");
  }

  private void updateOrderAccessibility(int orderId) {
    System.out.println("Enter new accessibility (true or false): ");
    boolean newAccessibility;
    String accessibilityInput = scanner.nextLine().trim().toLowerCase();
    if (accessibilityInput.equalsIgnoreCase("true") || accessibilityInput.equalsIgnoreCase("false")) {
      newAccessibility = Boolean.parseBoolean(accessibilityInput);
      orderDao.updateOrderAccessibility(orderId, newAccessibility);
      System.out.println("Accessibility updated successfully.");
    } else {
      System.out.println("Invalid input. Please enter true or false for accessibility.");
    }
  }

  private void updateOrderRoute(int orderId) {
    System.out.println("Enter new start city: ");
    String newStartCity = scanner.nextLine().trim();

    System.out.println("Enter new end city: ");
    String newEndCity = scanner.nextLine().trim();

    orderDao.updateOrderRoute(orderId, newStartCity, newEndCity);
    System.out.println("Start and end cities updated successfully.");
  }
  public void deleteOrder() {
    if (!isAuthenticatedPassenger || currentPassenger == null) {
      System.out.println("Please register or log in first");
      return;
    }

    // Display passenger's orders
    //List<Order> orders = passengerDao.getOrdersForPassenger(currentPassenger.getAccountNumber());
    List<Order> orders = orderDao.getOrdersForPassenger(currentPassenger.getAccountNumber());
    if (orders.isEmpty()) {
      System.out.println("No orders found for this passenger.");
      return;
    } else {
      System.out.println("Your Orders:");
      for (Order order : orders) {
        System.out.println(order.toString());
      }
    }

    // Prompt passenger to choose an order to delete
    System.out.println("Enter the Order ID you want to delete: ");
    int orderId;
    try {
      orderId = Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
      System.out.println("Invalid input. Please enter a valid Order ID.");
      return;
    }

    // Check if the provided Order ID exists for the current passenger
    boolean orderExists = orders.stream().anyMatch(order -> order.getId() == orderId);
    if (!orderExists) {
      System.out.println("Invalid Order ID. Please enter a valid Order ID.");
      return;
    }

    // Confirm with the user before deleting
    System.out.println("Are you sure you want to delete this order? (yes/no)");
    String confirmation = scanner.nextLine().trim().toLowerCase();
    if (confirmation.equals("yes")) {
      // Delete the order
      //passengerDao.deleteOrder(orderId);
      orderDao.deleteOrder(orderId);
      System.out.println("Order deleted successfully.");
    } else {
      System.out.println("Deletion canceled.");
    }
  }



}
