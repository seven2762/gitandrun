package com.sparta.gitandrun.payment.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqPaymentCondByCustomerDTO {

    private Store store;
    private Condition condition;

    @Getter
    @NoArgsConstructor
    public static class Store {
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String status;
        private String sortType;
    }
}
