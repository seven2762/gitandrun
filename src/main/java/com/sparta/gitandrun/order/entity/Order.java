package com.sparta.gitandrun.order.entity;

import com.sparta.gitandrun.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Getter
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;

    private boolean isDeleted;

    @OneToMany(mappedBy = "order", cascade = PERSIST, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();
}
