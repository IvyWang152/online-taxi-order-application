USE city_ride;

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

CREATE TRIGGER after_order_completion
AFTER UPDATE ON ride_order
FOR EACH ROW
BEGIN
	IF NEW.order_status = 'in progress' AND OLD.order_status = 'available' THEN
		UPDATE car
        SET location = NEW.start_city
        WHERE plate = (SELECT car_plate FROM ride_order WHERE id = NEW.id);
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










