package com.example.taskqueue.user.entity;

public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER");

    private final String key;

    Role(String key) {
        this.key=key;
    }
    public String getKey() {
        return key;
    }
}
