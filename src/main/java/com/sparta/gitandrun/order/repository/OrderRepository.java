package com.sparta.gitandrun.order.repository;

import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.repository.custom.OrderCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderCustomRepository {

    Optional<Order> findByIdAndIsDeletedFalse(UUID orderId);

    Optional<Order> findByIdAndUser_UserId(UUID orderId, Long userId);

    Optional<Order> findByIdAndStore_User_UserId(UUID orderId, Long userId);
}
