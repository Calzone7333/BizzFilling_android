package com.bizzfilling.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendLink;
    private TextView tvBackToLogin;
    
    // OTP UI
    private android.widget.LinearLayout layoutEmail;
    private android.widget.LinearLayout layoutReset;
    private EditText etOtp;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private Button btnResetPassword;
    
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Views
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutReset = findViewById(R.id.layoutReset);
        
        etEmail = findViewById(R.id.etEmail);
        btnSendLink = findViewById(R.id.btnSendLink);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        
        etOtp = findViewById(R.id.etOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Check for passed email
        if (getIntent().hasExtra("email")) {
            String email = getIntent().getStringExtra("email");
            etEmail.setText(email);
        }

        // Initial State: Show Email Input, Hide Reset Input
        layoutEmail.setVisibility(View.VISIBLE);
        layoutReset.setVisibility(View.GONE);

        btnSendLink.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                requestOtp(email);
            }
        });
        
        btnResetPassword.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();
            
            if (otp.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            
            resetPassword(currentEmail, otp, newPass);
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void requestOtp(String email) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("Sending OTP...");
        progressDialog.show();

        com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(this).create(com.bizzfilling.app.api.ApiService.class);
        java.util.Map<String, String> body = new java.util.HashMap<>();
        body.put("email", email);

        apiService.requestEmailOtp(body).enqueue(new retrofit2.Callback<java.util.Map<String, String>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, String>> call, retrofit2.Response<java.util.Map<String, String>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();
                    currentEmail = email;
                    // Switch to Reset Layout
                    layoutEmail.setVisibility(View.GONE);
                    layoutReset.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, String>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetPassword(String email, String otp, String newPass) {
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("Resetting Password...");
        progressDialog.show();

        com.bizzfilling.app.api.ApiService apiService = com.bizzfilling.app.api.ApiClient.getClient(this).create(com.bizzfilling.app.api.ApiService.class);
        java.util.Map<String, String> body = new java.util.HashMap<>();
        body.put("email", email);
        body.put("code", otp);
        body.put("otp", otp); // Send both to be safe
        body.put("newPassword", newPass);

        apiService.resetPassword(body).enqueue(new retrofit2.Callback<java.util.Map<String, String>>() {
            @Override
            public void onResponse(retrofit2.Call<java.util.Map<String, String>> call, retrofit2.Response<java.util.Map<String, String>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset successfully. Please login.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to reset password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<java.util.Map<String, String>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
