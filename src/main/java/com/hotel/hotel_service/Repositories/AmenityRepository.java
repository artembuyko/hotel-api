package com.hotel.hotel_service.Repositories;

import com.hotel.hotel_service.Models.Entityes.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity,Long> {
    Optional<Amenity> findByName(String name);
}
