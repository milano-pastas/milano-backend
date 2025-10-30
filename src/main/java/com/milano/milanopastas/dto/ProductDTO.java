package com.milano.milanopastas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;

    @NotNull()
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private String unit;       // "gr", "un", "kg", etc.
    private String imageUrl;

    private boolean active;
}
