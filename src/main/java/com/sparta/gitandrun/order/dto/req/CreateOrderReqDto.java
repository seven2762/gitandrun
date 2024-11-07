package com.sparta.gitandrun.order.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderReqDto {
    private Long menuId;
    private int count;
    private boolean isType;
}
