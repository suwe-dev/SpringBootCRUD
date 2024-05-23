package com.example.crud2.model;

public class PostModel {
    private final long primary_key;
    private final String message;

    public PostModel(long primary_key, String message) {
        this.primary_key = primary_key;
        this.message = message;
    }

    public long getPrimary_key() {
        return primary_key;
    }

    public String getMessage() {
        return message;
    }
}
