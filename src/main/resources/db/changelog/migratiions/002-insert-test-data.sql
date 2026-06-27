-- Вставка отелей
INSERT INTO hotel (id, name, description, brand, address_house_number, address_street, address_city, address_country, address_post_code, contacts_phone, contacts_email, arrival_time_check_in, arrival_time_check_out)
VALUES (1, 'DoubleTree by Hilton Minsk', 'The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...', 'Hilton', '9', 'Pobediteley Avenue', 'Minsk', 'Belarus', '220004', '+375 17 309-80-00', 'doubletreeminsk.info@hilton.com', '14:00', '12:00');

-- Вставка удобств
INSERT INTO amenity (id, name) VALUES (1, 'Free WiFi');
INSERT INTO amenity (id, name) VALUES (2, 'Free parking');
INSERT INTO amenity (id, name) VALUES (3, 'Non-smoking rooms');

-- Связи
INSERT INTO hotel_amenity (hotel_id, amenity_id) VALUES (1, 1);
INSERT INTO hotel_amenity (hotel_id, amenity_id) VALUES (1, 2);
INSERT INTO hotel_amenity (hotel_id, amenity_id) VALUES (1, 3);