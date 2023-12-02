USE city_ride;

-- 1. CREATE: create passenger
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

-- 2. READ: read all available cars
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

-- 3. UPDATE: update order(eg: location, car choice)
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
-- 4. UPDATE: update user profile
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

-- 5. DELETE: cancel an order
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




