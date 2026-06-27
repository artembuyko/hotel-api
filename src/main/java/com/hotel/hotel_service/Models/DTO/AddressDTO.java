package com.hotel.hotel_service.Models.DTO;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddressDTO{
    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
