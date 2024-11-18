package com.sparta.gitandrun.payment.repository.custom;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByManagerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByCustomerDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.entity.enums.PaymentStatus;
import com.sparta.gitandrun.payment.entity.enums.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.gitandrun.order.entity.QOrder.order;
import static com.sparta.gitandrun.payment.entity.QPayment.payment;
import static com.sparta.gitandrun.store.entity.QStore.store;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Payment> findMyPaymentsWithConditions(Long userId,
                                                      ReqPaymentCondByCustomerDTO cond,
                                                      Pageable pageable) {

        List<Payment> results = queryFactory
                .selectFrom(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
                        payment.user.userId.eq(userId),
                        payment.isDeleted.eq(false),
                        storeNameLike(cond.getStore().getName()),
                        statusEq(cond.getCondition().getStatus())
                )
                .orderBy(orderSpecifier(cond.getCondition().getSortType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
                        payment.user.userId.eq(userId),
                        payment.isDeleted.eq(false),
                        storeNameLike(cond.getStore().getName()),
                        statusEq(cond.getCondition().getStatus())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Payment> findAllPaymentsWithConditions(ReqPaymentCondByManagerDTO cond, Pageable pageable) {

        List<Payment> results = queryFactory
                .selectFrom(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
                        usernameLike(cond.getCustomer().getName()),
                        storeNameLike(cond.getStore().getName()),
                        deletedEq(cond.getCondition().isDeleted()),
                        userIdEq(cond.getCustomer().getId()),
                        statusEq(cond.getCondition().getStatus())
                )
                .orderBy(orderSpecifier(cond.getCondition().getSortType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
                        usernameLike(cond.getCustomer().getName()),
                        storeNameLike(cond.getStore().getName()),
                        deletedEq(cond.getCondition().isDeleted()),
                        userIdEq(cond.getCustomer().getId()),
                        statusEq(cond.getCondition().getStatus())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression usernameLike(String username) {
        return !hasText(username) ? null : payment.user.username.containsIgnoreCase(username);
    }

    private BooleanExpression storeNameLike(String storeName) {
        return !hasText(storeName) ? null : payment.order.store.storeName.containsIgnoreCase(storeName);
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? payment.user.userId.eq(userId) : null;
    }

    private BooleanExpression deletedEq(boolean cond) {
        return payment.isDeleted.eq(cond);
    }

    private BooleanExpression statusEq(String status) {
        return status != null ? payment.paymentStatus.eq(PaymentStatus.fromString(status)) : null;
    }

    private OrderSpecifier<?> orderSpecifier(String order) {
        SortType sortType = SortType.fromString(order);

        return switch (sortType) {
            case LATEST -> payment.createdAt.desc();
            case OLDEST -> payment.createdAt.asc();
            case PRICE_HIGH -> payment.paymentPrice.desc();
            case PRICE_LOW -> payment.paymentPrice.asc();
        };
    }
}
