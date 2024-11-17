package com.sparta.gitandrun.order.repository;

import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.repository.queryDsl.OrderCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    Optional<Order> findByIdAndIsDeletedFalse(Long orderId);

    Optional<Order> findByIdAndUser_UserId(Long orderId, Long userId);

    Optional<Order> findByIdAndStore_User_UserId(Long orderId, Long userId);
}
