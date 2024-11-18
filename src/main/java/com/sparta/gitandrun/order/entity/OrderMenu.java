package com.sparta.gitandrun.order.entity;

import com.sparta.gitandrun.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "p_order_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int orderPrice;

    @Column(nullable = false)
    private int orderCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "p_order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "p_menu_id")
    private Menu menu;

    @Builder
    public OrderMenu(Menu menu, int orderCount, int orderPrice) {
        this.menu = menu;
        this.orderCount = orderCount;
        this.orderPrice = orderPrice;
    }

    // == 연관관계 메서드 == //
    public void updateOrder(Order order) {
        this.order = order;
    }
}
