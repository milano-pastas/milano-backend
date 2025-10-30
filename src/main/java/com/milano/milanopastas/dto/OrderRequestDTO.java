package com.milano.milanopastas.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderRequestDTO {
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private AddressDTO deliveryAddress;
    private List<OrderItemDTO> items;
    private BigDecimal deliveryFee;
}
