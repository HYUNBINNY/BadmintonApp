package com.app.badmintonapp; // ★ 본인의 패키지 이름 확인

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 앱 실행 시 로그인 화면 먼저 표시
        initMainApp();
    }

    // ==========================================
    // 로그인 화면
    // ==========================================
    private void showLoginScreen() {
        setContentView(R.layout.activity_login);

        // 회원가입 버튼
        TextView tvSignup = findViewById(R.id.tv_signup);
        if (tvSignup != null) {
            tvSignup.setOnClickListener(v -> showSignupScreen());
        }

        // 로그인 버튼
        View btnLogin = findViewById(R.id.btn_login);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "환영합니다!", Toast.LENGTH_SHORT).show();
                initMainApp();
            });
        }
    }

    // ==========================================
    //  회원가입 화면
    // ==========================================
    private void showSignupScreen() {
        setContentView(R.layout.activity_signin);

        // 뒤로가기 -> 로그인 화면
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> showLoginScreen());
        }

        // 가입 완료 -> 로그인 화면
        View btnProceed = findViewById(R.id.btn_signup_proceed);
        if (btnProceed != null) {
            btnProceed.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, "가입 완료!", Toast.LENGTH_SHORT).show();
                showLoginScreen();
            });
        }
    }

    // ==========================================
    // 메인 앱 화면
    // ==========================================
    private void initMainApp() {
        setContentView(R.layout.activity_main); // activity_main.xml 로드

        // 1. 초기 화면 설정 (ActivityHome 프래그먼트)
        replaceFragment(new ActivityHome());

        // 2. 하단 탭 버튼 클릭 리스너 설정
        LinearLayout btnHome = findViewById(R.id.btn_nav_home);
        LinearLayout btnRules = findViewById(R.id.btn_nav_rules);
        LinearLayout btnGame = findViewById(R.id.btn_nav_game);
        LinearLayout btnPlace = findViewById(R.id.btn_nav_place);
        LinearLayout btnShop = findViewById(R.id.btn_nav_shop);

        if (btnHome != null) btnHome.setOnClickListener(v -> replaceFragment(new ActivityHome()));
        if (btnRules != null) btnRules.setOnClickListener(v -> replaceFragment(new ActivityRules()));
        if (btnGame != null) btnGame.setOnClickListener(v -> replaceFragment(new ActivityMinigame()));
        if (btnPlace != null) btnPlace.setOnClickListener(v -> Toast.makeText(this, "준비 중입니다.", Toast.LENGTH_SHORT).show());
        if (btnShop != null) btnShop.setOnClickListener(v -> replaceFragment(new ActivityShopping()));
    }

    // 프래그먼트 교체 헬퍼 함수
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // ==========================================
    // 창 이동
    // ==========================================

    // [메인] 상단 프로필 아이콘 클릭 시 실행
    public void onProfileClick(View v) {
        setContentView(R.layout.activity_profile); // 프로필 화면으로 통째로 교체
    }

    // [프로필] 뒤로가기 아이콘 클릭 시 실행
    public void onBackClick(View v) {
        initMainApp(); // 메인 화면으로 전환
    }
    // ==========================================
    // 하단 버튼 클릭
    // ==========================================

    // 프로필 화면에서 '로그인' 버튼 클릭 시
    public void onProfileLoginClick(View v) {
        // 이미 만들어둔 로그인 화면 보여주는 함수 재사용
        showLoginScreen();
    }

    // 프로필 화면에서 '회원가입' 버튼 클릭 시
    public void onProfileSignupClick(View v) {
        // 이미 만들어둔 회원가입 화면 보여주는 함수 재사용
        showSignupScreen();
    }
}