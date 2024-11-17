package com.sparta.gitandrun.payment.repository.queryDsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondByManagerDTO;
import com.sparta.gitandrun.payment.dto.req.ReqPaymentCondDTO;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.entity.enums.PaymentStatus;
import com.sparta.gitandrun.payment.entity.enums.SortType;
import com.sparta.gitandrun.payment.entity.enums.StatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.gitandrun.order.entity.QOrder.order;
import static com.sparta.gitandrun.payment.entity.QPayment.payment;
import static com.sparta.gitandrun.store.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Payment> findMyPaymentsWithConditions(Long userId,
                                                      ReqPaymentCondDTO cond,
                                                      Pageable pageable) {

        List<Payment> results = queryFactory
                .selectFrom(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
                        deletedFalse(),
                        userIdEq(userId),
                        statusEq(cond.getPaymentStatus())
                )
                .orderBy(orderSpecifier(cond.getSortType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(payment.count())
                .from(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
                        userIdEq(userId),
                        statusEq(cond.getPaymentStatus())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Payment> findCustomerPaymentsWithConditions(ReqPaymentCondByManagerDTO cond,
                                                            Pageable pageable) {

        List<Payment> results = queryFactory
                .selectFrom(payment)
                .join(payment.order, order).fetchJoin()
                .join(payment.order.store, store).fetchJoin()
                .where(
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
                        userIdEq(cond.getCustomer().getId()),
                        statusEq(cond.getCondition().getStatus())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression deletedFalse() {
        return payment.isDeleted.eq(false);
    }

    private BooleanExpression deletedEq(boolean cond) {
        return cond ? payment.isDeleted.eq(true) : payment.isDeleted.eq(false);
    }

    private BooleanExpression userIdEq(Long userId) {
        return payment.user.userId.eq(userId);
    }

    private BooleanExpression statusEq(String status) {
        StatusType statusType = StatusType.fromString(status);

        return switch (statusType) {
            case PAID -> payment.paymentStatus.eq(PaymentStatus.PAID);
            case CANCEL -> payment.paymentStatus.eq(PaymentStatus.CANCEL);
            case ALL -> payment.paymentStatus.in(PaymentStatus.PAID, PaymentStatus.CANCEL);
        };
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
