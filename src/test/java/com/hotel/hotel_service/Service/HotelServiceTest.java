package com.hotel.hotel_service.Service;


import com.hotel.hotel_service.Models.DTO.*;
import com.hotel.hotel_service.Models.Entityes.*;
import com.hotel.hotel_service.Models.Mappers.HotelMapping;
import com.hotel.hotel_service.Repositories.AmenityRepository;
import com.hotel.hotel_service.Repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelMapping hotelMapper;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private HotelSummaryResponse summaryDTO;
    private HotelResponse detailDTO;
    private HotelRequest createRequest;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setDescription("Test Description");
        hotel.setBrand("TestBrand");

        Address address = new Address();
        address.setHouseNumber("10");
        address.setStreet("Main St");
        address.setCity("TestCity");
        address.setCountry("TestCountry");
        address.setPostCode("12345");
        hotel.setAddress(address);

        Contacts contacts = new Contacts();
        contacts.setPhone("+123456789");
        contacts.setEmail("test@hotel.com");
        hotel.setContacts(contacts);

        ArrivalTime arrival = new ArrivalTime();
        arrival.setCheckIn("14:00");
        arrival.setCheckOut("12:00");
        hotel.setArrivalTime(arrival);

        hotel.setAmenities(new HashSet<>());

        summaryDTO = new HotelSummaryResponse();
        summaryDTO.setId(1L);
        summaryDTO.setName("Test Hotel");
        summaryDTO.setDescription("Test Description");
        summaryDTO.setAddress("10 Main St, TestCity, TestCountry, 12345");
        summaryDTO.setPhone("+123456789");

        detailDTO = new HotelResponse();
        detailDTO.setId(1L);
        detailDTO.setName("Test Hotel");
        detailDTO.setDescription("Test Description");
        detailDTO.setBrand("TestBrand");

        createRequest = new HotelRequest();
        createRequest.setName("New Hotel");
        createRequest.setDescription("New Description");
        createRequest.setBrand("NewBrand");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setHouseNumber("20");
        addressDTO.setStreet("Second St");
        addressDTO.setCity("NewCity");
        addressDTO.setCountry("NewCountry");
        addressDTO.setPostCode("67890");
        createRequest.setAddress(addressDTO);

        ContactsDTO contactsDTO = new ContactsDTO();
        contactsDTO.setPhone("+987654321");
        contactsDTO.setEmail("new@hotel.com");
        createRequest.setContacts(contactsDTO);

        ArrivalTimeDTO arrivalDTO = new ArrivalTimeDTO();
        arrivalDTO.setCheckIn("15:00");
        arrivalDTO.setCheckOut("11:00");
        createRequest.setArrivalTime(arrivalDTO);
    }

    @Test
    void getAllHotels_ShouldReturnListOfSummaryDTO() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(summaryDTO);

        List<HotelSummaryResponse> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Hotel");
        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, times(1)).toSummaryResponse(hotel);
    }

    @Test
    void getAllHotels_WhenEmpty_ShouldReturnEmptyList() {
        when(hotelRepository.findAll()).thenReturn(Collections.emptyList());

        List<HotelSummaryResponse> result = hotelService.getAllHotels();

        assertThat(result).isEmpty();
        verify(hotelRepository, times(1)).findAll();
        verify(hotelMapper, never()).toSummaryResponse(any());
    }

    @Test
    void getHotelById_WhenExists_ShouldReturnDetailDTO() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toResponse(hotel)).thenReturn(detailDTO);

        HotelResponse result = hotelService.getHotelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Hotel");
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void getHotelById_WhenNotFound_ShouldThrow404() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(99L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(rse.getReason()).contains("Hotel not found");
                });
        verify(hotelRepository, times(1)).findById(99L);
    }


    @Test
    void createHotel_ShouldReturnSummaryDTO() {
        when(hotelMapper.toEntity(createRequest)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(summaryDTO);

        HotelSummaryResponse result = hotelService.createHotel(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Hotel");
        verify(hotelMapper, times(1)).toEntity(createRequest);
        verify(hotelRepository, times(1)).save(hotel);
        verify(hotelMapper, times(1)).toSummaryResponse(hotel);
    }

    @Test
    void addAmenities_WhenHotelExists_ShouldAddExistingAndNewAmenities() {
        Long hotelId = 1L;
        List<String> amenityNames = Arrays.asList("Free WiFi", "Pool", "Gym");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));


        Amenity wifi = new Amenity();
        wifi.setId(10L);
        wifi.setName("Free WiFi");
        when(amenityRepository.findByName("Free WiFi")).thenReturn(Optional.of(wifi));


        when(amenityRepository.findByName("Pool")).thenReturn(Optional.empty());
        Amenity newPool = new Amenity();
        newPool.setName("Pool");
        when(amenityRepository.save(any(Amenity.class))).thenAnswer(inv -> {
            Amenity a = inv.getArgument(0);
            a.setId(20L);
            return a;
        });

        when(amenityRepository.findByName("Gym")).thenReturn(Optional.empty());


        hotelService.addAmenities(hotelId, amenityNames);

        verify(amenityRepository, times(2)).save(any(Amenity.class));
        assertThat(hotel.getAmenities()).hasSize(3);
        assertThat(hotel.getAmenities()).extracting("name")
                .containsExactlyInAnyOrder("Free WiFi", "Pool", "Gym");
        verify(hotelRepository, never()).save(hotel);
    }

    @Test
    void addAmenities_WhenHotelNotFound_ShouldThrow404() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.addAmenities(99L, List.of("WiFi")))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> {
                    ResponseStatusException rse = (ResponseStatusException) ex;
                    assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                });
        verify(amenityRepository, never()).findByName(any());
        verify(amenityRepository, never()).save(any());
    }

    @Test
    void addAmenities_WithEmptyList_ShouldDoNothing() {
        Long hotelId = 1L;
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        hotelService.addAmenities(hotelId, Collections.emptyList());

        assertThat(hotel.getAmenities()).isEmpty();
        verify(amenityRepository, never()).findByName(any());
        verify(amenityRepository, never()).save(any());
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void searchHotels_WithMultipleParams_ShouldBuildSpecificationAndReturnSummaries() {
        String name = "Test";
        String brand = "Brand";
        String city = "City";
        String country = "Country";
        List<String> amenities = List.of("WiFi");

        when(hotelRepository.findAll(any(Specification.class))).thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(summaryDTO);

        List<HotelSummaryResponse> result = hotelService.searchHotels(name, brand, city, country, amenities);

        assertThat(result).hasSize(1);
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
        verify(hotelMapper, times(1)).toSummaryResponse(hotel);
    }

    @Test
    void searchHotels_WithNoParams_ShouldReturnAllHotels() {
        when(hotelRepository.findAll(any(Specification.class))).thenReturn(List.of(hotel));
        when(hotelMapper.toSummaryResponse(hotel)).thenReturn(summaryDTO);

        List<HotelSummaryResponse> result = hotelService.searchHotels(null, null, null, null, null);

        assertThat(result).hasSize(1);
        verify(hotelRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getHistogram_ForBrand_ShouldReturnMap() {
        List<Object[]> rows = Arrays.asList(
                new Object[]{"BrandA", 2L},
                new Object[]{"BrandB", 3L}
        );
        when(hotelRepository.countByBrand()).thenReturn(rows);

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("BrandA", 2L);
        assertThat(result).containsEntry("BrandB", 3L);
        verify(hotelRepository, times(1)).countByBrand();
    }

    @Test
    void getHistogram_ForCity_ShouldReturnMap() {
        List<Object[]> rows = Arrays.asList(
                new Object[]{"Minsk", 5L},
                new Object[]{"Moscow", 2L}
        );
        when(hotelRepository.countByCity()).thenReturn(rows);

        Map<String, Long> result = hotelService.getHistogram("city");

        assertThat(result).containsEntry("Minsk", 5L);
        verify(hotelRepository, times(1)).countByCity();
    }

    @Test
    void getHistogram_ForCountry_ShouldReturnMap() {
        when(hotelRepository.countByCountry()).thenReturn((List<Object[]>) List.of(new Object[]{"Belarus", 3L}));
        Map<String, Long> result = hotelService.getHistogram("country");
        assertThat(result).containsEntry("Belarus", 3L);
    }

    @Test
    void getHistogram_ForAmenities_ShouldReturnMap() {
        List<Object[]> rows = Arrays.asList(
                new Object[]{"Free WiFi", 10L},
                new Object[]{"Parking", 4L}
        );
        when(hotelRepository.countByAmenity()).thenReturn(rows);

        Map<String, Long> result = hotelService.getHistogram("amenities");

        assertThat(result).hasSize(2);
        assertThat(result).containsEntry("Free WiFi", 10L);
        verify(hotelRepository, times(1)).countByAmenity();
    }

    @Test
    void getHistogram_WithInvalidParam_ShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> hotelService.getHistogram("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid histogram parameter");
        verify(hotelRepository, never()).countByBrand();
        verify(hotelRepository, never()).countByCity();
        verify(hotelRepository, never()).countByCountry();
        verify(hotelRepository, never()).countByAmenity();
    }

    @Test
    void getHistogram_WithNullParam_ShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> hotelService.getHistogram(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }
}
