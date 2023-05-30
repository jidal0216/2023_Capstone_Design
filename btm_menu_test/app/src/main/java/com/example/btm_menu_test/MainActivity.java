package com.example.btm_menu_test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Shake 관련
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isShaking = false;

    //여기까지 Shake 관련


    private BottomNavigationView bottomNavigationView;   //    하단 메뉴바 관련

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 여기부터 Shake 기능
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        // 여기까지 Shake 기능

        //    하단 메뉴바 관련
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.menu_item1) {
                    startActivity(new Intent(MainActivity.this, CalenderActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_item2) {
                    startActivity(new Intent(MainActivity.this, TipActivity.class));
                    return true;
                } else if (item.getItemId() == R.id.menu_item3) {
                    startActivity(new Intent(MainActivity.this, GameActivity.class));
                    return true;
                }

                return false;
            }
        });
        //    여기까지 하단 메뉴바 관련

    }

        // Shake 기능
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            if ((Math.abs(x) > 15 || Math.abs(y) > 15 || Math.abs(z) > 15) && !isShaking) {
                isShaking = true;
                RandomMotivation();
            }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    private void RandomMotivation() {
        String[] motivation = {
                "큰 목표를 이루고 싶으면 허락을 구하지 마라.",
                "일반적인 것을 잃을 위험을 감수하지 않으면 평범한 것에 만족해야 한다.",
                "추구할 수 있는 용기가 있다면 우리의 모든 꿈은 이뤄질 수 있다.",
                "기다리는 사람에게 좋은 일이 생기지만, 찾아나서는 사람에게는 더 좋은 일이 생긴다.",
                "열정을 잃지 않고 실패에서 실패로 걸어가는 것이 성공이다.",
                "기회는 일어나는 것이 아니라 만들어내는 것이다.",
                "나는 실패한 게 아니다. 나는 잘 되지 않는 방법 1만 가지를 발견한 것이다.",
                "성공적인 삶의 비밀은 무엇을 하는 게 자신의 운명인지 찾아낸 다음 그걸 하는 것이다.",
                "지옥을 겪고 있다면 계속 겪어 나가라.",
                "괴로운 시련처럼 보이는 것이 뜻밖의 좋은 일일 때가 많다.",
                "잘못된 것들을 쫓아다니는 것을 그만두면 옳은 일들이 당신을 따라잡을 기회가 생긴다.",
                "매일 당신을 두렵게 만드는 일을 하나씩 하라.",
                "놀라운 일을 하려고 노력조차 하지 않을 거면 살아 있어서 뭐하나.",
                "인생이란 자신을 찾는 것이 아니라 자신을 만드는 것이다.",
                "지식이란 당신이 뭘 할 수 있는지 아는 것이다. 지혜란 하지 않아야 할 때를 아는 것이다.",
                "당신의 문제가 문제가 아니라 당신의 반응이 문제다",
                "당신이 세상을 바꿀 수 없다고 말하는 사람에는 두 종류가 있다. 시도하기를 두려워하는 사람들, 당신이 성공할까 봐 두려워하는 사람들.",
                "나는 내가 더 노력할수록 운이 더 좋아진다는 걸 발견했다.",
                "모든 성취의 시작점은 갈망이다.",
                "성공은 매일 반복한 작은 노력들의 합이다.",
                "사람들은 동기 부여는 오래가지 않는다고 말한다. 목욕도 마찬가지다. 그래서 매일 하라고하는 것이다.",
                "성공으로 가는 길과 실패로 가는 길은 거의 똑같다.",
                "실패에서부터 성공을 만들어 내라. 좌절과 실패는 성공으로 가는 가장 확실한 디딤돌이다.",
                "당신의 인생을 스스로 설계하지 않으면 다른 사람의 계획에 빠져들 가능성이 크다. 남들이 당신을 위해 계획해 놓은 것? 많지 않다.",
                "패배의 공포가 승리의 짜릿함보다 커지게 하지 마라.",
                "성공이란 절대 실수를 하지 않는 게 아니라 같은 실수를 두 번 하지 않는 것에 있다.",
                "진짜 어려움은 극복할 수 있다. 정복할 수 없는 것은 상상 속의 어려움들뿐이다.",
                "성취의 크기는 목표를 이루기 위해 당신이 극복해야 했던 장애물의 크기로 잰다.",
                "실패는 성공을 맛내는 양념이다.",
                "낭비한 시간에 대한 후회는 더 큰 시간 낭비이다.",
                "어제로 돌아갈 수 없다. 왜냐하면 나는 어제와는 다른 사람이 되었기 때문이다.",
                "해야할 일은 과감히 하라. 결심한 일은 반드시 실행하라.",
                "최대한의 삶을 살고, 최대한 긍정적인 것에 집중하자.",
                "최선을 다하고 있다고 말해봤자 소용없다. 필요한 일을 함에 있어서는 반드시 성공해야 한다.",
                "동기 부여가 당신을 시작하게 한다. 습관이 당신을 계속 움직이게 한다."
        };

        Random random = new Random();
        int index = random.nextInt(motivation.length);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("포기하지 마세요!");
        builder.setMessage(motivation[index]);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShaking = false;
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
        // 여기까지 Shake 기능

}