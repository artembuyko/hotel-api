package com.hotel.hotel_service.Models.Entityes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Contacts {
    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;
}
