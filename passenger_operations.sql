USE city_ride;

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

DELIMITER ;
-- get passenger by account_number
DELIMITER $$
CREATE PROCEDURE get_passenger_by_account_number(account_number varchar(20))
BEGIN
	SELECT * from passenger
    where account_number = account_number;
END $$
DELIMITER ;


-- READ: read all available cars
DELIMITER //

CREATE PROCEDURE read_available_cars()
BEGIN
    SELECT
        car.plate,
        car_model.model,
        car_model.make,
        car.car_capacity,
        car.color,
        car.accessibility,
        driver.name AS driver_name,
        location.latitude AS current_latitude,
        location.longitude AS current_longitude
    FROM
        car
    JOIN car_model ON car.car_model_id = car_model.car_model_id
    JOIN driver ON car.driver_license = driver.driver_license
    JOIN location ON driver.address = location.location_id
    WHERE
        car.is_available = TRUE;
END //

DELIMITER ;

DELIMITER //

-- CREATE: create ride order
CREATE PROCEDURE create_ride_order(
    IN passenger_account_number VARCHAR(20),
    IN pickup_location_id INT,
    IN car_plate VARCHAR(20),
    IN distance_traveled DECIMAL(10, 2)
)
BEGIN
    INSERT INTO ride_order(passenger_account_number, pickup_location, car_plate, distance_traveled)
    VALUES (passenger_account_number, pickup_location_id, car_plate, distance_traveled);
END //

DELIMITER ;

DELIMITER //

-- UPDATE: update order(eg: location, car choice)
CREATE PROCEDURE update_ride_order(
    IN order_id INT,
    IN new_location_id INT,
    IN new_car_plate VARCHAR(20)
)
BEGIN
    UPDATE ride_order
    SET
        pickup_location = new_location_id,
        car_plate = new_car_plate
    WHERE
        id = order_id;
END //

DELIMITER ;

DELIMITER //
-- UPDATE: update user profile
CREATE PROCEDURE update_user_profile(
    IN account_number VARCHAR(20),
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
        account_number = account_number;
END //

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

SELECT ride_orderlocation* FROM passenger;
