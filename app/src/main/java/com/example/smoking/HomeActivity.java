package com.example.smoking;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewSavings;
    private TextView textViewLifespanIncrease;
    private TextView textViewTimeGained;
    private TextView textViewDaysSmokeFree;
    private TextView textViewTotalCigarettesSmoked;
    private TextView textViewLifespanReduction;
    private TextView textViewTimeLoss;
    private TextView textViewSavingsPerDay; // 추가된 부분

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textViewSavings = findViewById(R.id.textViewSavings);
        textViewLifespanIncrease = findViewById(R.id.textViewLifespanIncrease);
        textViewTimeGained = findViewById(R.id.textViewTimeGained);
        textViewDaysSmokeFree = findViewById(R.id.textViewDaysSmokeFree);
        textViewTotalCigarettesSmoked = findViewById(R.id.textViewTotalCigarettesSmoked);
        textViewLifespanReduction = findViewById(R.id.textViewLifespanReduction);
        textViewTimeLoss = findViewById(R.id.textViewTimeLoss);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        calculateStats();
    }

    private void calculateStats() {
        // SharedPreferences에서 흡연 정보 가져오기
        int averageSmokingAmount = sharedPreferences.getInt("averageSmokingAmount", 0);
        int cigaretteCount = sharedPreferences.getInt("cigaretteCount", 0);
        int cigarettePrice = sharedPreferences.getInt("cigarettePrice", 0);
        String smokingStartDate = sharedPreferences.getString("smokingStartDate", "");
        int averageSmokingTime = sharedPreferences.getInt("averageSmokingTime", 0);

        // 통계 계산하기
        int daysSmokeFree = calculateDaysSmokeFree(smokingStartDate);
        double savings = calculateSavings(cigaretteCount, cigarettePrice, averageSmokingAmount);
        double lifespanIncrease = calculateLifespanIncrease(averageSmokingAmount);
        double timeGained = calculateTimeGained(averageSmokingAmount, averageSmokingTime);
        int totalCigarettesSmoked = calculateTotalCigarettesSmoked(smokingStartDate, averageSmokingAmount);
        double lifespanReduction = calculateLifespanReduction(averageSmokingAmount);
        long timeLoss = calculateTimeLoss(smokingStartDate);


        // 값 포맷팅하기
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        NumberFormat numberFormat = NumberFormat.getInstance();

        // TextView에 계산된 값 설정하기
        textViewSavings.setText(formatSavings(savings));
        textViewLifespanIncrease.setText(decimalFormat.format(lifespanIncrease) + " 년");
        textViewTimeGained.setText(decimalFormat.format(timeGained) + " 분");
        textViewDaysSmokeFree.setText(numberFormat.format(daysSmokeFree) + " 일");
        textViewTotalCigarettesSmoked.setText(numberFormat.format(totalCigarettesSmoked) + " 개비");
        textViewLifespanReduction.setText(decimalFormat.format(lifespanReduction) + " 분");
        textViewTimeLoss.setText(formatTimeLoss(timeLoss));

    }

    private int calculateDaysSmokeFree(String smokingStartDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(smokingStartDate);
            Date currentDate = new Date();
            long diffInMilliseconds = currentDate.getTime() - startDate.getTime();
            return (int) TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double calculateSavings(int cigaretteCount, int cigarettePrice, int averageSmokingAmount) {
        double packPrice = (double) cigarettePrice / cigaretteCount;
        double dailySavings = packPrice * averageSmokingAmount;
        int daysSmokeFree = calculateDaysSmokeFree(sharedPreferences.getString("smokingStartDate", ""));
        return dailySavings * daysSmokeFree;
    }

    private String formatSavings(double savings) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(savings) + " 원";
    }

    private double calculateLifespanIncrease(int averageSmokingAmount) {
        return averageSmokingAmount * 0.001; // 하루 평균 한 개비 당 0.1년 수명 증가로 가정
    }

    private double calculateTimeGained(int averageSmokingAmount, int averageSmokingTime) {
        return averageSmokingAmount * averageSmokingTime;
    }

    private int calculateTotalCigarettesSmoked(String smokingStartDate, int averageSmokingAmount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(smokingStartDate);
            Date currentDate = new Date();
            long diffInMilliseconds = currentDate.getTime() - startDate.getTime();
            int daysSmokeFree = (int) TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
            return daysSmokeFree * averageSmokingAmount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double calculateLifespanReduction(int averageSmokingAmount) {
        return averageSmokingAmount * 5.0 / 60.0; // 하루 평균 한 개비 당 5분 수명 감소로 가정
    }

    private long calculateTimeLoss(String smokingStartDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(smokingStartDate);
            Date saveDate = new Date(sharedPreferences.getLong("saveDate", 0));
            long diffInMilliseconds = startDate.getTime() - saveDate.getTime();
            long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMilliseconds);
            long hours = diffInMinutes / 60; // 시간 단위 계산
            long minutes = diffInMinutes % 60; // 분 단위 계산
            return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    private String formatTimeLoss(long timeLoss) {
        long hours = TimeUnit.MILLISECONDS.toHours(timeLoss);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLoss) - TimeUnit.HOURS.toMinutes(hours);
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    private void clearSavedData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // SharedPreferences의 모든 데이터를 삭제합니다.
        editor.apply(); // 변경 사항을 저장합니다.
        calculateStats(); // 변경된 데이터에 따라 통계를 다시 계산합니다.
    }

    // "데이터 초기화" 버튼 클릭 시 호출되는 메소드
    public void onClearDataButtonClick(View view) {
        clearSavedData();
    }
}
