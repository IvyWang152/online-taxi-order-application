package org.example.model;

import java.util.Date;

public class Driver {

  private String driverLicense;

  private String name;

  private String gender;

  private Date birthDate;

  private String address;

  private boolean isAvailable = true;

  // Default constructor
  public Driver() {
  }

  // Constructor with parameters
  public Driver(String driverLicense, String name, String gender, Date birthDate, String address) {
    this.driverLicense = driverLicense;
    this.name = name;
    this.gender = gender;
    this.birthDate = birthDate;
    this.address = address;
  }

  public Driver(String driverLicense, String name, String gender, Date birthDate, String address, boolean is_available) {
    this.driverLicense = driverLicense;
    this.name = name;
    this.gender = gender;
    this.birthDate = birthDate;
    this.address = address;
    this.isAvailable = is_available;
  }

  public void setAvailable(boolean isAvailable){
    this.isAvailable = isAvailable;
  }

  public boolean getAvailable(){
    return this.isAvailable;
  }


  public void setDriverLicense(String driverLicense) {
    this.driverLicense = driverLicense;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDriverLicense() {
    return driverLicense;
  }

  public String getName() {
    return name;
  }

  public String getGender() {
    return gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public String toString() {
    return "Driver{" +
        "driver_license='" + driverLicense + '\'' +
        ", name='" + name + '\'' +
        ", gender='" + gender + '\'' +
        ", birth_date=" + birthDate + '\''+
        ", address='" + address + '\'' +
        ", is available='" + isAvailable + '\'' +
        '}';
  }
}
