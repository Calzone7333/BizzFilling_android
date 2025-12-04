package com.bizzfilling.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TalkToExpertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_to_expert);

        Button btnCallCA = findViewById(R.id.btnCallCA);
        Button btnCallIP = findViewById(R.id.btnCallIP);

        btnCallCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TalkToExpertActivity.this, "Connecting to CA...", Toast.LENGTH_SHORT).show();
            }
        });

        btnCallIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TalkToExpertActivity.this, "Connecting to IP Expert...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
