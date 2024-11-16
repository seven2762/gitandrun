package com.sparta.gitandrun.payment.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderStatus;
import com.sparta.gitandrun.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_price", nullable = false)
    private int paymentPrice;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "payment_status")
    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "p_order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "p_user_id")
    private User user;

    @Builder
    private Payment(int paymentPrice, Order order, User user) {
        this.paymentPrice = paymentPrice;
        this.paymentStatus = PaymentStatus.PAID;
        this.order = order;
        this.user = user;
        initAuditInfo(user);
    }

    // == 생성 메서드 == //
    public static Payment createPayment(int paymentPrice, Order order, User user) {

        // 주문 금액 확인
        if (paymentPrice != order.getTotalPrice()) {
            throw new IllegalArgumentException("결제 금액이 주문 총 금액과 일치하지 않습니다.");
        }

        // 주문 상태 확인
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("결제가 불가능한 상태의 주문입니다.");
        }

        return Payment.builder()
                .paymentPrice(paymentPrice)
                .order(order.completeOrder())
                .user(user)
                .build();
    }


    // == 결제 취소 메서드 == //
    public void cancelPayment(User user) {
        this.paymentStatus = PaymentStatus.CANCEL;
        this.order.cancelOrder();
        setUpdatedBy(user.getUsername());
    }
}
