package com.example.btm_menu_test;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SmokeActivity extends AppCompatActivity {

    private EditText editTextAverageSmokingAmount; // 평균 흡연량을 입력하는 EditText
    private EditText editTextCigaretteCount; // 하루 흡연 횟수를 입력하는 EditText
    private EditText editTextCigarettePrice; // 한 개의 담배 가격을 입력하는 EditText
    private EditText editTextSelectedDate; // 선택한 날짜를 표시하는 EditText
    private EditText editTextAverageSmokingTime; // 평균 흡연 시간을 입력하는 EditText
    private Button buttonSelectDate; // 날짜 선택 버튼
    private Button buttonSave; // 저장 버튼

    private DatabaseHelper databaseHelper; // SQLite 데이터베이스  클래스

    private Calendar selectedDate; // 선택한 날짜를 저장하는 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke);

        editTextAverageSmokingAmount = findViewById(R.id.editTextAverageSmokingAmount);
        editTextCigaretteCount = findViewById(R.id.editTextCigaretteCount);
        editTextCigarettePrice = findViewById(R.id.editTextCigarettePrice);
        editTextSelectedDate = findViewById(R.id.editTextSelectedDate);
        editTextAverageSmokingTime = findViewById(R.id.editTextAverageSmokingTime);
        buttonSelectDate = findViewById(R.id.buttonSelectDate);
        buttonSave = findViewById(R.id.buttonSave);

        databaseHelper = new DatabaseHelper(this);

        // 저장된 값이 있는 경우 HomeActivity를 시작하고 SmokeActivity를 종료
        if (hasSavedSmokeInfo()) {
            goToHomeActivity();
            return;
        }

        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSmokeInfo();
            }
        });
    }

    /**
     * 저장된 흡연 정보의 유무를 확인합니다.
     *
     * @return 저장된 흡연 정보가 있는 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private boolean hasSavedSmokeInfo() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int count = databaseHelper.getSmokeDataCount(db);
        db.close();
        return count > 0;
    }

    /**
     * 날짜 선택 다이얼로그를 표시합니다.
     */
    private void showDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(Calendar.YEAR, year);
                            selectedDate.set(Calendar.MONTH, monthOfYear);
                            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            // 선택한 날짜와 현재 날짜를 비교합니다.
                            if (selectedDate.after(currentDate)) {
                                throw new IllegalArgumentException("미래의 날짜를 선택할 수 없습니다.");
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String selectedDateString = dateFormat.format(selectedDate.getTime());
                            editTextSelectedDate.setText(selectedDateString);
                        } catch (IllegalArgumentException e) {
                            // 미래 날짜를 선택한 경우 오류 메시지를 표시합니다.
                            Toast.makeText(SmokeActivity.this, "미래의 날짜를 선택할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * 흡연 정보를 저장합니다.
     */
    private void saveSmokeInfo() {
        int averageSmokingAmount = Integer.parseInt(editTextAverageSmokingAmount.getText().toString());
        int cigaretteCount = Integer.parseInt(editTextCigaretteCount.getText().toString());
        int cigarettePrice = Integer.parseInt(editTextCigarettePrice.getText().toString());
        String smokingStartDate = editTextSelectedDate.getText().toString();
        int averageSmokingTime = Integer.parseInt(editTextAverageSmokingTime.getText().toString());


        // 입력 값의 유효성을 확인합니다.
        if (averageSmokingAmount <= 0 || cigaretteCount <= 0 || cigarettePrice <= 0 || smokingStartDate.isEmpty() || averageSmokingTime <= 0) {
            Toast.makeText(this, "입력 값을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        int totalSmokingTime = averageSmokingAmount * averageSmokingTime;
        if (totalSmokingTime > 1440) {
            Toast.makeText(this, "설정한 하루 평균 흡연량과 평균 흡연 시간이 24 시간을 초과합니다. 다시 설정해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        databaseHelper.insertSmokingData(db, averageSmokingAmount, cigaretteCount, cigarettePrice, smokingStartDate, averageSmokingTime);
        db.close();

        Toast.makeText(this, "흡연 정보가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
        goToHomeActivity();
    }

    /**
     * HomeActivity를 시작합니다.
     */
    private void goToHomeActivity() {
        Intent intent = new Intent(SmokeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}