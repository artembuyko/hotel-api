package com.hotel.hotel_service.Controller;

import tools.jackson.databind.ObjectMapper;
import com.hotel.hotel_service.Models.DTO.AddressDTO;
import com.hotel.hotel_service.Models.DTO.ArrivalTimeDTO;
import com.hotel.hotel_service.Models.DTO.ContactsDTO;
import com.hotel.hotel_service.Models.DTO.HotelRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HotelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllHotels() throws Exception {
        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldCreateAndRetrieveHotel() throws Exception {
        HotelRequest request = new HotelRequest();
        request.setName("Integration Hotel");
        request.setDescription("Test description");
        request.setBrand("TestBrand");
        AddressDTO address = new AddressDTO();
        address.setHouseNumber("1");
        address.setStreet("Street");
        address.setCity("City");
        address.setCountry("Country");
        address.setPostCode("123");
        request.setAddress(address);
        ContactsDTO contacts = new ContactsDTO();
        contacts.setPhone("123");
        contacts.setEmail("test@test.com");
        request.setContacts(contacts);
        ArrivalTimeDTO arrival = new ArrivalTimeDTO();
        arrival.setCheckIn("14:00");
        arrival.setCheckOut("12:00");
        request.setArrivalTime(arrival);

        String response = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/hotels/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Hotel"));
    }

    @Test
    void shouldAddAmenities() throws Exception {
        HotelRequest request = new HotelRequest();
        request.setName("Amenity Hotel");
        request.setDescription("For amenities test");
        request.setBrand("TestBrand");
        AddressDTO address = new AddressDTO();
        address.setHouseNumber("2");
        address.setStreet("Another St");
        address.setCity("TestCity");
        address.setCountry("TestCountry");
        address.setPostCode("456");
        request.setAddress(address);
        ContactsDTO contacts = new ContactsDTO();
        contacts.setPhone("456");
        contacts.setEmail("amenity@test.com");
        request.setContacts(contacts);
        ArrivalTimeDTO arrival = new ArrivalTimeDTO();
        arrival.setCheckIn("15:00");
        arrival.setCheckOut("11:00");
        request.setArrivalTime(arrival);

        String response = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(post("/hotels/" + id + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Free WiFi\", \"Pool\"]"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/hotels/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities").value(org.hamcrest.Matchers.hasItem("Pool")));
    }
}