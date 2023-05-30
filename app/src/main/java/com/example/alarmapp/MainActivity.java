package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begging);

        nextButton = findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = System.currentTimeMillis();
                Intent intent = new Intent(MainActivity.this, MyAlarmActivity.class);
                intent.putExtra("startTime", startTime);
                startActivity(intent);
            }
        });


    }
}