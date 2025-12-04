package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerify;
    private TextView tvResend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        etOtp = findViewById(R.id.etOtp);
        btnVerify = findViewById(R.id.btnVerify);
        tvResend = findViewById(R.id.tvResend);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etOtp.getText().toString();
                if (otp.length() < 6) {
                    Toast.makeText(VerifyOtpActivity.this, "Please enter valid 6-digit OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Verify API call
                    Toast.makeText(VerifyOtpActivity.this, "Verified Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VerifyOtpActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerifyOtpActivity.this, "OTP Resent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
