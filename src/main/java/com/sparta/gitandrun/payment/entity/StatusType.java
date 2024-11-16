package com.sparta.gitandrun.payment.entity;

public enum StatusType {
    PAID, CANCEL, ALL;

    public static StatusType fromString(String status) {
        if (status == null) return PAID;

        return switch (status.toUpperCase()) {
            case "CANCEL" -> CANCEL;
            case "ALL" -> ALL;
            default -> PAID;
        };
    }
}
