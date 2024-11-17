package com.sparta.gitandrun.payment.repository.queryDsl;

import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByManagerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentCustomRepository {

    Page<Payment> findMyPaymentsWithConditions(Long userId, ReqPaymentCondDTO condition, Pageable pageable);

    Page<Payment> findCustomerPaymentsWithConditions(ReqPaymentCondByManagerDTO condition, Pageable pageable);
}
