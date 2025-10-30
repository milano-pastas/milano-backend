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

//    @ManyToOne(optional=false, fetch = FetchType.LAZY)
//    @JoinColumn(name="category_id")
//    private Category category;

    @Column(nullable=false, precision = 12, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    private String unit; // "gr", "un", "kg", etc.

    @Column(nullable=false)
    private boolean active = true;
}