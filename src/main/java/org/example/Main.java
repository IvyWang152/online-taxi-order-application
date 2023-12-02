package org.example;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {

    System.out.println("Hello world!");
    CLI cli = new CLI();
    cli.start();
    System.out.println("Close dashboard? (y/n)");
    System.out.println("Enter your choice: ");
    Scanner s = new Scanner(System.in);
    String res = s.nextLine();
    if(res.equalsIgnoreCase("y")){
      cli.close();
    }

  }
}