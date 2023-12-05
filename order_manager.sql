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
    fare_policy_name varchar(255),
    start_city varchar(255),
    end_city varchar(255)
)
BEGIN
DECLARE account_exists INT;
    DECLARE fare_policy_exists INT;
    DECLARE commute_route_exists INT;

    SELECT COUNT(*) INTO account_exists FROM passenger WHERE account_number = account_number;
    SELECT COUNT(*) INTO fare_policy_exists FROM fare_policy WHERE name = fare_policy_name;
    SELECT COUNT(*) INTO commute_route_exists FROM commute_distance WHERE start_city = start_city_p AND end_city = end_city_p;

    IF account_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid account number.';
    ELSEIF fare_policy_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid fare policy name.';
    ELSEIF commute_route_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Invalid commute route.';
    ELSE
        INSERT INTO ride_order(order_date, desired_capacity, accessibility, account_number, fare_policy_name, start_city, end_city) 
        VALUES (order_date, desired_capacity, accessibility, account_number, fare_policy_name, start_city, end_city);
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
      AND c.location = start_city_name;
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
CREATE PROCEDURE update_order_status(order_id INT, new_status enum('available','completed','canceled','in progress'))
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
    IF NEW.order_status = 'completed' AND OLD.order_status != 'completed' THEN
        UPDATE car
        SET location = NEW.end_city
        WHERE plate = (SELECT car_plate FROM ride_order WHERE id = NEW.id);
    END IF;
END $$

DELIMITER ;
















