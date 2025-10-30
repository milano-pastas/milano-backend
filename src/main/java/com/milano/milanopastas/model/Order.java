package com.milano.milanopastas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // datos del comprador sin registro
    @Column(nullable=false, length=120)
    private String buyerName;

    @Column(nullable=false, length=120)
    private String buyerEmail;

    @Column(length=20)
    private String buyerPhone;

    @Embedded
    private Address deliveryAddress;

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal subtotal;

    @Column(precision=12, scale=2)
    private BigDecimal deliveryFee;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private String paymentProvider;   // "MERCADOPAGO"
    private String paymentIntentId;   // ID preferencia o transacción
    private String paymentReceipt;    // ID del pago aprobado

    @Column(nullable=false)
    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

}
