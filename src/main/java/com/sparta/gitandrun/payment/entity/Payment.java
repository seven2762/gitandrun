package com.sparta.gitandrun.payment.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import com.sparta.gitandrun.order.entity.Order;
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

    @Column(name = "is_paid")
    private boolean isPaid;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "p_order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "p_user_id")
    private User user;

    @Builder
    private Payment(int paymentPrice, Order order, User user) {
        this.paymentPrice = paymentPrice;
        this.isPaid = true;
        this.order = order;
        this.user = user;
        initAuditInfo(user);
    }

    // == 생성 메서드 == //
    public static Payment createPayment(int paymentPrice, Order order, User user) {
        return Payment.builder()
                .paymentPrice(paymentPrice)
                .order(order.completeOrder())
                .user(user)
                .build();
    }
}
