package com.sparta.gitandrun.payment.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqPaymentPostDTO {

    private OrderInfo orderInfo;

    @Getter
    @NoArgsConstructor
    public static class OrderInfo {
        private Long orderId;
        private int price;
    }
}
