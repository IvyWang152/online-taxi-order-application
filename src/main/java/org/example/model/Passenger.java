package org.example.model;

import java.util.Date;

public class Passenger {

  private String accountNumber;

  private String name;

  private String gender;


  private Date birthDate;

  public Passenger() {

  }
  public Passenger(String accountNumber, String name, String gender, Date birthDate) {
    this.accountNumber = accountNumber;
    this.name = name;
    this.gender = gender;
    this.birthDate = birthDate;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }
}
