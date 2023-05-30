package com.example.eggtouchgame;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

//메인 액티비티에서 계란까기 게임을 위해 변수 선언해야 할 것들
public class MainActivity extends AppCompatActivity {
    private ImageView eggImageView;
    private TextView touchCountTextView;
    private int touchCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eggImageView = findViewById(R.id.eggImageView);
        touchCountTextView = findViewById(R.id.touchCountTextView);

        // 배경 이미지 설정
        ImageView backgroundImageView = new ImageView(eggImageView.getContext());
        backgroundImageView.setImageResource(R.drawable.background);
        backgroundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        // 게임 영역을 감싸는 컨테이너에 배경 이미지 추가
        RelativeLayout gameContainer = (RelativeLayout) eggImageView.getParent();
        gameContainer.addView(backgroundImageView, 0); // 배경 이미지를 가장 아래에 추가

        eggImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchCount = 0;
                touchCountTextView.setText("반드시 참아봅시다!");
                eggImageView.setImageResource(R.drawable.goodegg);
                eggImageView.setOnTouchListener(new eggTouchGame(eggImageView, touchCountTextView));
            }
        });
    }
}