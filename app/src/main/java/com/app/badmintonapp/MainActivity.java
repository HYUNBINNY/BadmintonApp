package com.app.badmintonapp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color; // ★ 추가됨
import android.graphics.Typeface; // ★ 추가됨
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // ★ 추가됨
import android.widget.LinearLayout;
import android.widget.TextView; // ★ 추가됨
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnHome, btnRules, btnGame, btnPlace, btnShop;

    private long lastBackPressedTime = 0L;
    private static final long BACK_PRESS_INTERVAL = 2000L; // 2초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 뷰 연결
        btnHome = findViewById(R.id.btn_nav_home);
        btnRules = findViewById(R.id.btn_nav_rules);
        btnGame = findViewById(R.id.btn_nav_game);
        btnPlace = findViewById(R.id.btn_nav_place);
        btnShop = findViewById(R.id.btn_nav_shop);

        // 2. 앱 시작 시 홈 화면 로드
        if (savedInstanceState == null) {
            loadFragment(new ActivityHome(), false);
        }

        // 3. 뒤로가기 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return;
                }
                long now = System.currentTimeMillis();
                if (now - lastBackPressedTime < BACK_PRESS_INTERVAL) {
                    finish();
                } else {
                    lastBackPressedTime = now;
                    Toast.makeText(MainActivity.this, "종료하시겠습니까? (한 번 더 누르면 종료)", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 4. 버튼 클릭 리스너 설정 (클릭 시 색상 변경 + 화면 이동)

        // 홈
        btnHome.setOnClickListener(v -> {
            updateBottomMenu(btnHome); // ★ 버튼 색상 변경
            loadFragment(new ActivityHome(), false);
        });

        // 규칙
        btnRules.setOnClickListener(v -> {
            updateBottomMenu(btnRules); // ★ 버튼 색상 변경
            loadFragment(new ActivityRules(), true);
        });

        // 미니게임
        btnGame.setOnClickListener(v -> {
            updateBottomMenu(btnGame); // ★ 버튼 색상 변경
            try {
                loadFragment(new ActivityMinigame(), true);
            } catch (Exception e) {
                Toast.makeText(this, "미니게임 화면 준비중!", Toast.LENGTH_SHORT).show();
            }
        });

        // 체육관
        btnPlace.setOnClickListener(v -> {
            updateBottomMenu(btnPlace); // ★ 버튼 색상 변경
            Toast.makeText(this, "준비 중입니다", Toast.LENGTH_SHORT).show();
        });

        // 쇼핑
        btnShop.setOnClickListener(v -> {
            updateBottomMenu(btnShop); // ★ 버튼 색상 변경
            try {
                loadFragment(new ActivityShopping(), true);
            } catch (Exception e) {
                Toast.makeText(this, "쇼핑 화면 준비중!", Toast.LENGTH_SHORT).show();
            }
        });

        // ★ 앱 시작 시 홈 버튼 색상 적용 (onCreate 마지막에 위치해야 함)
        updateBottomMenu(btnHome);
    }

    /**
     * 하단 메뉴 색상 변경 함수
     * 선택된 메뉴는 검은색/굵게, 나머지는 회색/보통으로 변경
     */
    private void updateBottomMenu(LinearLayout selectedLayout) {
        // 모든 메뉴 버튼을 배열에 담기
        LinearLayout[] menus = {btnHome, btnRules, btnGame, btnPlace, btnShop};

        for (LinearLayout layout : menus) {
            // XML 구조상 첫 번째는 이미지(0), 두 번째는 텍스트(1)
            ImageView icon = (ImageView) layout.getChildAt(0);
            TextView text = (TextView) layout.getChildAt(1);

            if (layout == selectedLayout) {
                // 선택된 버튼: 검은색(#000000) & 글씨 굵게
                icon.setColorFilter(Color.parseColor("#000000"));
                text.setTextColor(Color.parseColor("#000000"));
                text.setTypeface(null, Typeface.BOLD);
            } else {
                // 나머지 버튼: 회색(#888888) & 글씨 보통
                icon.setColorFilter(Color.parseColor("#888888"));
                text.setTextColor(Color.parseColor("#888888"));
                text.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    /**
     * Fragment 교체
     */
    protected void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    // 상단 프로필 버튼 (Activity로 전환)
    public void onProfileClick(View view) {
        try {
            startActivity(new Intent(MainActivity.this, ActivityProfile.class));
        } catch (Exception e) {
            Toast.makeText(this, "프로필 Activity가 아직 없어요!", Toast.LENGTH_SHORT).show();
        }
    }
}