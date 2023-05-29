package com.example.smoking;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    private EditText editTextAverageSmokingAmount;
    private EditText editTextCigaretteCount;
    private EditText editTextCigarettePrice;
    private EditText editTextSelectedDate;
    private EditText editTextAverageSmokingTime;
    private Button buttonSelectDate;
    private Button buttonSave;

    private SharedPreferences sharedPreferences;

    private Calendar selectedDate;

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // 흡연 정보가 이미 저장되었는지 확인합니다.
        boolean isSmokeInfoSaved = sharedPreferences.getBoolean("isSmokeInfoSaved", false);
        if (isSmokeInfoSaved) {
            // 흡연 정보가 이미 저장되었으면 HomeActivity로 이동합니다.
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

    private void showDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String selectedDateString = dateFormat.format(selectedDate.getTime());
                        editTextSelectedDate.setText(selectedDateString);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveSmokeInfo() {
        // 입력된 값을 가져옵니다.
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

        // 흡연 정보를 SharedPreferences에 저장합니다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("averageSmokingAmount", averageSmokingAmount);
        editor.putInt("cigaretteCount", cigaretteCount);
        editor.putInt("cigarettePrice", cigarettePrice);
        editor.putString("smokingStartDate", smokingStartDate);
        editor.putInt("averageSmokingTime", averageSmokingTime);
        editor.putBoolean("isSmokeInfoSaved", true);
        editor.apply();

        Toast.makeText(this, "흡연 정보가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
        goToHomeActivity();
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(SmokeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
