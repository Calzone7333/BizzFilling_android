package com.bizzfilling.app.api.models;

public class LoginResponse {
    @com.google.gson.annotations.SerializedName("token")
    private String token;

    @com.google.gson.annotations.SerializedName("user")
    private User user;

    @com.google.gson.annotations.SerializedName("message")
    private String message;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
