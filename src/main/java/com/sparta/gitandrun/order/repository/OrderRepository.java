package com.sparta.gitandrun.order.repository;

import com.sparta.gitandrun.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.id = :orderId and o.isDeleted = false ")
    Optional<Order> findOrderById(@Param("orderId") Long orderId);
}
