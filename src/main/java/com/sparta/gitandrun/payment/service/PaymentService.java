package com.sparta.gitandrun.payment.service;

import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderMenu;
import com.sparta.gitandrun.order.entity.OrderStatus;
import com.sparta.gitandrun.order.repository.OrderMenuRepository;
import com.sparta.gitandrun.order.repository.OrderRepository;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentPostDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.repository.PaymentRepository;
import com.sparta.gitandrun.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Payment-Service")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional
    public void createPayment(User user, ReqPaymentPostDTO dto) {

        List<OrderMenu> orderMenus = orderMenuRepository.findByOrderId(dto.getOrderInfo().getOrderId());

        Payment payment = Payment.createPayment(dto.getOrderInfo().getPrice(), orderMenus, user);

        paymentRepository.save(payment);
    }
}
