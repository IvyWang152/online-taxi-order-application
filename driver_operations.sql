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
CALL create_driver('ADMIN','test','binary','1990-05-29','Boston');

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

-- car models
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

CALL add_Car_model("Corolla","Toyota");
CALL add_Car_model("ARIYA","Nissan");
CALL add_Car_model("XC90","Volvo");
CALL add_Car_model("Accord","Honda");
CALL add_Car_model("LS","Lexus");

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

CALL create_car('6666',2,4,'red',false,'admin');
CALL create_car('I6HW2',1,6,'white',true,'admin');
CALL create_car('FF01A',2,4,'blue',true,'MMB122');
CALL create_car('54WHB',3,6,'yellow',false,'TT0TT');

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


DELIMITER $$
CREATE PROCEDURE get_cars()
BEGIN
	SELECT * FROM car;
END $$

DELIMITER ;

Call get_cars();

-- 3. READ: get_availble_orders
DROP PROCEDURE IF EXISTS get_orders;
DELIMITER $$
CREATE PROCEDURE get_orders()
BEGIN
	SELECT * FROM ride_order;
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




