package org.example.model;

import java.sql.Date;

public class Order {
  private String startCity;
  private String endCity;
  private int id;
  private Date orderDate;
  private int desiredCapacity;
  private Boolean accessibility;
  private String farePolicy;
  private int driverReview;
  private int passengerReview;
  private String orderStatus;
  private String policyName;
  private String accountNumber;

  public Order(Date orderDate, int desiredCapacity, Boolean accessibility, String farePolicy,
               String startCity, String endCity, String accountNumber) {
    this.orderDate = orderDate;
    this.desiredCapacity = desiredCapacity;
    this.accessibility = accessibility;
    this.farePolicy = farePolicy;
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

  public String getFarePolicy() {
    return farePolicy;
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

  public String getPolicyName() {
    return policyName;
  }

  public String getStartCity() {
    return startCity;
  }

  public String getEndCity() {
    return endCity;
  }

  public void setFarePolicy(String farePolicy) {
    this.farePolicy = farePolicy;
  }

  @Override
  public String toString() {
    return "Order{" +
            "orderDate=" + orderDate +
            ", desiredCapacity=" + desiredCapacity +
            ", accessibility='" + accessibility + '\'' +
            ", fare policy=" + farePolicy +
            ", driverReview=" + driverReview +
            ", passengerReview=" + passengerReview +
            ", orderStatus='" + orderStatus + '\'' +
            '}';
  }
}
