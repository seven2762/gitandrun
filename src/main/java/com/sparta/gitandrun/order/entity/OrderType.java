package com.sparta.gitandrun.order.entity;

public enum OrderType {
    DELIVERY(Type.DELIVERY),
    VISIT(Type.VISIT);

    private final String type;

    OrderType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static class Type {
        public static final String DELIVERY = "DELIVERY";
        public static final String VISIT = "VISIT";
    }
}
