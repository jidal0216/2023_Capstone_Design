package com.example.btm_menu_test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MicTestActivity extends AppCompatActivity {

    private long startTime;
    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;
    final int PERMISSION = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mictest);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        if ( Build.VERSION.SDK_INT >= 30 ){
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }

        textView = findViewById(R.id.sttResult);
        sttBtn = findViewById(R.id.sttStart);

        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        sttBtn.setOnClickListener(v -> {
            mRecognizer=SpeechRecognizer.createSpeechRecognizer(MicTestActivity.this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });
    }

    private final RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "폐활량 측정을 시작합니다. \n있는 힘껏 버티세요!", Toast.LENGTH_SHORT).show();
            startTime = System.currentTimeMillis();
        }
        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float rmsdB) {}

        @Override
        public void onBufferReceived(byte[] buffer) {}

        @Override
        public void onEndOfSpeech() {
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime) / 1000; // 밀리초를 초로 변환

            if (duration < 10) {
                textView.setText("폐기능이 정상에 못미칩니다.");
            } else if (duration < 20) {
                textView.setText("폐기능이 정상입니다.");
            } else if (duration < 30) {
                textView.setText("운동선수의 폐입니다.");
            } else {
                textView.setText("폐가 아니고 로봇이네요.");
            }

            Toast.makeText(getApplicationContext(), "총 측정 시간: " + duration + "초", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(int error) {

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                case SpeechRecognizer.ERROR_CLIENT:
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                case SpeechRecognizer.ERROR_NO_MATCH:
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                case SpeechRecognizer.ERROR_SERVER:
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                default:
                    break;
            }
        }

        @Override
        public void onResults(Bundle results) {}

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };
}

