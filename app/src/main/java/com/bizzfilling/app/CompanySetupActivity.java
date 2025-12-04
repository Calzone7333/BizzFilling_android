package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CompanySetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_setup);

        EditText etCompanyName = findViewById(R.id.etCompanyName);
        Button btnSetup = findViewById(R.id.btnSetup);

        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etCompanyName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(CompanySetupActivity.this, "Please enter company name", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CompanySetupActivity.this, "Setup Complete!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CompanySetupActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
