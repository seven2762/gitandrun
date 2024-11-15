package com.sparta.gitandrun.order.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ReqOrderPostDTO {

    private Type type;
    private List<OrderItem> orderItems;

    @Getter
    public static class Type {
        private boolean type;
    }

    @Getter
    public static class OrderItem {
        private UUID menuId;
        private int count;
    }
}
