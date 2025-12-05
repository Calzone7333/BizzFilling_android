package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bizzfilling.app.api.ApiClient;
import com.bizzfilling.app.api.ApiService;
import com.bizzfilling.app.api.models.SignupRequest;
import com.bizzfilling.app.api.models.SignupResponse;
import android.widget.EditText;
// import com.google.android.material.textfield.TextInputEditText; // Removed

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {

    private EditText etFullName, etEmail, etPassword;
    private Button btnSignup;
    private TextView tvLoginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLoginLink = findViewById(R.id.tvLoginLink);

        // --- ANIMATIONS ---
        try {
            android.view.animation.Animation slideUp = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.item_animation_slide_up);
            android.view.animation.Animation fadeIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in);
            
            View ivIllustration = findViewById(R.id.ivIllustration);
            View tvTitle = findViewById(R.id.tvTitle);
            View tvSubtitle = findViewById(R.id.tvSubtitle);
            View layoutName = findViewById(R.id.layoutName);
            View layoutEmail = findViewById(R.id.layoutEmail);
            View layoutPassword = findViewById(R.id.layoutPassword);
            View layoutDivider = findViewById(R.id.layoutDivider);
            View layoutSocial = findViewById(R.id.layoutSocial);

            if (ivIllustration != null) ivIllustration.startAnimation(fadeIn);
            if (tvTitle != null) tvTitle.startAnimation(slideUp);
            if (tvSubtitle != null) tvSubtitle.startAnimation(slideUp);
            if (layoutName != null) layoutName.startAnimation(slideUp);
            if (layoutEmail != null) layoutEmail.startAnimation(slideUp);
            if (layoutPassword != null) layoutPassword.startAnimation(slideUp);
            if (btnSignup != null) btnSignup.startAnimation(slideUp);
            if (layoutDivider != null) layoutDivider.startAnimation(fadeIn);
            if (layoutSocial != null) layoutSocial.startAnimation(fadeIn);
            
            // Back Button Listener
            View btnBack = findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }

        } catch (Exception e) {
            e.printStackTrace(); // Prevent crash if animation fails
        }

        if (btnSignup != null) {
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (etFullName == null || etEmail == null || etPassword == null) {
                            Toast.makeText(SignupActivity.this, "Error: Views not initialized properly", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
                        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
                        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

                        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!isNetworkAvailable()) {
                                Intent intent = new Intent(SignupActivity.this, NoInternetActivity.class);
                                startActivity(intent);
                                return;
                            }

                            // API Call
                            ApiService apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
                            // Default role USER
                            SignupRequest request = new SignupRequest(fullName, email, password, "USER");

                            // Show loading
                            btnSignup.setEnabled(false);
                            btnSignup.setText("Creating Account...");

                            apiService.signup(request).enqueue(new Callback<SignupResponse>() {
                                @Override
                                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                                    if (isFinishing()) return;
                                    btnSignup.setEnabled(true);
                                    btnSignup.setText("Create Account");

                                    try {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Signup Successful! Please Login.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Signup Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(SignupActivity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SignupResponse> call, Throwable t) {
                                    if (isFinishing()) return;
                                    btnSignup.setEnabled(true);
                                    btnSignup.setText("Create Account");

                                    String errorMessage = "Network Error: " + t.getMessage();
                                    Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignupActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (tvLoginLink != null) {
            tvLoginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
