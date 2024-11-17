package com.sparta.gitandrun.order.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReqOrderCondByOwnerDTO {

    private Store store;
    private Condition condition;

    @Getter
    @NoArgsConstructor
    public static class Store {
        private UUID id;
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String status;
        private String type;
        private String sort;
    }
}
