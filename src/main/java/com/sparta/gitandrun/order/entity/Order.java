package com.sparta.gitandrun.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Getter
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;

    private boolean isDeleted;

    @OneToMany(mappedBy = "order", cascade = PERSIST, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Builder
    public Order (OrderType type) {
        this.orderType = type;
    }

    public void addOrderMenus(List<OrderMenu> orderMenus) {
        this.orderMenus.addAll(orderMenus);
        orderMenus.forEach(orderMenu -> orderMenu.updateOrder(this));
    }

    // == 생성 메서드 == //
    public static Order createOrder(boolean type, List<OrderMenu> orderMenus) {
        Order order = Order.builder()
                .type(type ? OrderType.DELIVERY : OrderType.VISIT)
                .build();

        order.addOrderMenus(orderMenus);

        return order;
    }
}
