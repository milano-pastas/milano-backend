package com.milano.milanopastas.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String name;

    @Column(length=500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    private Category category = Category.OTROS;

    @Column(nullable=false, precision = 12, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    private String unit; // 0 si kilos, numeros de unidades sino

    @Column(nullable=false)
    private boolean active = true;

    @Column(length=500)
    private String sabores;
}