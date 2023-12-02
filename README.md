# Taxi Booking Application

# overview
Taxi Booking Application is a java-based application that allows the users
access/manage taxi order details either as driver or passenger.
The driver can manage driver profile and take orders created by passengers.
The passenger can manage passenger profile and order details.

# features

# structure
taxi-order/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── org/
│   │   │   │   └── example/
│   │   │   │       ├── cli/
│   │   │   │       │   └── CLI.java            # Command Line Interface for user interaction
│   │   │   │       │
│   │   │   │       ├── dao/                    # Data Access Objects for database interactions
│   │   │   │       │   ├── DriverDao.java
│   │   │   │       │   └── PassengerDao.java
│   │   │   │       │
│   │   │   │       ├── DBConnector.java        # Database connection management
│   │   │   │       │
│   │   │   │       ├── model/                  # Entity classes representing data
│   │   │   │       │   ├── Driver.java
│   │   │   │       │   └── Passenger.java
│   │   │   │       │
│   │   │   │       └── Main.java               # Main entry point of the application
│   │   │   │
├── pom.xml                                     # Maven build file (if using Maven)
└── README.md                                   # Project description and instructions

