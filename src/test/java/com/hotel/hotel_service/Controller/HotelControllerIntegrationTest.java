package com.hotel.hotel_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldCreateAndRetrieveHotel() throws Exception {
        HotelRequest request = new HotelRequest();
        request.setName("Integration Hotel");
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
        request.setArrivalTime(arrival);

        String response = mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/property-view/hotels/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Hotel"));
    }

    @Test
    void shouldAddAmenities() throws Exception {
        mockMvc.perform(post("/property-view/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Free WiFi\", \"Pool\"]"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities").value(org.hamcrest.Matchers.hasItem("Pool")));
    }
}
