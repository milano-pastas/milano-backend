package com.milano.milanopastas.test;

import com.milano.milanopastas.model.Order;
import com.milano.milanopastas.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestMailController {

    private final NotificationService notificationService;

    @GetMapping("/mail")
    public String testMail() {
        notificationService.notifyNewOrder(
                Order.builder()
                        .id(1L)
                        .buyerName("Matias Camacho")
                        .buyerEmail("matiasca444@gmail.com")
                        .total(new BigDecimal("450.00"))
                        .build()
        );
        return "Email enviado correctamente";
    }
}

