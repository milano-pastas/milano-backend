package com.milano.milanopastas.repository;

import java.util.List;
import com.milano.milanopastas.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByNameContainingIgnoreCase(String name);
}
