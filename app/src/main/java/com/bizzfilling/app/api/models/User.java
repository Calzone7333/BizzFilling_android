package com.bizzfilling.app.api.models;

public class User {
    @com.google.gson.annotations.SerializedName("id")
    private String id;

    @com.google.gson.annotations.SerializedName("fullName")
    private String fullName;

    @com.google.gson.annotations.SerializedName("email")
    private String email;

    @com.google.gson.annotations.SerializedName("role")
    private String role;

    @com.google.gson.annotations.SerializedName("status")
    private String status;

    @com.google.gson.annotations.SerializedName("walletBalance")
    private Double walletBalance;

    @com.google.gson.annotations.SerializedName("phone")
    private String phone;

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }
}
