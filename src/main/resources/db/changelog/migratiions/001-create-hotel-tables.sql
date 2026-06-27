
CREATE TABLE hotel (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description VARCHAR(2000),
                       brand VARCHAR(255) NOT NULL,
                       address_house_number VARCHAR(50) NOT NULL,
                       address_street VARCHAR(255) NOT NULL,
                       address_city VARCHAR(255) NOT NULL,
                       address_country VARCHAR(255) NOT NULL,
                       address_post_code VARCHAR(20) NOT NULL,
                       contacts_phone VARCHAR(50) NOT NULL,
                       contacts_email VARCHAR(255) NOT NULL,
                       arrival_time_check_in VARCHAR(10) NOT NULL,
                       arrival_time_check_out VARCHAR(10)
);

CREATE TABLE amenity (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE hotel_amenity (
                               hotel_id BIGINT NOT NULL,
                               amenity_id BIGINT NOT NULL,
                               FOREIGN KEY (hotel_id) REFERENCES hotel(id),
                               FOREIGN KEY (amenity_id) REFERENCES amenity(id)
);