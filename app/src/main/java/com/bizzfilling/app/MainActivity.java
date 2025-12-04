package com.bizzfilling.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Button;

public class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.btnLoginMain);
        if (loginBtn != null) {
            loginBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }
}
