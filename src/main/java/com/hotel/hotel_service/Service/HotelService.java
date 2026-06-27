package com.hotel.hotel_service.Service;

import com.hotel.hotel_service.Models.DTO.HotelRequest;
import com.hotel.hotel_service.Models.DTO.HotelResponse;
import com.hotel.hotel_service.Models.DTO.HotelSummaryResponse;

import java.util.List;
import java.util.Map;

public interface HotelService {

    List<HotelSummaryResponse> getAllHotels();
    HotelResponse getHotelById(Long id);
    List<HotelSummaryResponse> searchHotels(String name, String brand, String city, String country, List<String> amenities);
    HotelSummaryResponse createHotel(HotelRequest request);
    void addAmenities(Long hotelId, List<String> amenityNames);
    Map<String, Long> getHistogram(String param);
}
