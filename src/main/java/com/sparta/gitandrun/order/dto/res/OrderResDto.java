package com.sparta.gitandrun.order.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResDto {
    private Long orderId;
    private String status;
    private String type;
    private List<OrderMenuResDto> orderMenuResDtos;
}
