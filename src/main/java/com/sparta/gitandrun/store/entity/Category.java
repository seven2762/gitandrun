package com.sparta.gitandrun.store.entity;

import java.util.UUID;

public enum Category {
    KOREAN(UUID.fromString("11111111-1111-1111-1111-111111111111")),
    CHINESE(UUID.fromString("22222222-2222-2222-2222-222222222222")),
    SNACK(UUID.fromString("33333333-3333-3333-3333-333333333333")),
    CHICKEN(UUID.fromString("44444444-4444-4444-4444-444444444444")),
    PIZZA(UUID.fromString("55555555-5555-5555-5555-555555555555"));

    private final UUID uuid;

    Category(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
