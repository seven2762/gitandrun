package com.sparta.gitandrun.order.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuResDto {
    @JsonIgnore
    private Long orderId;
    private Long orderMenuId;
    private UUID menuId;
    private String menuName;
    private int menuPrice;
    private int count;
}
