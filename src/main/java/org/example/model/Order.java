package org.example.model;

import java.sql.Date;

public class Order {
  private int id;
  private Date orderDate;
  private String accountNumber;
  private int desiredCapacity;
  private Boolean accessibility;
  private String startCity;
  private String endCity;
  private int driverReview;
  private int passengerReview;
  private String orderStatus;

  private String carPlate;
  private double fare;


  public Order(Date orderDate, int desiredCapacity, Boolean accessibility,
               String startCity, String endCity, String accountNumber) {
    this.orderDate = orderDate;
    this.desiredCapacity = desiredCapacity;
    this.accessibility = accessibility;
    this.startCity = startCity;
    this.endCity = endCity;
    this.accountNumber = accountNumber;
  }

  public Order() {

  }

  // Getters and setters for each attribute
  public int getId() {
    return id;
  }

  public void setId(int id){
    this.id = id;
  }
  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public int getDesiredCapacity() {
    return desiredCapacity;
  }

  public void setDesiredCapacity(int desiredCapacity) {
    this.desiredCapacity = desiredCapacity;
  }

  public boolean getAccessibility() {
    return accessibility;
  }

  public void setAccessibility(boolean accessibility) {
    this.accessibility = accessibility;
  }


  public int getDriverReview() {
    return driverReview;
  }

  public void setDriverReview(int driverReview) {
    this.driverReview = driverReview;
  }

  public int getPassengerReview() {
    return passengerReview;
  }

  public void setPassengerReview(int passengerReview) {
    this.passengerReview = passengerReview;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getAccountNumber() {
    return accountNumber;
  }
  public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}

  public String getStartCity() {
    return startCity;
  }

  public String getEndCity() {
    return endCity;
  }

  public void setStartCity(String startCity) {
    this.startCity = startCity;
  }

  public void setEndCity(String endCity) {
    this.endCity = endCity;
  }

  public double getFare() {
    return fare;
  }

  public void setFare(double fare) {
    this.fare = fare;
  }

  public String getCarPlate() {
    return carPlate;
  }

  public void setCarPlate(String carPlate) {
    this.carPlate = carPlate;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", orderDate=" + orderDate +
        ", accountNumber='" + accountNumber + '\'' +
        ", desiredCapacity=" + desiredCapacity +
        ", accessibility=" + accessibility +
        ", startCity='" + startCity + '\'' +
        ", endCity='" + endCity + '\'' +
        ", driverReview=" + driverReview +
        ", passengerReview=" + passengerReview +
        ", orderStatus='" + orderStatus + '\'' +
        ", carPlate='" + carPlate + '\'' +
        ", fare=" + fare +
        '}';
  }
}
