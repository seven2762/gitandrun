package com.sparta.gitandrun.payment.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReqPaymentCondDTO {
    private String paymentStatus;
    private String sortType;
}
