package com.example.btm_menu_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    private BottomNavigationView bottomNavigationView;   //    하단 메뉴바 관련

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    하단 메뉴바 관련

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.menu_item1) {
                    startActivity(new Intent(MainActivity.this, CalenderActivity.class));
                    return true;
                }
                return false;
            }
        });
        //    여기까지 하단 메뉴바 관련
    }
}