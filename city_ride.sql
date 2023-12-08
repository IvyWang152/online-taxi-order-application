CREATE DATABASE if not exists city_ride;
use city_ride;

-- Creating Tables
CREATE TABLE car_model (
	car_model_id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50),
    make VARCHAR(20)
);

INSERT INTO car_model (model, make) VALUES ("Corolla", "Toyota");
INSERT INTO car_model (model, make) VALUES ("ARIYA","Nissan");
INSERT INTO car_model (model, make) VALUES ("XC90","Volvo");
INSERT INTO car_model (model, make) VALUES ("Accord","Honda");
INSERT INTO car_model (model, make) VALUES ("LS","Lexus");

-- make: Toyota, Tesla
-- model: toyota-camry; toyota-rav4

-- Creating Driver table
CREATE TABLE driver (
    driver_license VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    gender VARCHAR(10),
    birth_date DATE,
    address VARCHAR(100),
    is_available BOOLEAN DEFAULT TRUE
);


INSERT INTO driver(driver_license,name,gender,birth_date,address) VALUES ('AB6X','admin','unknown','2000-01-01','Malden');
INSERT INTO driver(driver_license,name,gender,birth_date,address) VALUES ('TT0TT','Harry','male','1986-10-13','Hagwarts');
INSERT INTO driver(driver_license,name,gender,birth_date,address) VALUES ('MMB122','Micy','female','2200-11-07','Texas');
INSERT INTO driver(driver_license,name,gender,birth_date,address) VALUES ('ADMIN','test','binary','1990-05-29','Boston');
INSERT INTO driver(driver_license,name,gender,birth_date,address) VALUES ('2WSX0','Tara','female','1983-07-15','New York');

-- Creating Car table
CREATE TABLE car (
	plate VARCHAR(20) PRIMARY KEY,
    car_model_id INT,
    car_capacity INT,
    color VARCHAR(20),
    accessibility BOOL,
    driver_license varchar(20),
    location varchar(255) DEFAULT NULL,
    FOREIGN KEY(car_model_id) REFERENCES car_model(car_model_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (driver_license) REFERENCES driver(driver_license) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO car(plate,car_model_id,car_capacity,color,accessibility,driver_license) VALUES ('6666',2,4,'red',false,'admin');
INSERT INTO car(plate,car_model_id,car_capacity,color,accessibility,driver_license) VALUES ('8888',4,4,'black',true,'ab6x');
INSERT INTO car(plate,car_model_id,car_capacity,color,accessibility,driver_license) VALUES ('I6HW2',1,6,'white',true,'2wsx0');
INSERT INTO car(plate,car_model_id,car_capacity,color,accessibility,driver_license) VALUES ('FF01A',2,4,'blue',true,'MMB122');
INSERT INTO car(plate,car_model_id,car_capacity,color,accessibility,driver_license) VALUES ('54WHB',3,6,'yellow',false,'TT0TT');

-- Creating Payment table
CREATE TABLE payment_method (
    card_type ENUM('debit','credit'),
    card_number VARCHAR(20) PRIMARY KEY
);


CREATE TABLE commute_distance(
	start_city VARCHAR(255),
    end_city VARCHAR(255),
    distance DECIMAL(10,2),
    PRIMARY KEY (start_city, end_city)
);

INSERT INTO commute_distance(start_city,end_city,distance) VALUES ('Boston','Worcester',47);
INSERT INTO commute_distance(start_city,end_city,distance) VALUES ('Boston','Cambridge',3);
INSERT INTO commute_distance(start_city,end_city,distance) VALUES ('Newton','Boston',10);
INSERT INTO commute_distance(start_city,end_city,distance) VALUES ('New York','Boston',190);
INSERT INTO commute_distance(start_city,end_city,distance) VALUES ('Boston','New York',180);
INSERT INTO commute_distance(start_city,end_city,distance) VALUES ('Malden','Boston',22);

-- Creating Passenger table
CREATE TABLE passenger (
    account_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    gender VARCHAR(10),
    birth_date DATE
);

INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('1', 'John Doe', 'Male', '2000-01-01');
INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('2', 'Jane Smith', 'Female', '1995-05-15');
INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('3', 'Sam Brown', 'Male', '1988-03-25');
INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('4', 'Michael Green', 'Male', '1975-12-08');
INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('5', 'Taylor Johnson', 'Non-Binary', '1990-08-28');
INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('6', 'Alexis Kim', 'Agender', '1993-04-17');
INSERT INTO passenger (account_number, name, gender, birth_date)
VALUES ('7', 'Jordan Lee', 'Other', '1985-06-12');

-- Passenger-Payment relationship
CREATE TABLE passenger_payment (
    account_number VARCHAR(20),
    card_number VARCHAR(20),
    PRIMARY KEY (account_number, card_number),
    FOREIGN KEY (account_number) REFERENCES passenger(account_number) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (card_number) REFERENCES payment_method(card_number) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO payment_method (card_type, card_number)
VALUES ('Debit', '1111222233334444');
INSERT INTO payment_method (card_type, card_number)
VALUES ('Credit', '1111222233335555');
INSERT INTO payment_method (card_type, card_number)
VALUES ('Credit', '1111222233336666');
INSERT INTO payment_method (card_type, card_number)
VALUES ('Credit', '1111222233337777');
INSERT INTO payment_method (card_type, card_number)
VALUES ('Credit', '1111222233338888');


-- Creating order table (a weak entity)
CREATE TABLE ride_order (
	id INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATE,
    desired_capacity INT,
    accessibility bool,
    fare DECIMAL(10, 2) DEFAULT 0,
    driver_review INT DEFAULT 0,
    passenger_review INT DEFAULT 0,
    order_status varchar(20) DEFAULT 'available',
    account_number VARCHAR(20),
    car_plate VARCHAR(20) DEFAULT NULL,
    start_city VARCHAR(255),
    end_city VARCHAR(255),
    
    FOREIGN KEY (account_number) REFERENCES passenger(account_number) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (car_plate) REFERENCES car(plate) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_commute_distance
        FOREIGN KEY (start_city, end_city)
        REFERENCES commute_distance(start_city, end_city)
        ON UPDATE CASCADE ON DELETE RESTRICT
    -- FOREIGN KEY (start_city, end_city) REFERENCES commute_distance(start_city,end_city) ON UPDATE CASCADE ON DELETE RESTRICT
);

INSERT INTO ride_order(order_date,desired_capacity,accessibility,account_number,start_city,end_city)
values ('2023-12-07',4,true,'1','Boston','Cambridge');


-- Driver procedures
-- 1. CREATE: create driver / register
DELIMITER $$

CREATE PROCEDURE create_driver(
	driver_license varchar(20),
    name varchar(50),
    gender varchar(10),
    birth_date DATE,
    address varchar(100)
)
BEGIN
INSERT INTO driver(driver_license, name, gender, birth_date, address) 
VALUES (driver_license, name,gender,birth_date, address);
END $$

DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_drivers()
BEGIN
	SELECT * from driver;
END $$

DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_driver_by_license(driver_license_p varchar(20))
BEGIN
	SELECT * from driver
    where driver_license = driver_license_p;
END $$
DELIMITER ;

-- get, create car models
DELIMITER $$
CREATE PROCEDURE get_car_models()
BEGIN
    SELECT * FROM car_model;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE add_car_model(
    IN model_name VARCHAR(50),
    IN make_name VARCHAR(20)
)
BEGIN
    INSERT INTO car_model (model, make) VALUES (model_name, make_name);
END $$
DELIMITER ;

-- create car
DELIMITER $$
CREATE PROCEDURE create_car(
	plate VARCHAR(20),
    car_model_id INT,
    car_capacity INT,
    color VARCHAR(20),
    accessibility BOOL,
    driver_license varchar(20)
)
BEGIN 
    INSERT INTO car(plate, car_model_id, car_capacity, color, accessibility,driver_license) 
    VALUES(plate, car_model_id, car_capacity, color, accessibility,driver_license);
END $$

DELIMITER ;

-- get car of the driver
DELIMITER $$
CREATE PROCEDURE get_car_of_driver(driver_license_p varchar(20))
BEGIN
	DECLARE res INT;
    
	SELECT COUNT(*) INTO res FROM car WHERE driver_license = driver_license_p;
	IF res=0 THEN
		SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No car registered under this driver account';
	END IF;
    SELECT * FROM car WHERE driver_license = driver_license_p;
END $$

DELIMITER ;

-- get all cars
DELIMITER $$
CREATE PROCEDURE get_cars()
BEGIN
	SELECT * FROM car;
END $$

DELIMITER ;

-- 6. UPDATE: update car color and accessibility
DELIMITER $$
CREATE PROCEDURE updated_car_details (
	plate_p VARCHAR(20),
    color_p VARCHAR(20),
    accessibility_p BOOL,
    driver_license_p varchar(20)
)
BEGIN
	UPDATE car
    SET color = color_p,
    accessibility = accessibility_p
    WHERE plate = plate_p;
END $$
DELIMITER ;

-- 7. UPDATE: update driver details
DELIMITER $$
CREATE PROCEDURE update_driver_details(
	driver_license_p varchar(20),
    name_p varchar(50),
    gender_p varchar(10),
    birth_date_p DATE,
    address_p varchar(100),
    is_available_p bool
)
BEGIN 
	UPDATE driver
    SET name = name_p,
    gender = gender_p,
    birth_date = birth_date_p,
    address = address_p,
    is_available = is_available_p
    WHERE driver_license = driver_license_p;
END $$
DELIMITER ;


-- 8. DELETE: delete driver and automatically delete their cars (trigger)
DELIMITER $$
CREATE PROCEDURE delete_driver_account(
	driver_license_p  varchar(20)
)
BEGIN
	DELETE FROM driver WHERE driver_license = driver_license_p;
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER delete_driver_cars
AFTER DELETE ON driver
FOR EACH ROW
BEGIN
	DELETE FROM car WHERE driver_license = OLD.driver_license;
END $$
DELIMITER ;


-- PASSENGER
-- CREATE: create passenger
DELIMITER $$

CREATE PROCEDURE create_passenger(
	account_number VARCHAR(20),
    name VARCHAR(50),
    gender VARCHAR(10),
    birth_date DATE
)
BEGIN
INSERT INTO passenger(account_number, name, gender, birth_date) 
VALUES (account_number, name,gender,birth_date);
END $$
DELIMITER $$
CREATE PROCEDURE get_passengers()
BEGIN
	SELECT * from passenger;
END $$

DELIMITER ;



-- get passenger by account_number
DELIMITER $$
CREATE PROCEDURE get_passenger_by_account_number(account_number_p varchar(20))
BEGIN
	SELECT * from passenger
    where account_number = account_number_p;
END $$
DELIMITER ;

-- READ: read all available cars
DELIMITER //

CREATE PROCEDURE read_available_cars()
BEGIN
    SELECT
        car.plate,
        car.car_model_id,
        car.car_capacity,
        car.color,
        car.accessibility,
        car.location,
        driver.name AS driver_name
    FROM
        car
    JOIN car_model ON car.car_model_id = car_model.car_model_id
    JOIN driver ON car.driver_license = driver.driver_license
    WHERE
        driver.is_available = TRUE;
END //

DELIMITER //


DELIMITER //
-- UPDATE: update user profile
CREATE PROCEDURE update_user_profile(
    IN account_number_P VARCHAR(20),
    IN new_name VARCHAR(50),
    IN new_gender VARCHAR(10),
    IN new_birth_date DATE
)
BEGIN
    UPDATE passenger
    SET
        name = new_name,
        gender = new_gender,
        birth_date = new_birth_date
    WHERE
        account_number = account_number_P;
END //

DELIMITER ;
-- 8. DELETE: delete passenger and automatically delete their orders (trigger)
DELIMITER $$
CREATE PROCEDURE delete_passenger_account(
	account_number_p  varchar(20)
)
BEGIN
	DELETE FROM passenger WHERE account_number = account_number_p;
END $$
DELIMITER ;

DELIMITER //

-- DELETE: cancel an order
CREATE PROCEDURE Cancel_ride_order(
    IN order_id INT
)
BEGIN
    DELETE FROM ride_order
    WHERE id = order_id;
END //

DELIMITER ;

-- Function to calculate total fare
DELIMITER //
CREATE FUNCTION calculate_total_fare(
    base_fare DECIMAL(10, 2),
    per_mile_rate DECIMAL(10, 2),
    distance_traveled DECIMAL(10, 2)
)
RETURNS DECIMAL(10, 2)
READS SQL DATA
BEGIN
    DECLARE total_fare DECIMAL(10, 2);
    SET total_fare = base_fare + per_mile_rate * distance_traveled;
    RETURN total_fare;
END;
//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE get_orders_for_passenger(IN account_number_p VARCHAR(255))
BEGIN
    SELECT *
    FROM ride_order
    WHERE account_number = account_number_p;
END //
DELIMITER ;



-- ORDER

DELIMITER $$
CREATE PROCEDURE get_orders_by_driver(driver_license_p varchar(20), order_status_p ENUM('completed', 'canceled', 'in progress'))
BEGIN
	SELECT * FROM ride_order o 
    JOIN car c ON o.car_plate = c.plate
    JOIN driver d ON d.driver_license = c.driver_license
    WHERE d.driver_license = driver_license_p AND o.order_status = order_status_p;	
END $$
DELIMITER ;

-- ALL ORDER OPERSTIONS ARE ONLY FOR LOGGED IN USERS!!!

-- create order
DELIMITER $$
-- default: fare, driver review, passenger review, order_status
-- only logged in passenger can perform this operation
CREATE PROCEDURE create_order(
	order_date DATE,
    desired_capacity int,
    accessibility bool,
    account_number varchar(20),
    start_city varchar(255),
    end_city varchar(255)
)
BEGIN
	DECLARE account_exists INT;
    DECLARE commute_route_exists INT;

    SELECT COUNT(*) INTO account_exists FROM passenger WHERE account_number = account_number;
    SELECT COUNT(*) INTO commute_route_exists FROM commute_distance WHERE start_city = start_city AND end_city = end_city;

    IF account_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid account number.';
    ELSEIF commute_route_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid commute route.';
    ELSE
        INSERT INTO ride_order(order_date, desired_capacity, accessibility, account_number, start_city, end_city) 
        VALUES (order_date, desired_capacity, accessibility, account_number, start_city, end_city);
    END IF;
END $$
DELIMITER ;

-- update/edit order
-- edit capacity
DELIMITER $$
CREATE PROCEDURE update_order_capacity(
	order_id int,
    desired_capacity_p int
)
BEGIN 
    DECLARE order_exists INT;

    SELECT COUNT(*) INTO order_exists FROM ride_order WHERE id = order_id;

    IF order_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Order ID does not exist.';
    ELSE
        UPDATE ride_order
        SET desired_capacity = desired_capacity_p
        WHERE id = order_id;
    END IF;
END $$
DELIMITER ;

-- edit order accessibility
DELIMITER $$
CREATE PROCEDURE update_order_accessibility(
	order_id int,
    accessibility_p int
)
BEGIN 
    DECLARE order_exists INT;

    SELECT COUNT(*) INTO order_exists FROM ride_order WHERE id = order_id;

    IF order_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Order ID does not exist.';
    ELSE
        UPDATE ride_order
        SET accessibility = accessibility_p
        WHERE id = order_id;
    END IF;
END $$
DELIMITER ;


-- edit order start_city and end_city
DELIMITER $$
CREATE PROCEDURE update_order_route(
	order_id int,
    start_city_p varchar(255),
    end_city_p varchar(255)
)
BEGIN 
	DECLARE order_exists INT;
    DECLARE route_exists INT;
    SELECT COUNT(*) INTO order_exists FROM ride_order WHERE id = order_id;
    SELECT COUNT(*) INTO route_exists FROM commute_distance WHERE start_city = start_city_p AND end_city = end_city_p;

    IF order_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Order ID does not exist.';
    ELSEIF route_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Commute route does not exist.';
    ELSE
        UPDATE ride_order
        SET start_city = start_city_p, end_city = end_city_p
        WHERE id = order_id;
    END IF;
END $$
DELIMITER ;

DELIMITER $$

-- search available cars
-- CREATE PROCEDURE get_available_cars()
-- BEGIN
--     SELECT c.* 
--     FROM car c
--     JOIN driver d ON c.driver_license = d.driver_license
--     WHERE d.is_available = true;
-- END $$

-- DELIMITER ;

-- search available cars in start city
-- we can do further filters at the backend like filter cars with desired capacity or accessibility
-- or we can create procedures here 
DELIMITER $$

CREATE PROCEDURE get_available_cars_in_city(start_city_name VARCHAR(255))
BEGIN
    SELECT DISTINCT c.* 
    FROM car c
    JOIN driver d ON c.driver_license = d.driver_license
    WHERE d.is_available = TRUE
      AND (c.location = start_city_name OR c.location IS NULL);
END $$

DELIMITER ;

-- confirm order: whether delete it or not

DELIMITER $$

CREATE PROCEDURE delete_order(IN order_id INT)
BEGIN
    DELETE FROM ride_order WHERE id = order_id;
END $$

DELIMITER ;

-- update order status
DELIMITER $$
CREATE PROCEDURE update_order_status(order_id INT, new_status varchar(20))
BEGIN
	UPDATE ride_order
    SET order_status = new_status
    WHERE id = order_id;
END $$

DELIMITER ;

DELIMITER $$

-- drop trigger after_order_completion;
CREATE TRIGGER after_order_completion
AFTER UPDATE ON ride_order
FOR EACH ROW
BEGIN
	IF NEW.order_status = 'in progress' AND OLD.order_status = 'available' THEN
		UPDATE car
        SET location = NEW.start_city
        WHERE plate = NEW.car_plate;
		UPDATE driver
        INNER JOIN car ON driver.driver_license = car.driver_license
        SET is_available = FALSE WHERE car.plate = NEW.car_plate;
	END IF;
    IF NEW.order_status = 'completed' AND OLD.order_status != 'completed' THEN
        UPDATE car
        SET location = NEW.end_city
        WHERE plate = (SELECT car_plate FROM ride_order WHERE id = NEW.id);
		UPDATE driver 
        INNER JOIN car ON driver.driver_license = car.driver_license
        SET is_available = TRUE WHERE car.plate = NEW.car_plate;
    END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE view_routes()
BEGIN
	SELECT * FROM commute_distance;
END $$

DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_order_by_orderId(id_p int)
begin
	SELECT * FROM ride_order
    WHERE id = id_p;
end $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE get_cars_with_cap_or_access(start_city_name VARCHAR(255), capacity int, accessibility_p bool)
BEGIN
    SELECT DISTINCT c.* 
    FROM car c
    JOIN driver d ON c.driver_license = d.driver_license
    WHERE d.is_available = TRUE
      AND (c.location = start_city_name or c.location is null)
      AND c.car_capacity = capacity
      AND c.accessibility = accessibility_p;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE process_order(order_id INT,car_plate_p varchar(20),start_city_p varchar(255))
BEGIN
	UPDATE ride_order
    SET car_plate = car_plate_p,
	start_city = start_city_p
    WHERE id = order_id;

END $$

DELIMITER ;


DELIMITER $$

CREATE FUNCTION get_latest_order_id(account_number VARCHAR(20))
RETURNS INT DETERMINISTIC
BEGIN
    DECLARE latest_order_id INT DEFAULT 0;

    SELECT id INTO latest_order_id 
    FROM ride_order 
    WHERE account_number = account_number 
    ORDER BY order_date DESC, id DESC
    LIMIT 1;

    RETURN latest_order_id;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE update_order_fare(order_id_p INT, base_fare_p DECIMAL(10, 2), per_mile_rate_p DECIMAL(10, 2))
BEGIN
    DECLARE distance_p DECIMAL(10, 2);
    DECLARE start_city_p VARCHAR(255);
    DECLARE end_city_p VARCHAR(255);
    DECLARE total_fare DECIMAL(10, 2);
    DECLARE order_exists INT;
    

    -- Fetch start and end city for the order
    SELECT start_city, end_city INTO start_city_p, end_city_p FROM ride_order WHERE id = order_id_p;
	SELECT COUNT(*) INTO order_exists from ride_order where id = order_id_p;
    IF order_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Order ID not found in database.';
    END IF;
    -- Get the distance for the route
    SELECT distance INTO distance_p FROM commute_distance WHERE start_city = start_city_p AND end_city = end_city_p;

    -- Calculate total fare using the function
    SET total_fare = calculate_total_fare(base_fare_p, per_mile_rate_p, distance_p);

    -- Update the fare in the order
    UPDATE ride_order
    SET fare = total_fare
    WHERE id = order_id_p;
END $$

DELIMITER ;




