package com.app.badmintonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    // 뒤로가기
    public void onBackClick(View view) {
        finish();
    }

    // ✅ 로그인 화면으로 이동
    public void onProfileLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // ✅ 회원가입 화면으로 이동
    public void onProfileSignupClick(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}