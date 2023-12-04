package org.example.model;

public class Car {
  private String plate;
  private int carModelId;
  private int carCapacity;
  private String color;
  private boolean accessibility;
  private String driverLicense;

  public Car(String plate, int carModelId, int carCapacity, String color, boolean accessibility, String driverLicense) {
    this.plate = plate;
    this.carModelId = carModelId;
    this.carCapacity = carCapacity;
    this.color = color;
    this.accessibility = accessibility;
    this.driverLicense = driverLicense;
  }

  public Car() {
  }

  public String getPlate() {
    return plate;
  }

  public void setPlate(String plate) {
    this.plate = plate;
  }

  public int getCarModelId() {
    return carModelId;
  }

  public void setCarModelId(int carModelId) {
    this.carModelId = carModelId;
  }

  public int getCarCapacity() {
    return carCapacity;
  }

  public void setCarCapacity(int carCapacity) {
    this.carCapacity = carCapacity;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public boolean isAccessibility() {
    return accessibility;
  }

  public void setAccessibility(boolean accessibility) {
    this.accessibility = accessibility;
  }

  public String getDriverLicense(){
    return driverLicense;
  }
  public void setDriverLicense(String driverLicense){
    this.driverLicense = driverLicense;
  }

  @Override
  public String toString() {
    return "Car{" +
        "plate='" + plate + '\'' +
        ", carModelId=" + carModelId +
        ", carCapacity=" + carCapacity +
        ", color='" + color + '\'' +
        ", accessibility=" + accessibility +
        ", driverLicense =" + driverLicense +
        '}';
  }
}
