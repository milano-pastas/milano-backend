package com.milano.milanopastas.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AddressDTO {
    private String street;
    private Double latitude;
    private Double longitude;
    private String notes;
}
