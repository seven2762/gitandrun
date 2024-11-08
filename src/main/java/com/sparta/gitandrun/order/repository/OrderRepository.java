package com.sparta.gitandrun.order.repository;

import com.sparta.gitandrun.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
