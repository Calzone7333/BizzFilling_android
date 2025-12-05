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
import com.bizzfilling.app.api.models.LoginRequest;
import com.bizzfilling.app.api.models.LoginResponse;
import android.widget.EditText;
// import com.google.android.material.textfield.TextInputEditText; // Removed

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    // private LinearLayout emailLoginForm; // Not strictly needed unless toggling visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        // emailLoginForm = findViewById(R.id.emailLoginForm);

        // --- ANIMATIONS ---
        // --- ANIMATIONS ---
        try {
            android.view.animation.Animation slideUp = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.item_animation_slide_up);
            android.view.animation.Animation fadeIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in);
            
            View ivIllustration = findViewById(R.id.ivIllustration);
            View tvWelcome = findViewById(R.id.tvWelcome);
            View tvSubtitle = findViewById(R.id.tvSubtitle);
            View layoutEmail = findViewById(R.id.layoutEmail);
            View layoutPassword = findViewById(R.id.layoutPassword);
            View btnLogin = findViewById(R.id.btnLogin);

            if (ivIllustration != null) ivIllustration.startAnimation(fadeIn);
            if (tvWelcome != null) tvWelcome.startAnimation(slideUp);
            if (tvSubtitle != null) tvSubtitle.startAnimation(slideUp);
            if (layoutEmail != null) layoutEmail.startAnimation(slideUp);
            if (layoutPassword != null) layoutPassword.startAnimation(slideUp);
            if (btnLogin != null) btnLogin.startAnimation(slideUp);
            
            // Back Button Listener
            View btnBack = findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }

        } catch (Exception e) {
            e.printStackTrace(); // Prevent crash if animation fails
        }
        // ------------------

        // --- GLOBAL CRASH HANDLER ---
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                Toast.makeText(getApplicationContext(), "CRASH: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            });
            
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            } else {
                System.exit(1);
            }
        });
        // -----------------------------

        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (etEmail == null || etPassword == null) {
                            Toast.makeText(LoginActivity.this, "Error: Views not initialized properly", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
                        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!isNetworkAvailable()) {
                            Intent intent = new Intent(LoginActivity.this, NoInternetActivity.class);
                            startActivity(intent);
                            return;
                        }

                        // API Call
                        ApiService apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
                        LoginRequest request = new LoginRequest(email, password);

                        // Show loading indicator
                        btnLogin.setEnabled(false);
                        btnLogin.setText("Signing in...");

                        apiService.login(request).enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                if (isFinishing()) return;
                                btnLogin.setEnabled(true);
                                btnLogin.setText("Sign In");
                                
                                try {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getUser() != null) {
                                            String role = response.body().getUser().getRole();
                                            String token = response.body().getToken();
                                            String userId = response.body().getUser().getId();
                                            String name = response.body().getUser().getFullName();
                                            String email = response.body().getUser().getEmail();
                                            
                                            // Defensive checks
                                            if (token == null) token = "";
                                            if (role == null) role = "USER";
                                            if (userId == null) userId = "";
                                            if (name == null) name = "User";
                                            if (email == null) email = "";

                                            // Save session
                                            com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(getApplicationContext());
                                            sessionManager.saveSession(token, role, userId, name, email);

                                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                            intent.putExtra("ROLE", role.toUpperCase());
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Login Failed: User data is missing", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        String errorMsg = "Login Failed: " + response.message();
                                        if (response.code() == 401) {
                                            errorMsg = "Invalid Email or Password";
                                        }
                                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "Error processing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t) {
                                if (isFinishing()) return;
                                btnLogin.setEnabled(true);
                                btnLogin.setText("Sign In");
                                
                                String errorMessage = "Network Error: " + t.getMessage();
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "CRASH: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



        TextView tvSignupLink = findViewById(R.id.tvSignupLink);
        if (tvSignupLink != null) {
            tvSignupLink.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            });
        }
        
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
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
