package org.example;

import java.util.Scanner;

public class CLI {
  private final Scanner scanner;

  public CLI() {
    this.scanner = new Scanner(System.in);
  }

  public int showMainMenuAndGetSelection() {
    //we can start as "choose to be a driver or passenger"
    // then provide matching operation prompts
    System.out.println("1. Add new driver");
    System.out.println("2. View drivers");
    // ... other options
    System.out.print("Enter your choice: ");
    return scanner.nextInt();
  }

  // Methods to handle input for other operations
  // e.g., public Driver getNewDriverDetails() {...}

  // Method to close the scanner
  public void close() {
    scanner.close();
  }
}
