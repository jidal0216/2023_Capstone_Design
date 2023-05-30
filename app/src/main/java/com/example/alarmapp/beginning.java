package com.example.alarmapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class beginning extends AppCompatActivity {
    private EditText averageCigarettesEditText;
    private EditText cigarettesPerPackEditText;
    private EditText startYearEditText;
    private EditText averageSmokingTimeEditText;
    private EditText averageSmokingPriceEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begging);

        averageCigarettesEditText = findViewById(R.id.edittext_average_cigarettes);
        cigarettesPerPackEditText = findViewById(R.id.edittext_cigarettes_per_pack);
        startYearEditText = findViewById(R.id.edittext_start_year);
        averageSmokingTimeEditText = findViewById(R.id.edittext_average_smoking_time);
        averageSmokingPriceEditText = findViewById(R.id.edittext_average_smoking_price);

        Intent intent = getIntent();
        if (intent != null) {
            int averageCigarettes = intent.getIntExtra("averageCigarettes", 0);
            int cigarettesPerPack = intent.getIntExtra("cigarettesPerPack", 0);
            int startYear = intent.getIntExtra("startYear", 0);
            int averageSmokingTime = intent.getIntExtra("averageSmokingTime", 0);
            int averageSmokingPrice = intent.getIntExtra("averageSmokingPrice", 0);

            // Set the values in the EditText fields
            averageCigarettesEditText.setText(String.valueOf(averageCigarettes));
            cigarettesPerPackEditText.setText(String.valueOf(cigarettesPerPack));
            startYearEditText.setText(String.valueOf(startYear));
            averageSmokingTimeEditText.setText(String.valueOf(averageSmokingTime));
            averageSmokingPriceEditText.setText(String.valueOf(averageSmokingPrice));
        }
    }
}