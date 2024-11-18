package com.sparta.gitandrun.payment.repository.custom;

import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByManagerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByCustomerDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentCustomRepository {

    Page<Payment> findMyPaymentsWithConditions(Long userId, ReqPaymentCondByCustomerDTO cond, Pageable pageable);

    Page<Payment> findCustomerPaymentsWithConditions(ReqPaymentCondByManagerDTO condition, Pageable pageable);
}
