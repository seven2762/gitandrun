package com.sparta.gitandrun.payment.entity;

public enum SortType {
    LATEST,
    OLDEST,
    PRICE_HIGH,
    PRICE_LOW;

    public static SortType fromString(String order) {
        if (order == null) return LATEST;

        return switch (order.toUpperCase()) {
            case "OLDEST" -> OLDEST;
            case "PRICE_HIGH" -> PRICE_HIGH;
            case "PRICE_LOW" -> PRICE_LOW;
            default -> LATEST;
        };
    }
}
