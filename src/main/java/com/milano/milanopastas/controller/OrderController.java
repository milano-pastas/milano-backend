package com.milano.milanopastas.controller;

import com.milano.milanopastas.dto.OrderRequestDTO;
import com.milano.milanopastas.dto.OrderResponseDTO;
import com.milano.milanopastas.mapper.OrderMapper;
import com.milano.milanopastas.model.Order;
import com.milano.milanopastas.repository.OrderRepository;
import com.milano.milanopastas.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;
    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    //publico
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO dto,
                                         @RequestParam(required = false) String customer_comment,
                                         @RequestParam String captchaToken) {
        if (!validateCaptcha(captchaToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Captcha inválido"));
        }
        if (customer_comment != null && !customer_comment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Bot detected"));
        }
        Order order = orderMapper.toEntity(dto);
        Order saved = orderRepository.save(order);
        notificationService.notifyNewOrder(saved);
        return ResponseEntity.ok(orderMapper.toResponse(saved));
    }

    private boolean validateCaptcha(String token) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify";
            var body = Map.of("secret", recaptchaSecret, "response", token);
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "secret=" + recaptchaSecret + "&response=" + token))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().contains("\"success\": true");
        } catch (Exception e) {
            return false;
        }
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
