package com.hotel.hotel_service.Models.Mappers;

import com.hotel.hotel_service.Models.DTO.HotelRequest;
import com.hotel.hotel_service.Models.DTO.HotelResponse;
import com.hotel.hotel_service.Models.DTO.HotelSummaryResponse;
import com.hotel.hotel_service.Models.Entityes.Address;
import com.hotel.hotel_service.Models.Entityes.Amenity;
import com.hotel.hotel_service.Models.Entityes.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapping {

    @Mapping(target = "address", expression = "java(mapAddressToString(hotel.getAddress()))")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryResponse toSummaryResponse(Hotel hotel);

    @Mapping(target = "amenities", source = "amenities")   // теперь без expression
    HotelResponse toResponse(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelRequest request);

    default String amenityToString(Amenity amenity) {
        return amenity.getName();
    }

    default String mapAddressToString(Address address) {
        if (address == null) return null;
        return address.getHouseNumber() + " " + address.getStreet() + ", " +
                address.getCity() + ", " + address.getCountry() + ", " +
                address.getPostCode();
    }
}