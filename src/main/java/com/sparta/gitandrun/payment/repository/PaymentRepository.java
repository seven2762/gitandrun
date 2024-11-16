package com.sparta.gitandrun.payment.repository;

import com.sparta.gitandrun.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
