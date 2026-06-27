package com.hotel.hotel_service.Models.Entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ArrivalTime {
    @Column(nullable = false)
    private String checkIn;

    @Column(nullable = false)
    private String checkOut;
}
