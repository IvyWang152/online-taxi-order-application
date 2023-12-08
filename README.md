# Taxi Booking Application

## Overview
Taxi Booking Application is a Java-based application designed to facilitate the management of taxi orders for both drivers and passengers. It enables drivers to manage their profiles and process rides, while passengers can manage their profiles and book rides.

## Features
- **Driver Management**: Drivers can register, log in, view and update their profiles, and manage ride orders.
- **Passenger Management**: Passengers can register, log in, view and update their profiles, and create ride orders.
- **Order Management**: Creation, viewing, updating, and deletion of ride orders.
- **Route Management**: View available routes and distances.
- **Car Management**: Drivers can add, view, and update their cars.
- **Order Matching**: Matches available cars with passenger orders based on location and preferences.

## Structure
The project is organized as follows:
- `cli/CLI.java`: Command Line Interface for user interaction.
- `dao/`: Data Access Objects for database interactions.
- `DBConnector.java`: Manages the database connection.
- `model/`: Contains entity classes representing data.
- `Main.java`: The main entry point of the application.

## Getting Started
To run the Taxi Booking Application, follow these steps:
1. Ensure Java is installed on your system.
2. Clone the repository to your local machine.
3. Navigate to the project directory and compile the Java files.
4. Run `Main.java` to start the application.

## Usage
After starting the application, follow the on-screen prompts to interact with the system. You can choose to operate as a driver or a passenger, and then perform various actions based on your role.

## Contributing
Contributions to the Taxi Booking Application are welcome. Please fork the repository and submit a pull request with your proposed changes.

## License
This project is licensed under the [MIT License](LICENSE.md).
