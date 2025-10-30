package com.milano.milanopastas.service;

import com.milano.milanopastas.model.*;
import com.milano.milanopastas.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final NotificationService notificationService;

    @Transactional
    public Order createOrder(String buyerName, String buyerEmail, String buyerPhone,
                             Address address, List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("El pedido no puede estar vacío");
        }

        Order order = new Order();
        order.setBuyerName(buyerName);
        order.setBuyerEmail(buyerEmail);
        order.setBuyerPhone(buyerPhone);
        order.setDeliveryAddress(address);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItem item : items) {
            Product product = productService.findById(item.getProduct().getId());

            item.setOrder(order);
            item.setUnitPrice(product.getPrice());
            item.setLineTotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            subtotal = subtotal.add(item.getLineTotal());
        }

        order.setItems(items);
        order.setSubtotal(subtotal);
        order.setDeliveryFee(BigDecimal.ZERO); // por ahora sin costo de envío
        order.setTotal(subtotal);

        Order saved = orderRepository.save(order);

        // Notificar cliente y fábrica
        notificationService.notifyNewOrder(saved);

        return saved;
    }

    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
