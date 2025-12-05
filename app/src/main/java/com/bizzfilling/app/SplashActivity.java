package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.bizzfilling.app.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500; // 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Handle Android 12+ Splash Screen
        androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_splash);

            // Animate Logo (Keep for older versions, or if you want a double splash effect)
            // For Android 12+, the system splash handles the initial launch.
            View logoContainer = findViewById(R.id.logoContainer);
            if (logoContainer != null) {
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                logoContainer.startAnimation(fadeIn);
            }

            new Handler().postDelayed(() -> {
                try {
                    com.bizzfilling.app.utils.SessionManager sessionManager = new com.bizzfilling.app.utils.SessionManager(getApplicationContext());
                    Intent intent;

                    if (sessionManager.getToken() != null) {
                        // User is logged in, go to Dashboard
                        intent = new Intent(SplashActivity.this, DashboardActivity.class);
                        intent.putExtra("ROLE", sessionManager.getRole());
                    } else {
                        // User is not logged in, go to Login
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fallback to Login if anything fails
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, SPLASH_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
            // If layout fails, just go to Login immediately
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
}
