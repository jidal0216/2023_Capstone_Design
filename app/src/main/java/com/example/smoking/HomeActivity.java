package com.example.smoking;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private TextView textViewSmokingCessationPeriod;
    private TextView textViewSavingMoney;


    private DatabaseHelper databaseHelper;
    private Handler handler;
    private Runnable timerRunnable;
    private long startTime;

    private static final String TIMER_PREFS = "timer_prefs";
    private static final String START_TIME_KEY = "start_time_key";

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
        textViewSmokingCessationPeriod = findViewById(R.id.textViewsmokingcessationperiod);
        textViewSavingMoney = findViewById(R.id.textViewSavingMoney);

        databaseHelper = new DatabaseHelper(this);
        handler = new Handler();

        // SharedPreferences에서 시작 시간을 복원합니다.
        SharedPreferences prefs = getSharedPreferences(TIMER_PREFS, MODE_PRIVATE);
        startTime = prefs.getLong(START_TIME_KEY, System.currentTimeMillis());

        calculateStats();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 앱이 종료될 때 시작 시간을 SharedPreferences에 저장합니다.
        SharedPreferences prefs = getSharedPreferences(TIMER_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(START_TIME_KEY, startTime);
        editor.apply();
    }

    private void calculateStats() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // 데이터베이스에서 흡연 정보 가져오기
        Cursor cursor = db.rawQuery("SELECT * FROM smoking ORDER BY id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int averageSmokingAmount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AVERAGE_SMOKING_AMOUNT));
            @SuppressLint("Range") int cigaretteCount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CIGARETTE_COUNT));
            @SuppressLint("Range") int cigarettePrice = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CIGARETTE_PRICE));
            @SuppressLint("Range") String smokingStartDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SMOKING_START_DATE));
            @SuppressLint("Range") int averageSmokingTime = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_AVERAGE_SMOKING_TIME));

            // 통계 계산하기
            int daysSmokeFree = calculateDaysSmokeFree(smokingStartDate);
            double savings = calculateSavings(cigaretteCount, cigarettePrice, averageSmokingAmount, daysSmokeFree);
            double lifespanIncrease = calculateLifespanIncrease(averageSmokingAmount);
            double timeGained = calculateTimeGained(averageSmokingAmount, averageSmokingTime);
            int totalCigarettesSmoked = calculateTotalCigarettesSmoked(daysSmokeFree, averageSmokingAmount);
            double lifespanReduction = calculateLifespanReduction(averageSmokingAmount, daysSmokeFree);
            long timeLoss = calculateTimeLoss(totalCigarettesSmoked,averageSmokingTime);
            long timeDifference = calculateTimeDifference();
            double savingMoney = calculateSavingMoney(cigarettePrice, cigaretteCount, averageSmokingAmount);



            // 값 포맷팅하기

            NumberFormat numberFormat = NumberFormat.getInstance();
            String formattedTimeDifference = formatTimeDifference(timeDifference);

            // TextView에 계산된 값 설정하기
            textViewSavings.setText(formatSavings(savings)) ;  //돈 쓴 금액
            textViewLifespanIncrease.setText(formatLifespanIncrease(lifespanIncrease) );  // 예상 수명 증가
            textViewTimeGained.setText(formatTimeGained(timeGained)); //흡연 중단으로 얻은 시간
            textViewDaysSmokeFree.setText(numberFormat.format(daysSmokeFree) + " 일"); //총 흡연 기간
            textViewTotalCigarettesSmoked.setText(numberFormat.format(totalCigarettesSmoked) + " 개비"); //총 흡연량
            textViewLifespanReduction.setText(formatLifespanReduction((long) lifespanReduction));  //흡연으로 인한 수명 감소
            textViewTimeLoss.setText(formatTimeLoss(timeLoss));  // 흡연으로 인한 시간 손실
            textViewSmokingCessationPeriod.setText(formattedTimeDifference); // 금연 기간
            textViewSavingMoney.setText(formatSavings(savingMoney)); // 절약 금액
        }

        cursor.close();
        db.close();
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long timeDifference = currentTime - startTime;
                String formattedTimeDifference = formatTimeDifference(timeDifference);
                textViewSmokingCessationPeriod.setText(formattedTimeDifference);
                calculateStats();


                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(timerRunnable, 1000);
    }
    @Override
    protected void onResume() {
        super.onResume();


        handler.postDelayed(timerRunnable, 1000); //  ( 데이터값 실시간 출력 )
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timerRunnable); //  ( 데이터값 실시간 출력 )
    }

    // 총 흡연 기간
    private int calculateDaysSmokeFree(String smokingStartDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = dateFormat.parse(smokingStartDate);
            Date currentDate = new Date();
            long timeDifference = currentDate.getTime() - startDate.getTime();
            int daysSmokeFree = (int) TimeUnit.MILLISECONDS.toDays(timeDifference);
            return daysSmokeFree > 0 ? daysSmokeFree : 1; // 최소 1일로 설정
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 돈 쓴 금액
    private double calculateSavings(int cigaretteCount, int cigarettePrice, int averageSmokingAmount,int daysSmokeFree) {
        double packPrice = (double) cigarettePrice / cigaretteCount;
        double dailySavings = packPrice * averageSmokingAmount ;
        double UsePrice = dailySavings * daysSmokeFree ;
        return UsePrice;
    }
    //돈 쓴 금액 단위 변경 + 값 포매팅
    private String formatSavings(double savings) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(savings) + " 원";
    }
    // 예상 수명 증가
    private double calculateLifespanIncrease(int averageSmokingAmount) {
        long OneDaySmoke = averageSmokingAmount ;
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - startTime;
        double timeInSeconds = (double) timeDifference / 1000  ;
        double lifespanIncrease = timeInSeconds * OneDaySmoke * 1 / 1440;  // in seconds

        // 수명 증가가 수명 감소보다 크면 수명 감소 값으로 변경
        double lifespanReduction = calculateLifespanReduction(averageSmokingAmount, (int) timeInSeconds);
        if (lifespanIncrease > lifespanReduction) {
            lifespanIncrease = lifespanReduction;
            System.out.println("흡연으로 인한 수명 손실이 거의 다 회복되었습니다.고생하셨습니다.");
        }

        return lifespanIncrease;
    }


    // 수명 증가 포맷팅
    private String formatLifespanIncrease(double lifespanIncrease) {
        long totalSeconds = (long) lifespanIncrease;
        long days = TimeUnit.SECONDS.toDays(totalSeconds);
        long hours = TimeUnit.SECONDS.toHours(totalSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%d일 %d시간 %d분 %d초", days, hours, minutes, seconds);
    }

    // 금연으로 얻은 시간
    private double calculateTimeGained(int averageSmokingAmount, int averageSmokingTime) {
        long smokeDayTime = averageSmokingAmount * averageSmokingTime ;
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - startTime;
        double timeInSeconds = (double) timeDifference /1000 ;
        return smokeDayTime * timeInSeconds / 1440 ;  // in seconds
    }

    // 금연으로 얻은 시간 포맷팅
    private String formatTimeGained(double timeGained) {
        long totalSeconds = (long) timeGained;
        long days = TimeUnit.SECONDS.toDays(totalSeconds);
        long hours = TimeUnit.SECONDS.toHours(totalSeconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
        long seconds = totalSeconds % 60;

        return String.format(Locale.getDefault(), "%d일 %d시간 %d분 %d초", days, hours, minutes, seconds);
    }

// 총 담배 개수
    private int calculateTotalCigarettesSmoked(int daysSmokeFree, int averageSmokingAmount) {
        return daysSmokeFree * averageSmokingAmount;
    }
    // 흡연으로 인한 수명 감소
    private double calculateLifespanReduction(int averageSmokingAmount, int daysSmokeFree) {
        return averageSmokingAmount * 11 * daysSmokeFree;
    }

    // 수명 감소 포맷팅
    private String formatLifespanReduction(double lifespanReduction) {
        long totalMinutes = (long) lifespanReduction;
        long days = TimeUnit.MINUTES.toDays(totalMinutes);
        long hours = TimeUnit.MINUTES.toHours(totalMinutes) % 24;
        long minutes = totalMinutes % 60;

        return String.format(Locale.getDefault(), "%d일 %d시간 %d분", days, hours, minutes);
    }

    // 시간 손실
    private long calculateTimeLoss(int totalCigarettesSmoked ,int averageSmokingTime) {
        return  averageSmokingTime * totalCigarettesSmoked ;
}

    // 흡연으로 인한 시간 손실 포매팅
    private String formatTimeLoss(long timeLoss) {
        long totalMinutes = (long) timeLoss;
        long days = TimeUnit.MINUTES.toDays(timeLoss);
        long hours = TimeUnit.MINUTES.toHours(timeLoss) % 24;
        long minutes = totalMinutes % 60;
        return String.format(Locale.getDefault(), "%d일 %d시간 %d분", days, hours, minutes);
    }

//금연 기간
    private long calculateTimeDifference() {
        long currentTime = System.currentTimeMillis();
        return currentTime - startTime;
    }
//금연 기간 포매팅
    private String formatTimeDifference(long timeDifference) {
        long days = TimeUnit.MILLISECONDS.toDays(timeDifference);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference) % 60;

        return String.format(Locale.getDefault(), "%d일 %d시간 %d분 %d초", days, hours, minutes, seconds);
    }
//절약금액
    private double calculateSavingMoney(int cigarettePrice, int cigaretteCount, int averageSmokingAmount) {
        double packPrice = (double) cigarettePrice / cigaretteCount;
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - startTime;
        double timeInSeconds = (double) timeDifference / 1000 ;

        // 시간이 흐를수록 절약 금액이 증가하도록 계산
        double savingMoney = packPrice * averageSmokingAmount / 86400 * timeInSeconds;

        return savingMoney;
    }

    // 흡연 정보 수정 화면으로 이동
    public void onButtonEditProfileClicked(View view) {
        databaseHelper.clearSavedData();

        Intent intent = new Intent(this, SmokeActivity.class);
        startActivity(intent);

    }

    public void onButtonQuitSmokingClicked(View view) {
        // 금연하기 버튼 동작 처리
    }
//데이터 초기화 메서드
    private void clearSavedData() {


        // 시작 시간 재설정
        startTime = System.currentTimeMillis();

        // 예상 수명 증가 , 금연으로 얻은 시간, 금연 기간 초기화 후 텍스트 뷰 업데이트
        String formattedTimeDifference = formatTimeDifference(0);
        textViewSmokingCessationPeriod.setText(formattedTimeDifference);
        textViewLifespanIncrease.setText(formatLifespanIncrease(0.0));
        textViewTimeGained.setText(formatTimeGained(0.0));
    }
    // "금연 실패" 버튼을 클릭했을 때 호출되는 메서드
    public void onClearDataButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("금연 초기화")
                .setMessage("금연 데이터를 초기화하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // "예"를 선택한 경우
                        Toast.makeText(getApplicationContext(), "금연 초기화 됩니다.", Toast.LENGTH_SHORT).show();
                        // 금연 데이터 초기화
                        clearSavedData();
                        Toast.makeText(getApplicationContext(), "이번에는 꼭 금연 하시길 바랍니다.", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("아니요", null)
                .show();



    }
}
