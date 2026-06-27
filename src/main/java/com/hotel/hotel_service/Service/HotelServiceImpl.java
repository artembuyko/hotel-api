package com.hotel.hotel_service.Service;

import com.hotel.hotel_service.Exceptions.HotelNotFoundException;
import com.hotel.hotel_service.Models.DTO.HotelRequest;
import com.hotel.hotel_service.Models.DTO.HotelResponse;
import com.hotel.hotel_service.Models.DTO.HotelSummaryResponse;
import com.hotel.hotel_service.Models.Entityes.Amenity;
import com.hotel.hotel_service.Models.Entityes.Hotel;
import com.hotel.hotel_service.Models.Mappers.HotelMapping;
import com.hotel.hotel_service.Repositories.AmenityRepository;
import com.hotel.hotel_service.Repositories.HotelRepository;
import com.hotel.hotel_service.Repositories.Specifications.HotelSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class HotelServiceImpl implements HotelService{

    private final HotelMapping mapping;
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;


    @Override
    public List<HotelSummaryResponse> getAllHotels() {
        return hotelRepository.findAll().stream().map(mapping::toSummaryResponse).toList();
    }

    @Override
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(()-> new HotelNotFoundException("отель с таким айди не найден"));
        return mapping.toResponse(hotel);
    }

    @Transactional
    @Override
    public HotelSummaryResponse createHotel(HotelRequest request) {
        Hotel hotel = mapping.toEntity(request);
        Hotel saveHotel = hotelRepository.save(hotel);
        return mapping.toSummaryResponse(saveHotel);
    }

    @Transactional
    @Override
    public void addAmenities(Long hotelId, List<String> amenityNames) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(()-> new HotelNotFoundException("отель с таким айди не найден"));
        for (String name : amenityNames) {
            Amenity amenity = amenityRepository.findByName(name)
                    .orElseGet(() -> {
                        Amenity newAmenity = new Amenity();
                        newAmenity.setName(name);
                        return amenityRepository.save(newAmenity);
                    });
            hotel.getAmenities().add(amenity);
        }
    }

    @Override
        public List<HotelSummaryResponse> searchHotels(
                String name,
                String brand,
                String city,
                String country,
                List<String> amenities) {

        Specification<Hotel> spec = Specification.where((root, query, cb) -> cb.conjunction());

            if (name != null && !name.isBlank()) {
                spec = spec.and(HotelSpecification.hasName(name));
            }
            if (brand != null && !brand.isBlank()) {
                spec = spec.and(HotelSpecification.hasBrand(brand));
            }
            if (city != null && !city.isBlank()) {
                spec = spec.and(HotelSpecification.hasCity(city));
            }
            if (country != null && !country.isBlank()) {
                spec = spec.and(HotelSpecification.hasCountry(country));
            }
            if (amenities != null && !amenities.isEmpty()) {
                spec = spec.and(HotelSpecification.hasAmenities(amenities));
            }

            List<Hotel> hotels = hotelRepository.findAll(spec);
            return hotels.stream()
                    .map(mapping::toSummaryResponse)
                    .collect(Collectors.toList());
        }

    public Map<String, Long> getHistogram(String param) {
        if (param == null) {
            throw new IllegalArgumentException("Histogram parameter cannot be null");
        }
        return switch (param.toLowerCase()) {
            case "brand" -> toMap(hotelRepository.countByBrand());
            case "city" -> toMap(hotelRepository.countByCity());
            case "country" -> toMap(hotelRepository.countByCountry());
            case "amenities" -> toMap(hotelRepository.countByAmenity());
            default -> throw new IllegalArgumentException("Invalid histogram parameter: " + param);
        };
    }

    private Map<String, Long> toMap(List<Object[]> rows) {
        return rows.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }
}
