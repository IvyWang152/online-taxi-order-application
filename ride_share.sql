
CREATE DATABASE if not exists city_ride;
use city_ride;

CREATE TABLE car_model (
	car_model_id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50),
    make VARCHAR(20)
);

-- make: Toyota, Tesla
-- model: toyota-camry; toyota-rav4

-- Creating Location table: geographical location used to locate drivers and passengers
CREATE TABLE location (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE,
    longitude DOUBLE
);


-- Creating Driver table
CREATE TABLE driver (
    driver_license VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    gender VARCHAR(10),
    birth_date DATE,
    address VARCHAR(100),
    is_available BOOLEAN DEFAULT TRUE
);

-- Creating Car table
CREATE TABLE car (
	plate VARCHAR(20) PRIMARY KEY,
    car_model_id INT,
    car_capacity INT,
    color VARCHAR(20),
    accessibility BOOL,
    driver_license varchar(20),
    FOREIGN KEY(car_model_id) REFERENCES car_model(car_model_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (driver_license) REFERENCES driver(driver_license) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- Creating Payment table
CREATE TABLE payment_method (
    card_type ENUM('debit','credit'),
    card_number VARCHAR(20) PRIMARY KEY
);


CREATE TABLE fare_policy(
	name VARCHAR(255) PRIMARY KEY,
    base_fare DECIMAL(10, 2),
    per_mile_rate DECIMAL(10, 2),
    cancellation_fee DECIMAL(10, 2)
);

-- Creating Passenger table
CREATE TABLE passenger (
    account_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    gender VARCHAR(10),
    birth_date DATE
);

-- Passenger-Payment relationship
CREATE TABLE passenger_payment (
    account_number VARCHAR(20),
    card_number VARCHAR(20),
    PRIMARY KEY (account_number, card_number),
    FOREIGN KEY (account_number) REFERENCES passenger(account_number) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (card_number) REFERENCES payment_method(card_number) ON UPDATE CASCADE ON DELETE RESTRICT
);


-- Creating order table (a weak entity)
CREATE TABLE ride_order (
	id INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATE,
    order_time TIME,
    desired_make VARCHAR(20),
    desired_model VARCHAR(20),
    desired_capacity INT,
    fare DECIMAL(10, 2),
    driver_review INT,
    passenger_review INT,
    order_status ENUM('available', 'completed', 'canceled', 'in progress') DEFAULT 'available',
    account_number VARCHAR(20),
    fare_policy_name VARCHAR(255),
    car_plate VARCHAR(20),
    pickup_location INT,
    destination_location INT,
    FOREIGN KEY (account_number) REFERENCES passenger(account_number) ON UPDATE CASCADE ON DELETE RESTRICT
    FOREIGN KEY (fare_policy_name) REFERENCES fare_policy(name) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (car_plate) REFERENCES car(plate) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (pickup_location) REFERENCES location(location_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (destination_location) REFERENCES location(location_id) ON UPDATE CASCADE ON DELETE RESTRICT
);



