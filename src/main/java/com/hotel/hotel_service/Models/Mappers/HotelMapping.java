package com.hotel.hotel_service.Models.Mappers;

import com.hotel.hotel_service.Models.DTO.HotelRequest;
import com.hotel.hotel_service.Models.DTO.HotelResponse;
import com.hotel.hotel_service.Models.DTO.HotelSummaryResponse;
import com.hotel.hotel_service.Models.Entityes.Address;
import com.hotel.hotel_service.Models.Entityes.Amenity;
import com.hotel.hotel_service.Models.Entityes.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface HotelMapping {

    @Mapping(target = "address", expression = "java(mapAddressToString(hotel.getAddress()))")
    @Mapping(target = "phone", source = "contacts.phone")
    HotelSummaryResponse toSummaryResponse(Hotel hotel);

    @Mapping(target = "amenities", expression = "java(mapAmenitiesToStrings(hotel.getAmenities()))")
    HotelResponse toResponse(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelRequest request);

    default List<String> mapAmenitiesToStrings(Set<Amenity> amenities) {
        if (amenities == null) {
            return null;
        }
        return amenities.stream()
                .map(Amenity::getName)
                .collect(Collectors.toList());
    }

    default String mapAddressToString(Address address) {
        if (address == null) {
            return null;
        }
        return address.getHouseNumber() + " " + address.getStreet() + ", " +
                address.getCity() + ", " + address.getCountry() + ", " +
                address.getPostCode();
    }
}
