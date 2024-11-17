package com.sparta.gitandrun.payment.dto.req;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqPaymentCondByManagerDTO {

    @Valid
    @NotNull(message = "고객 정보를 입력해주세요")
    private Customer customer;
    private Condition condition;

    @Getter
    @NoArgsConstructor
    public static class Customer {
        @NotNull(message = "id 를 입력해주세요.")
        private Long id;
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String status;
        private String sortType;
        private boolean deleted;
    }
}
