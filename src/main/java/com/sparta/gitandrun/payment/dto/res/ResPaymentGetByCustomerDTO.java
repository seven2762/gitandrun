package com.sparta.gitandrun.payment.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.gitandrun.payment.entity.Payment;
import com.sparta.gitandrun.payment.entity.PaymentStatus;
import com.sparta.gitandrun.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResPaymentGetByCustomerDTO {

    private PaymentPage paymentPage;

    public static ResPaymentGetByCustomerDTO of(Page<Payment> paymentPage, Store store) {
        return ResPaymentGetByCustomerDTO.builder()
                .paymentPage(new PaymentPage(paymentPage, store))
                .build();
    }

    @Getter
    public static class PaymentPage extends PagedModel<PaymentPage.PaymentSummary> {

        public PaymentPage(Page<Payment> paymentPage, Store store) {
            super(
                    new PageImpl<>(
                            PaymentSummary.from(paymentPage.getContent(), store),
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

            private static List<PaymentSummary> from(List<Payment> payments, Store store) {
                return payments.stream()
                        .map(payment -> PaymentSummary.from(payment, store))
                        .toList();
            }

            private static PaymentSummary from(Payment payment, Store store) {
                return PaymentSummary.builder()
                        .paymentDTO(PaymentDTO.from(payment))
                        .storeDTO(StoreDTO.from(store))
                        .build();
            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            private static class PaymentDTO {

                private Long paymentId;
                private int paymentPrice;
                private String paymentStatus;
                private LocalDateTime createdAt;
                private LocalDateTime cancelAt;

                private static PaymentDTO from(Payment payment) {
                    return PaymentDTO.builder()
                            .paymentId(payment.getId())
                            .paymentPrice(payment.getPaymentPrice())
                            .paymentStatus(payment.getPaymentStatus().status)
                            .createdAt(payment.getCreatedAt())
                            .cancelAt(
                                    payment.getPaymentStatus() == PaymentStatus.CANCEL
                                            ? payment.getUpdatedAt() : null
                            )
                            .build();
                }

            }

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            private static class StoreDTO {

                private String name;

                private static StoreDTO from(Store store) {
                    return StoreDTO.builder()
                            .name(store.getStoreName())
                            .build();
                }
            }
        }

    }
}
