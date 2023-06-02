package com.seonghyun.test;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ScrollView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private long[] trophyTimes = {5000,        // 1초
            8000,    // 1일
            10000,   // 3일
            604800000,   // 7일
    };
    private ImageView trophy1, trophy2, trophy3, trophy4;

    private SharedPreferences sp;

    private long ChallengeTime;

    private Button btn_reset;


    int num = 0; // 도전과제 숫자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        trophy1 = findViewById(R.id.imageView1);
        trophy2 = findViewById(R.id.imageView2);
        trophy3 = findViewById(R.id.imageView3);
        trophy4 = findViewById(R.id.imageView4);


        sp = getSharedPreferences("ChallengeTime", MODE_PRIVATE);


        btn_reset = findViewById(R.id.btn_reset);
        // 데이터 초기화 + 트로피 dark로 전환
        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ChallengeTime = System.currentTimeMillis();
                sp.edit().putLong("ChallengeGetTime",ChallengeTime).apply();
                trophy2.setImageResource(R.drawable.icon_dark);
                trophy3.setImageResource(R.drawable.icon_dark);
                trophy4.setImageResource(R.drawable.icon_dark);
                num = 0;
            }
        });

        // 그림에 설명 추가
        trophy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "금연에 꼭 성공 하시길..!", Toast.LENGTH_LONG).show();
            }
        });

        trophy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "금연 1일차 성공 트로피입니다.", Toast.LENGTH_LONG).show();
            }
        });

        trophy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "금연 3일차 성공 트로피입니다.", Toast.LENGTH_LONG).show();
            }
        });

        trophy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "금연 7일차 성공 트로피입니다.", Toast.LENGTH_LONG).show();
            }
        });


        //"금연일수" + "금연시작" 도전과제 시작
        startSmokingChallenge();
    }

    private void startSmokingChallenge() {
        //앱 실행시 "금연시작" 트로피가 생성됩니다.
        trophy1.setImageResource(R.drawable.icon_bright);

        //"금연일수" 트로피를 시간마다 설정한 부분입니다.

        ChallengeTime = sp.getLong("ChallengeGetTime", 0);

        if(ChallengeTime == 0){
            ChallengeTime = System.currentTimeMillis();
            sp.edit().putLong("ChallengeGetTime",ChallengeTime).apply();
        }


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long time = System.currentTimeMillis() - ChallengeTime; // 현재시간 - 시작시간
                if(num == 0 && time > trophyTimes[0]){                 // tropthyTimes = 배열값
                    trophy2.setImageResource(R.drawable.icon_bright);
                    num = 1;
                }

                if(num == 1 && time > trophyTimes[1]){
                    trophy3.setImageResource(R.drawable.icon_bright);
                    num = 2;
                }

                if(num == 2 && time > trophyTimes[2]){
                    trophy4.setImageResource(R.drawable.icon_bright);
                    num = 3;
                }

            }
        },0,1000);

    }
}
    