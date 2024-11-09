package com.sparta.gitandrun.order.entity;

import com.sparta.gitandrun.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "p_order_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // == 생성 메서드 == //
    public static List<OrderMenu> createOrderMenus(List<Menu> menus, Map<Menu, Long> menuCountMap) {

        return menus.stream()
                .map(
                        menu -> {
                            int count = menuCountMap.get(menu).intValue();

                            return OrderMenu.builder()
                                    .menu(menu)
                                    .orderCount(count)
                                    .orderPrice(menu.getPrice() * count)
                                    .build();
                        }
                )
                .toList();
    }
}
