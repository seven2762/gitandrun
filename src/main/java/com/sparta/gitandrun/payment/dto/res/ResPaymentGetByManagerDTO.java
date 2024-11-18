package com.sparta.gitandrun.payment.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.entity.enums.PaymentStatus;
import com.sparta.gitandrun.store.entity.Store;
import com.sparta.gitandrun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPaymentGetByManagerDTO {

    private PaymentPage paymentPage;

    public static ResPaymentGetByManagerDTO of(Page<Payment> paymentPage) {
        return ResPaymentGetByManagerDTO.builder()
                .paymentPage(new PaymentPage(paymentPage))
                .build();
    }

    @Getter
    public static class PaymentPage extends PagedModel<PaymentPage.PaymentSummary> {
        public PaymentPage(Page<Payment> paymentPage) {
            super(
                    new PageImpl<>(
                            PaymentSummary.from(paymentPage.getContent()),
                            paymentPage.getPageable(),
                            paymentPage.getTotalElements()
                    )
            );
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class PaymentSummary {

            private PaymentDTO paymentDTO;
            private StoreDTO storeDTO;

            private static List<PaymentSummary> from(List<Payment> payments) {
                return payments.stream()
                        .map(PaymentSummary::from)
                        .toList();
            }

            private static PaymentSummary from(Payment payment) {
                return PaymentSummary.builder()
                        .paymentDTO(PaymentDTO.from(payment))
                        .storeDTO(StoreDTO.from(payment.getOrder().getStore()))
                        .build();
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            private static class PaymentDTO {

                private UUID paymentId;
                private Customer customer;
                private int paymentPrice;
                private String paymentStatus;
                private LocalDateTime createdAt;
                private LocalDateTime cancelAt;

                private static PaymentDTO from(Payment payment) {
                    return PaymentDTO.builder()
                            .paymentId(payment.getId())
                            .customer(Customer.from(payment.getUser()))
                            .paymentPrice(payment.getPaymentPrice())
                            .paymentStatus(payment.getPaymentStatus().status)
                            .createdAt(payment.getCreatedAt())
                            .cancelAt(
                                    payment.getPaymentStatus() == PaymentStatus.CANCEL
                                            ? payment.getUpdatedAt() : null
                            )
                            .build();
                }

                @Getter
                @Builder
                @NoArgsConstructor
                @AllArgsConstructor
                private static class Customer {

                    private Long userId;
                    private String username;
                    private String phone;

                    private static Customer from(User user) {
                        return Customer.builder()
                                .userId(user.getUserId())
                                .username(user.getUsername())
                                .phone(user.getPhone())
                                .build();
                    }
                }

            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            private static class StoreDTO {

                private UUID storeId;
                private String name;

                private static StoreDTO from(Store store) {
                    return StoreDTO.builder()
                            .storeId(store.getStoreId())
                            .name(store.getStoreName())
                            .build();
                }
            }

        }
    }
}
