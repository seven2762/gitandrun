package com.sparta.gitandrun.order.repository.custom;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByCustomerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByManagerDTO;
import com.sparta.gitandrun.order.dto.req.ReqOrderCondByOwnerDTO;
import com.sparta.gitandrun.order.entity.Order;
import com.sparta.gitandrun.order.entity.OrderStatus;
import com.sparta.gitandrun.order.entity.OrderType;
import com.sparta.gitandrun.payment.entity.enums.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static com.sparta.gitandrun.order.entity.QOrder.order;
import static com.sparta.gitandrun.order.entity.QOrderMenu.orderMenu;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findMyOrderListWithConditions(Long userId, ReqOrderCondByCustomerDTO cond, Pageable pageable) {

        List<Order> results = queryFactory
                .select(order)
                .from(order)
                .where(
                        order.user.userId.eq(userId),
                        deletedFalse(),
                        storeNameLike(cond.getStore().getName()),
                        statusEq(cond.getCondition().getStatus()),
                        typeEq(cond.getCondition().getType())
                )
                .orderBy(
                        orderSpecifier(cond.getCondition().getSort())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.user.userId.eq(userId),
                        deletedFalse(),
                        statusEq(cond.getCondition().getStatus()),
                        typeEq(cond.getCondition().getType())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Order> findOwnerOrderListWithConditions(Long userId, ReqOrderCondByOwnerDTO cond, Pageable pageable) {

        List<Order> results = queryFactory
                .select(order)
                .from(order)
                .where(
                        deletedFalse(),
                        usernameLike(cond.getCustomer().getName()),
                        storeIdOrUserIdEq(userId, cond.getStore().getId()),
                        statusEq(cond.getCondition().getStatus()),
                        typeEq(cond.getCondition().getType())
                )
                .orderBy(
                        orderSpecifier(cond.getCondition().getSort())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        deletedFalse(),
                        storeIdOrUserIdEq(userId, cond.getStore().getId()),
                        statusEq(cond.getCondition().getStatus()),
                        typeEq(cond.getCondition().getType())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Order> findManagerOrderListWithConditions(ReqOrderCondByManagerDTO cond, Pageable pageable) {

        List<Order> results = queryFactory
                .select(order)
                .from(order)
                .where(
                        usernameLike(cond.getCustomer().getName()),
                        storeNameLike(cond.getStore().getName()),
                        statusEq(cond.getCondition().getStatus()),
                        typeEq(cond.getCondition().getType())
                )
                .orderBy(
                        orderSpecifier(cond.getCondition().getSort())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        usernameLike(cond.getCustomer().getName()),
                        storeNameLike(cond.getStore().getName()),
                        statusEq(cond.getCondition().getStatus()),
                        typeEq(cond.getCondition().getType())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }


    private BooleanExpression storeIdOrUserIdEq(Long userId, UUID storeId) {
        if (storeId != null) {
            return order.store.storeId.eq(storeId)
                    .and(order.store.user.userId.eq(userId));
        } else {
            return order.store.user.userId.eq(userId);
        }
    }

    private BooleanExpression deletedFalse() {
        return order.isDeleted.eq(false);
    }

    private BooleanExpression usernameLike(String username) {
        return !hasText(username) ? null : order.user.username.containsIgnoreCase(username);
    }

    private BooleanExpression storeNameLike(String storeName) {
        return !hasText(storeName) ? null : order.store.storeName.containsIgnoreCase(storeName);
    }

    private BooleanExpression statusEq(String status) {
        return order.orderStatus.eq(OrderStatus.fromString(status));
    }

    private BooleanExpression typeEq(String type) {
        return order.orderType.eq(OrderType.fromString(type));
    }

    private OrderSpecifier<?> orderSpecifier(String sort) {

        SortType sortType = SortType.fromString(sort);

        return switch (sortType) {
            case OLDEST -> order.createdAt.asc();
            case PRICE_HIGH -> new OrderSpecifier<>(
                    com.querydsl.core.types.Order.DESC,
                    getOrderPriceSumByOrderIdQuery()
            );
            case PRICE_LOW -> new OrderSpecifier<>(
                    com.querydsl.core.types.Order.ASC,
                    getOrderPriceSumByOrderIdQuery()
            );
            default -> order.createdAt.desc();
        };
    }

    private JPQLQuery<Integer> getOrderPriceSumByOrderIdQuery() {
        return JPAExpressions
                .select(orderMenu.orderPrice.sum())
                .from(orderMenu)
                .where(orderMenu.order.id.eq(order.id))
                .groupBy(orderMenu.order.id);
    }
}
