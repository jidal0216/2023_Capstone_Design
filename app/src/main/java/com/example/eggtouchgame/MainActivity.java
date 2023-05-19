package com.example.eggtouchgame;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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