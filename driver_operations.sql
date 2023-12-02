USE city_ride;

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

CALL create_driver('AB6X','admin','unknown','2000-01-01','Malden');
CALL create_driver('TT0TT','Harry','male','1986-10-13','Hagwarts');
CALL create_driver('MMB122','Micy','female','2200-11-07','Texas');
passenger
DELIMITER $$
CREATE PROCEDURE get_drivers()
BEGIN
	SELECT * from driver;
END $$

DELIMITER ;
-- 2. CREATE: create a car
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


-- 3. READ: get_availble_orders
DROP PROCEDURE IF EXISTS get_orders;
DELIMITER $$
CREATE PROCEDURE get_orders()
BEGIN
	SELECT * FROM ride_order WHERE order_status = 'available';
END $$

DELIMITER ;

-- 4. READ: read orders of a driver
DELIMITER $$
CREATE PROCEDURE get_order_by_driver(driver_license_p varchar(20), order_status_p ENUM('completed', 'canceled', 'in progress'))
BEGIN
	SELECT * FROM ride_order o 
    JOIN car c ON o.car = c.plate
    JOIN driver d ON d.driver_license = c.driver_license
    WHERE d.driver_license = driver_license_p AND o.order_status = order_status_p;	
END $$
DELIMITER ;

-- 5. UPDATE: update order status
DELIMITER $$
CREATE PROCEDURE complete_order(order_id INT)
BEGIN
	UPDATE ride_order
    SET order_status = 'completed'
    WHERE id = order_id;
END $$

DELIMITER ;


-- Trigger to update driver availability
DELIMITER //
CREATE TRIGGER update_driver_availability
AFTER UPDATE ON ride_order
FOR EACH ROW
BEGIN
	IF OLD.order_status != NEW.order_status THEN
    IF NEW.order_status = 'completed' OR NEW.order_status = 'canceled' THEN
        UPDATE driver 
        INNER JOIN car ON driver.driver_license = car.driver_license
        SET is_available = TRUE WHERE car.plate = NEW.car_plate;
    ELSE
        UPDATE driver 
        INNER JOIN car ON driver.driver_license = car.driver_license
        SET is_available = FALSE WHERE car.plate = NEW.car_plate;
    END IF;
    END IF;
END;
//
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



