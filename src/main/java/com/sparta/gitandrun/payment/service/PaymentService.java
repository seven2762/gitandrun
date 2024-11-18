package com.sparta.gitandrun.payment.service;

import com.sparta.gitandrun.order.dto.res.ResDto;
import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderMenu;
import com.sparta.gitandrun.order.repository.OrderMenuRepository;
import com.sparta.gitandrun.order.repository.OrderRepository;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByManagerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByCustomerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentPostDTO;
import com.sparta.gitandrun.payment.dto.res.ResPaymentGetByIdDTO;
import com.sparta.gitandrun.payment.dto.res.ResPaymentGetByUserIdDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.repository.PaymentRepository;
import com.sparta.gitandrun.user.entity.Role;
import com.sparta.gitandrun.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Transactional
    public void createPayment(User user, ReqPaymentPostDTO dto) {

        Order order = getOrder(user, dto);

        Payment payment = Payment.createPayment(dto.getOrderInfo().getPrice(), order, user);

        paymentRepository.save(payment);
    }

    /*
        고객 결제 목록 조회
    */
    @Transactional(readOnly = true)
    public ResponseEntity<ResDto<ResPaymentGetByUserIdDTO>> getByCustomer(User user, ReqPaymentCondByCustomerDTO cond, Pageable pageable) {

        Page<Payment> findPaymentPage = paymentRepository.findMyPaymentsWithConditions(user.getUserId(), cond, pageable);

        return new ResponseEntity<>(
                ResDto.<ResPaymentGetByUserIdDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("결제 목록 조회에 성공했습니다.")
                        .data(ResPaymentGetByUserIdDTO.of(findPaymentPage))
                        .build(),
                HttpStatus.OK
        );
    }

    /*
        매니저 결제 목록 조회
    */
    @Transactional(readOnly = true)
    public ResponseEntity<ResDto<ResPaymentGetByUserIdDTO>> getByManager(ReqPaymentCondByManagerDTO condition, Pageable pageable) {

        Page<Payment> findPaymentPage = paymentRepository.findAllPaymentsWithConditions(condition, pageable);

        return new ResponseEntity<>(
                ResDto.<ResPaymentGetByUserIdDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("결제 목록 조회에 성공했습니다.")
                        .data(ResPaymentGetByUserIdDTO.of(findPaymentPage))
                        .build(),
                HttpStatus.OK
        );
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResDto<ResPaymentGetByIdDTO>> getBy(Long paymentId) {

        Payment findPayment = getPaymentBy(paymentId);

        List<OrderMenu> findOrderMenus = orderMenuRepository.findByOrderId(findPayment.getOrder().getId());

        return new ResponseEntity<>(
                ResDto.<ResPaymentGetByIdDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("결제 상세 조회에 성공했습니다.")
                        .data(ResPaymentGetByIdDTO.of(findPayment, findPayment.getOrder(), findOrderMenus))
                        .build(),
                HttpStatus.OK
        );
    }


    /*
        결제 취소
    */
    @Transactional
    public void cancelPayment(User user, Long paymentId) {

        Payment payment =
                user.getRole() == Role.CUSTOMER
                        ? getPayment(paymentId, user.getUserId())
                        : getPayment(paymentId);

        payment.cancelPayment(user);
    }

    @Transactional
    public void deletePayment(User user, Long paymentId) {
        getPayment(paymentId).deletePayment(user);
    }


    private Payment getPaymentBy(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 항목입니다."));
    }

    private Payment getPayment(Long paymentId) {
        return paymentRepository.findPaidPaymentById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 항목입니다."));
    }

    private Payment getPayment(Long paymentId, Long userId) {
        return paymentRepository.findPaidPaymentByIdAndUserId(paymentId, userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 항목입니다."));
    }

    private Order getOrder(User user, ReqPaymentPostDTO dto) {
        Order order = orderRepository.findByIdAndUser_UserId(dto.getOrderInfo().getOrderId(), user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("접근 권한이 없습니다."));
        return order;
    }

}
