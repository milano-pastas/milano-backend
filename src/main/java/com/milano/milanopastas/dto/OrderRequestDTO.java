package com.milano.milanopastas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String buyerName;

    @Email(message = "El correo no es válido")
    private String buyerEmail;

    @Pattern(regexp = "^[0-9]{8,15}$", message = "Teléfono inválido")
    private String buyerPhone;

    @Valid
    private AddressDTO deliveryAddress;

    @NotEmpty(message = "Debe incluir al menos un ítem")
    private List<OrderItemDTO> items;

    private BigDecimal deliveryFee;
}
