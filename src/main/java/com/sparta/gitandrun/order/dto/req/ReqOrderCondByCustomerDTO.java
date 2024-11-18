package com.sparta.gitandrun.order.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqOrderCondByCustomerDTO {

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
        private String type;
        private String sort;
    }
}
