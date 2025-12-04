package com.bizzfilling.app.api.models;

public class SignupRequest {
    private String fullName;
    private String email;
    private String password;
    private String role;

    public SignupRequest(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
