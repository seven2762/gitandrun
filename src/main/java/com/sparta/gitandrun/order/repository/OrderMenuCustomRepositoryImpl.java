package com.sparta.gitandrun.order.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gitandrun.order.entity.OrderMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.sparta.gitandrun.order.entity.QOrderMenu.orderMenu;
import static com.sparta.gitandrun.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class OrderMenuCustomRepositoryImpl implements OrderMenuCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderMenu> findOrderMenusByStoreId(UUID storeId, List<UUID> storeIds) {
        BooleanBuilder builder = new BooleanBuilder();

        if (storeId != null) {
            builder.and(store.storeId.eq(storeId));
        }

        if (storeIds != null && !storeIds.isEmpty()) {
            builder.and(store.storeId.in(storeIds));
        }

        return queryFactory
                .selectFrom(orderMenu)
                .join(orderMenu.order).fetchJoin()
                .join(orderMenu.menu).fetchJoin()
                .join(orderMenu.menu.store, store).fetchJoin()
                .where(builder)
                .fetch();
    }
}
