package com.hotel.hotel_service.Models.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class HotelSummaryResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
