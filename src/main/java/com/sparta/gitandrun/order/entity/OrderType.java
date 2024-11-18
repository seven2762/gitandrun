package com.sparta.gitandrun.order.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum OrderType {
    DELIVERY(Type.DELIVERY),
    VISIT(Type.VISIT);

    private final String type;

    OrderType(String type) {
        this.type = type;
    }

    public static class Type {
        public static final String DELIVERY = "DELIVERY";
        public static final String VISIT = "VISIT";
    }

    public static OrderType fromString(String type) {
        return Objects.equals(type.toUpperCase(), OrderType.DELIVERY.getType())
                ? DELIVERY : VISIT;
    }
}
