// 2번
package com.example.capstonb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class ResultActivity extends AppCompatActivity {
    private TextView savingsTextView;
    private TextView lifespanTextView;
    private TextView timeSavedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        savingsTextView = findViewById(R.id.textview_savings);
        lifespanTextView = findViewById(R.id.textview_lifespan);
        timeSavedTextView = findViewById(R.id.textview_time_saved);




        Intent intent = new Intent(Home_Activity.this, ResultActivity.class);
        intent.putExtra("averageSmokingAmount", averageSmokingAmount);
        intent.putExtra("cigaretteCount", cigaretteCount);
        intent.putExtra("averageSmokingTime", averageSmokingTime);
        intent.putExtra("cigarettePrice", cigarettePrice);
        startActivity(intent);

        Intent intent = getIntent();
        int averageSmokingAmount = intent.getIntExtra("averageSmokingAmount", 0);
        int cigaretteCount = intent.getIntExtra("cigaretteCount", 0);
        int averageSmokingTime = intent.getIntExtra("averageSmokingTime", 0);
        int cigarettePrice = intent.getIntExtra("cigarettePrice", 0);


        StringBuilder savingsBuilder = new StringBuilder();
        StringBuilder lifespanBuilder = new StringBuilder();
        StringBuilder timeSavedBuilder = new StringBuilder();

        // 수명 계산하는 식 다시 설정해야함
        int[] years = {5,10,20,30,40};

        savingsBuilder.append("절약한 돈\n");
        lifespanBuilder.append("늘어난 수명\n");
        timeSavedBuilder.append("절약한 시간\n");

        for (int yearsPassed : years) {
            int packsPerYear = (averageSmokingAmount * 365) / cigaretteCount;
            int totalSavings = packsPerYear * cigarettePrice * yearsPassed;

            int lifespanIncrease = ((averageSmokingAmount * yearsPassed * 11) / 24);
            int lifespanDays = lifespanIncrease / (24 * 60);
            int lifespanHours = (lifespanIncrease % (24 * 60)) / 60;
            int lifespanMinutes = (lifespanIncrease % (24 * 60)) % 60;

            int minutesPerYear = averageSmokingAmount * averageSmokingTime * 365;
            int timeSaved = minutesPerYear * yearsPassed;
            int timeSavedDays = timeSaved / (24 * 60);
            int timeSavedHours = (timeSaved % (24 * 60)) / 60;
            int timeSavedMinutes = (timeSaved % (24 * 60)) % 60;

            String formattedSavings = String.format("%,d", totalSavings);

            savingsBuilder.append(yearsPassed).append("년 후- ").append(formattedSavings).append("원\n");
            lifespanBuilder.append(yearsPassed).append("년 후- ")
                    .append(lifespanDays).append("일 ").append(lifespanHours).append("시간 ").append(lifespanMinutes).append("분\n");
            timeSavedBuilder.append(yearsPassed).append("년 후- ")
                    .append(timeSavedDays).append("일 ").append(timeSavedHours).append("시간 ").append(timeSavedMinutes).append("분\n");
        }
        savingsTextView.setText(savingsBuilder.toString());
        lifespanTextView.setText(lifespanBuilder.toString());
        timeSavedTextView.setText(timeSavedBuilder.toString());
    }

}