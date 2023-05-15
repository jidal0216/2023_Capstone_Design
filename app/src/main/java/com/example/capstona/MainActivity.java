package com.example.capstona;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
    private EditText averageCigarettesEditText;
    private EditText cigarettesPerPackEditText;
    private EditText startYearEditText;
    private EditText averageSmokingTimeEditText;
    private EditText averageSmokingPriceEditText;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        averageCigarettesEditText = findViewById(R.id.edittext_average_cigarettes);
        cigarettesPerPackEditText = findViewById(R.id.edittext_cigarettes_per_pack);
        startYearEditText = findViewById(R.id.edittext_start_year);
        averageSmokingTimeEditText = findViewById(R.id.edittext_average_smoking_time);
        averageSmokingPriceEditText = findViewById(R.id.edittext_average_smoking_price);
        nextButton = findViewById(R.id.button_next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered values
                int averageCigarettes = Integer.parseInt(averageCigarettesEditText.getText().toString());
                int cigarettesPerPack = Integer.parseInt(cigarettesPerPackEditText.getText().toString());
                int startYear = Integer.parseInt(startYearEditText.getText().toString());
                int averageSmokingTime = Integer.parseInt(averageSmokingTimeEditText.getText().toString());
                int averageSmokingPrice = Integer.parseInt(averageSmokingPriceEditText.getText().toString());

                // Pass the values to the next activity
                Intent intent = new Intent(String.valueOf(MainActivity.this));
                intent.putExtra("averageCigarettes", averageCigarettes);
                intent.putExtra("cigarettesPerPack", cigarettesPerPack);
                intent.putExtra("startYear", startYear);
                intent.putExtra("averageSmokingTime", averageSmokingTime);
                intent.putExtra("averageSmokingPrice", averageSmokingPrice);
                startActivity(intent);
            }
        });
    }
}