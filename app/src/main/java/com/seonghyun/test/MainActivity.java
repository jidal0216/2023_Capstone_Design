package com.seonghyun.test;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private long[] trophyTimes = {1000,        // 1초
            86400000,    // 1일
            259200000,   // 3일
            604800000,   // 7일
    };
    private ImageView trophy1, trophy2, trophy3, trophy4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trophy1 = findViewById(R.id.imageView1);
        trophy2 = findViewById(R.id.imageView2);
        trophy3 = findViewById(R.id.imageView3);
        trophy4 = findViewById(R.id.imageView4);

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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trophy2.setImageResource(R.drawable.icon_bright);
                    }
                });
            }
        }, trophyTimes[1]); // 3일 후

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trophy3.setImageResource(R.drawable.icon_bright);
                    }
                });
            }
        }, trophyTimes[2]); // 7일 후

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        trophy4.setImageResource(R.drawable.icon_bright);
                    }
                });
            }
        }, trophyTimes[3] + (trophyTimes[3] * 3)); // 30일 후
    }
}
    