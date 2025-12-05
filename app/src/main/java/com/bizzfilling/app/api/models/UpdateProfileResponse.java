package com.bizzfilling.app.api.models;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse {
    @SerializedName("user")
    private User user;

    @SerializedName("message")
    private String message;

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
