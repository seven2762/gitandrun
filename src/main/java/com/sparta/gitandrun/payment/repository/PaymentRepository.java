package com.sparta.gitandrun.payment.repository;

import com.sparta.gitandrun.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.id = :paymentId and p.paymentStatus = 'PAID'")
    Optional<Payment> findPaidPaymentById(@Param("paymentId") Long paymentId);

    @Query("select p from Payment p where p.id = :paymentId and p.user.userId = :userId and p.paymentStatus = 'PAID'")
    Optional<Payment> findPaidPaymentByIdAndUserId(@Param("paymentId") Long paymentId, @Param("userId") Long userId);
}
