package com.example.eggtouchgame;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//계란 까기 게임
public class eggTouchGame implements View.OnTouchListener {
    private ImageView eggImageView;
    private TextView touchCountTextView;

    private int touchCount = 0;

    public eggTouchGame(ImageView eggImageView, TextView touchCountTextView) {
        this.eggImageView = eggImageView;
        this.touchCountTextView = touchCountTextView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchCount++;
        touchCountTextView.setText(touchCount + "번 참았어요.");
        if (touchCount == 50) {
            eggImageView.setImageResource(R.drawable.normalegg);
        } else if (touchCount >= 100) {
            eggImageView.setImageResource(R.drawable.badegg);
            eggImageView.setOnTouchListener(null); // 터치 이벤트 비활성화
            touchCountTextView.setText("계란을 터치하시면\n" + "다시 시작됩니다!");
        }
        return true;
    }
}