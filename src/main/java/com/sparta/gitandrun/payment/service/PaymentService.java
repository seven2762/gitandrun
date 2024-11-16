package com.sparta.gitandrun.payment.service;

import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.repository.OrderRepository;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentPostDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.repository.PaymentRepository;
import com.sparta.gitandrun.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Payment-Service")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    
    @Transactional
    public void createPayment(User user, ReqPaymentPostDTO dto) {

        Order order = getOrder(user, dto);

        Payment payment = Payment.createPayment(dto.getOrderInfo().getPrice(), order, user);

        paymentRepository.save(payment);
    }

    private Order getOrder(User user, ReqPaymentPostDTO dto) {
        Order order = orderRepository.findByIdAndUser_UserId(dto.getOrderInfo().getOrderId(), user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("접근 권한이 없습니다."));
        return order;
    }

}
