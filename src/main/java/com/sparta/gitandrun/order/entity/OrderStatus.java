package com.sparta.gitandrun.order.entity;

public enum OrderStatus {

    PENDING(Status.PENDING),
    CANCEL(Status.CANCEL),
    COMPLETED(Status.COMPLETED),
    REJECT(Status.REJECT);

    public final String status;

    OrderStatus (String status) {
        this.status = status;
    }

    public static class Status {
        public static final String PENDING = "ORDER_PENDING";
        public static final String CANCEL = "ORDER_CANCEL";
        public static final String COMPLETED = "ORDER_COMPLETED";
        public static final String REJECT = "ORDER_REJECT";
    }

    public static OrderStatus fromString(String status) {
        return switch (status.toUpperCase()) {
            case Status.CANCEL -> CANCEL;
            case Status.PENDING ->  PENDING;
            case Status.REJECT -> REJECT;
            default -> COMPLETED;
        };
    }
}
