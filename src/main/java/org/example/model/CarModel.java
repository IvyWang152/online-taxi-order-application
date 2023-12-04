package org.example.model;

public class CarModel {
  private String model;
  private String make;

  private int modelId;

  public CarModel() {
  }

  public CarModel(int modelId,String model, String make) {
    this.model = model;
    this.make = make;
    this.modelId = modelId;
  }

  public int getModelId(){
    return modelId;
  }
  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  @Override
  public String toString() {
    return "CarModel{" +
        "model id='" + modelId + '\'' +
        "model='" + model + '\'' +
        ", make='" + make + '\'' +
        '}';
  }
}
