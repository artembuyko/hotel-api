package com.hotel.hotel_service.Controllers;

import com.hotel.hotel_service.Models.DTO.HotelRequest;
import com.hotel.hotel_service.Models.DTO.HotelResponse;
import com.hotel.hotel_service.Models.DTO.HotelSummaryResponse;
import com.hotel.hotel_service.Service.HotelServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelControllers {

    private final HotelServiceImpl service;

    @GetMapping
    public List<HotelSummaryResponse> getAllHotels() {
        return service.getAllHotels();
    }

    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable Long id) {
        return service.getHotelById(id);
    }

    @GetMapping("/search")
    public List<HotelSummaryResponse> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities) {
        return service.searchHotels(name, brand, city, country, amenities);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotelSummaryResponse createHotel(@Valid @RequestBody HotelRequest request) {
        return service.createHotel(request);

    }
}
