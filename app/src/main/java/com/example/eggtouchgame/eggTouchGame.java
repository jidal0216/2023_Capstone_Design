package com.example.eggtouchgame;

import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
//계란 까기 게임
public class eggTouchGame implements View.OnTouchListener {
    private ImageView eggImageView;
    private TextView touchCountTextView;
    private int touchCount = 0;
    private boolean touching = false;
    private MediaPlayer crackSoundPlayer; // 효과음 재생을 위한 MediaPlayer 객체
    private MediaPlayer crackSoundPlayer1; // 효과음 재생을 위한 MediaPlayer 객체

    public eggTouchGame(ImageView eggImageView, TextView touchCountTextView) {
        this.eggImageView = eggImageView;
        this.touchCountTextView = touchCountTextView;
        eggImageView.setOnTouchListener(this);

        crackSoundPlayer = MediaPlayer.create(eggImageView.getContext(), R.raw.crack_sound);
        crackSoundPlayer1 = MediaPlayer.create(eggImageView.getContext(), R.raw.crack_finish);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (!touching) {
                touching = true;
                touchCount++;
                touchCountTextView.setText(touchCount + "번 참았어요.");
                // 효과음 재생
                crackSoundPlayer.start();

                // 화면 흔들림 효과
                ObjectAnimator shakeAnimation = ObjectAnimator.ofFloat(eggImageView, "translationX", -20f, 20f);
                shakeAnimation.setDuration(100);
                shakeAnimation.setInterpolator(new AccelerateInterpolator());
                shakeAnimation.setRepeatCount(5);
                shakeAnimation.setRepeatMode(ObjectAnimator.REVERSE);
                shakeAnimation.start();
                if (touchCount == 50) {
                    touchCountTextView.setText("알에서 토끼가 나왔네요.");
                    eggImageView.setImageResource(R.drawable.normalegg);
                } else if (touchCount >= 100) {
                    eggImageView.setImageResource(R.drawable.badegg);
                    crackSoundPlayer1.start();

                    eggImageView.setOnTouchListener(null); // 터치 이벤트 비활성화
                    touchCountTextView.setText("토끼를 터치하시면\n" + "다시 시작됩니다!");
                    crackSoundPlayer1.start();
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            touching = false;
        }
        return true;
    }
}