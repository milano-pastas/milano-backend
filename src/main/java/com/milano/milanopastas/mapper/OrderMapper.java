package com.milano.milanopastas.mapper;

import com.milano.milanopastas.dto.*;
import com.milano.milanopastas.model.*;
import com.milano.milanopastas.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductRepository productRepository;

    public Order toEntity(OrderRequestDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        // Crear entidad base
        Order order = Order.builder()
                .buyerName(dto.getBuyerName())
                .buyerEmail(dto.getBuyerEmail())
                .buyerPhone(dto.getBuyerPhone())
                .deliveryAddress(toEntity(dto.getDeliveryAddress()))
                .deliveryFee(dto.getDeliveryFee() != null ? dto.getDeliveryFee() : BigDecimal.ZERO)
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        // Construir ítems a partir del DTO
        List<OrderItem> items = dto.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + itemDto.getProductId()));

                    BigDecimal lineTotal = product.getPrice()
                            .multiply(BigDecimal.valueOf(itemDto.getQuantity()));

                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(itemDto.getQuantity())
                            .unitPrice(product.getPrice())
                            .lineTotal(lineTotal)
                            .build();
                })
                .toList();

        // Calcular subtotal y total
        BigDecimal subtotal = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setSubtotal(subtotal);
        order.setTotal(subtotal.add(order.getDeliveryFee()));

        return order;
    }

    public OrderResponseDTO toResponse(Order order) {
        if (order == null) return null;

        return OrderResponseDTO.builder()
                .id(order.getId())
                .buyerName(order.getBuyerName())
                .buyerEmail(order.getBuyerEmail())
                .status(order.getStatus().name())
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(this::toItemDTO)
                        .toList())
                .build();
    }

    private Address toEntity(AddressDTO dto) {
        if (dto == null) return null;

        return Address.builder()
                .street(dto.getStreet())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .notes(dto.getNotes())
                .build();
    }

    private OrderItemDTO toItemDTO(OrderItem item) {
        if (item == null) return null;

        return OrderItemDTO.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .build();
    }
}
