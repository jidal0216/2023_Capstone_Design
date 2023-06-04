// 9번
package com.example.capstonb;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
public class EffectActivity extends AppCompatActivity {
    private LinearLayout effectContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        effectContainer = findViewById(R.id.effectContainer);
        // 금연 시간에 따라 효과를 표시합니다.
        // SharedPreference에서 값을 가져옵니다.
        SharedPreferences sharedPreferences = getSharedPreferences("timer_prefs", MODE_PRIVATE);
        int quitTimeInMinutes = sharedPreferences.getInt("quitTimeInMinutes", 0);  // -> 이 변수를 금연한 시간으로 바꿔야함.(일단 임의로 설정해놨습니다.)
        displayEffectsForQuitTime(quitTimeInMinutes);
    }
    private void displayEffectsForQuitTime(int quitTimeInMinutes) {
        String[] effects = {
                "20분: 심박수와 혈압이 떨어집니다.",
                "12시간: 혈액의 일산화탄소 수치가 정상으로 떨어집니다.",
                "12주: 폐 기능이 회복되며, 혈액 순환이 좋아집니다.",
                "9달: 기침과 호흡 곤란이 줄어듭니다.",
                "1년: 심장병의 위험은 흡연자의 절반 정도로 줄어듭니다.",
                "5년: 뇌졸중에 걸릴 위험이 비 흡연자와 같아집니다.",
                "10년: 폐암의 위험이 줄어듭니다.",
                "15년: 심장병에 걸릴 위험이 비 흡연자와 동일해집니다."
        };

        // 효과를 표시할 TextView를 동적으로 생성하고, 효과를 설정합니다.
        for (int i = 0; i < effects.length; i++) {
            TextView effectTextView = new TextView(this);
            effectTextView.setText(effects[i]);

            // 금연 시간에 따라 효과를 표시할지 결정합니다.
            int currentQuitTime = getQuitTimeInMinutes(i);
            int nextQuitTime = getQuitTimeInMinutes(i + 1);

            if (quitTimeInMinutes >= currentQuitTime && (nextQuitTime == 0 || quitTimeInMinutes < nextQuitTime)) {
                effectTextView.setBackgroundResource(R.drawable.effect_box_enabled);  // 활성화된 박스
            } else {
                effectTextView.setBackgroundResource(R.drawable.effect_box_disabled);  // 비활성화된 박스
            }

            effectTextView.setPadding(64, 64, 64, 64);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 16, 0, 16);

            effectTextView.setLayoutParams(layoutParams);

            effectContainer.addView(effectTextView);
        }
    }
    private int getQuitTimeInMinutes(int index) {
        switch (index) {
            case 0:
                return 20;  // 20분
            case 1:
                return 720;  // 12시간 (12시간 * 60분)
            case 2:
                return 10080;  // 12주 (12주 * 7일 * 24시간 * 60분)
            case 3:
                return 38880;  // 9달 (9달 * 30일 * 24시간 * 60분)
            case 4:
                return 525600;  // 1년 (1년 * 365일 * 24시간 * 60분)
            case 5:
                return 2628000;  // 5년 (5년 * 365일 * 24시간 * 60분)
            case 6:
                return 5256000;  // 10년 (10년 * 365일 * 24시간 * 60분)
            case 7:
                return 7884000;  // 15년 (15년 * 365일 * 24시간 * 60분)
            default:
                return 0;
        }
    }
}
