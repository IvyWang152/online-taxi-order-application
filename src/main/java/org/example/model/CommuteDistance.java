package org.example.model;

public class CommuteDistance {
  private String startCity;
  private String endCity;
  private double distance;

  public String getStartCity() {
    return startCity;
  }

  public void setStartCity(String startCity) {
    this.startCity = startCity;
  }

  public String getEndCity() {
    return endCity;
  }

  public void setEndCity(String endCity) {
    this.endCity = endCity;
  }

  public double getDistance() {
    return distance;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  @Override
  public String toString() {
    return "CommuteDistance{" +
        "startCity='" + startCity + '\'' +
        ", endCity='" + endCity + '\'' +
        ", distance=" + distance +
        '}';
  }
}
