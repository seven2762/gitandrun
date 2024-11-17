package com.sparta.gitandrun.order.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqOrderCondByCustomerDTO {

    private Condition condition;

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String status;
        private String type;
        private String sort;
    }
}
