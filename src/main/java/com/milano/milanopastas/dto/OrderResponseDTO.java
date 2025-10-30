package com.milano.milanopastas.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponseDTO {
    private Long id;
    private String buyerName;
    private String buyerEmail;
    private BigDecimal total;
    private String status;
    private OffsetDateTime createdAt;
    private List<OrderItemDTO> items;
}
