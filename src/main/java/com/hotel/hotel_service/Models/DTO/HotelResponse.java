package com.hotel.hotel_service.Models.DTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressDTO address;
    private ContactsDTO contacts;
    private ArrivalTimeDTO arrivalTime;
    private List<String> amenities;
}