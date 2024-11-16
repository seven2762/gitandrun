package com.sparta.gitandrun.payment.entity;

public enum PaymentStatus {

    PENDING(Status.PENDING),
    CANCEL(Status.CANCEL),
    PAID(Status.PAID);

    private String status;

    PaymentStatus (String status) {
        this.status = status;
    }

    public static class Status {
        public static final String PENDING = "PAY_PENDING";
        public static final String CANCEL = "PAY_CANCEL";
        public static final String PAID = "PAY_PAID";
    }

}
