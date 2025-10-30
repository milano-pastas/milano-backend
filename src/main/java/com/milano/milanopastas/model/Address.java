package com.milano.milanopastas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {
    @Column(nullable = false)
    private String street;        // “Av. Italia 2540”
    private Double latitude;      // -34.9011
    private Double longitude;     // -56.1645
    private String notes;         // Ej: "Puerta verde, timbre 2"
}
