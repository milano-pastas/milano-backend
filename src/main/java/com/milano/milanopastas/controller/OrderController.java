package com.milano.milanopastas.controller;

import com.milano.milanopastas.dto.OrderRequestDTO;
import com.milano.milanopastas.dto.OrderResponseDTO;
import com.milano.milanopastas.mapper.OrderMapper;
import com.milano.milanopastas.model.Order;
import com.milano.milanopastas.repository.OrderRepository;
import com.milano.milanopastas.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;

    //publico
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO dto) {
        Order order = orderMapper.toEntity(dto);
        Order saved = orderRepository.save(order);
        notificationService.notifyNewOrder(saved);
        return ResponseEntity.ok(orderMapper.toResponse(saved));
    }

    //admin
    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    //admin
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
