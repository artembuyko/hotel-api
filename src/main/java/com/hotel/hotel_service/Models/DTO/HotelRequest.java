package com.hotel.hotel_service.Models.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@Valid
@NoArgsConstructor
public class HotelRequest {
    @NotBlank
    private String name;
    private String description;   // optional
    @NotBlank
    private String brand;
    @Valid
    private AddressDTO address;
    @Valid
    private ContactsDTO contacts;
    @Valid
    private ArrivalTimeDTO arrivalTime;
}
