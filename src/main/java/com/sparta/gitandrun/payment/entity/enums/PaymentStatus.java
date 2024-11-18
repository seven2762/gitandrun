package com.sparta.gitandrun.payment.entity.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {

    PENDING(Status.PENDING),
    CANCEL(Status.CANCEL),
    PAID(Status.PAID);

    public final String status;

    public static class Status {
        public static final String PENDING = "PAY_PENDING";
        public static final String CANCEL = "PAY_CANCEL";
        public static final String PAID = "PAY_PAID";
    }

    public static PaymentStatus fromString(String status) {
        return switch (status.toUpperCase()) {
            case Status.PENDING -> PENDING;
            case Status.CANCEL -> CANCEL;
            default -> PAID;
        };
    }

}
