package com.sparta.gitandrun.payment.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReqPaymentPostDTO {

    private OrderInfo orderInfo;

    @Getter
    @NoArgsConstructor
    public static class OrderInfo {
        private UUID orderId;
        private int price;
    }
}
