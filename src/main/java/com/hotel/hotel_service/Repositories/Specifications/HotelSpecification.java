package com.hotel.hotel_service.Repositories.Specifications;

import com.hotel.hotel_service.Models.Entityes.Amenity;
import com.hotel.hotel_service.Models.Entityes.Hotel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;

import java.util.List;

public class HotelSpecification {

    public static Specification<Hotel> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Hotel> hasBrand(String brand) {
        return (root, query, cb) -> {
            if (brand == null || brand.isBlank()) return null;
            return cb.equal(cb.lower(root.get("brand")), brand.toLowerCase());
        };
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) return null;
            return cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase());
        };
    }

    public static Specification<Hotel> hasCountry(String country) {
        return (root, query, cb) -> {
            if (country == null || country.isBlank()) return null;
            return cb.equal(cb.lower(root.get("address").get("country")), country.toLowerCase());
        };
    }

    public static Specification<Hotel> hasAmenities(List<String> amenityNames) {
        return (root, query, cb) -> {
            if (amenityNames == null || amenityNames.isEmpty()) return null;
            Join<Hotel, Amenity> join = root.join("amenities");
            return join.get("name").in(amenityNames);
        };
    }
}