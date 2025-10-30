package com.milano.milanopastas.repository;

import com.milano.milanopastas.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}

