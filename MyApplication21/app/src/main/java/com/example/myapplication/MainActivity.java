package com.example.myapplication;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tomer.fadingtextview.FadingTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // String[] texts = {"text1","text2","text3"};

        FadingTextView FTV = (FadingTextView) findViewById(R.id.fadingTextView);
        // FTV.setTexts(texts);
        FTV.setTimeout(7, SECONDS);
    }
}