package com.sparta.gitandrun.payment.repository;

import com.sparta.gitandrun.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdAndIsPaidTrue(Long paymentId);

    Optional<Payment> findByIdAndUser_UserIdAndIsPaidTrue(Long paymentId, Long userId);
}
