package com.app.badmintonapp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout btnHome, btnRules, btnGame, btnPlace, btnShop;

    private long lastBackPressedTime = 0L;
    private static final long BACK_PRESS_INTERVAL = 2000L; // 2초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHome = findViewById(R.id.btn_nav_home);
        btnRules = findViewById(R.id.btn_nav_rules);
        btnGame = findViewById(R.id.btn_nav_game);
        btnPlace = findViewById(R.id.btn_nav_place);
        btnShop = findViewById(R.id.btn_nav_shop);

        // ✅ 앱 시작 시 홈
        if (savedInstanceState == null) {
            loadFragment(new ActivityHome(), false);
        }

        // ✅ 뒤로가기(제스처/버튼) 처리: OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                // 1) 백스택이 남아있으면: 이전 Fragment로
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return;
                }

                // 2) 백스택이 없으면(=홈): 두 번 눌러 종료
                long now = System.currentTimeMillis();
                if (now - lastBackPressedTime < BACK_PRESS_INTERVAL) {
                    finish();
                } else {
                    lastBackPressedTime = now;
                    Toast.makeText(MainActivity.this,
                            "종료하시겠습니까? (한 번 더 누르면 종료)",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 홈
        btnHome.setOnClickListener(v -> loadFragment(new ActivityHome(), false));

        // 규칙 (Fragment)
        btnRules.setOnClickListener(v -> loadFragment(new ActivityRules(), true));

        // 미니게임 (Fragment)
        btnGame.setOnClickListener(v -> {
            try {
                loadFragment(new ActivityMinigame(), true);
            } catch (Exception e) {
                Toast.makeText(this, "미니게임 화면 준비중!", Toast.LENGTH_SHORT).show();
            }
        });

        // 체육관: 토스트
        btnPlace.setOnClickListener(v ->
                Toast.makeText(this, "준비 중입니다", Toast.LENGTH_SHORT).show()
        );

        // 쇼핑 (Fragment)
        btnShop.setOnClickListener(v -> {
            try {
                loadFragment(new ActivityShopping(), true);
            } catch (Exception e) {
                Toast.makeText(this, "쇼핑 화면 준비중!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fragment 교체
     * @param fragment 교체할 Fragment
     * @param addToBackStack true면 뒤로가기 시 이전 화면으로 돌아감
     */
    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            // ✅ 홈으로 돌아올 때 백스택 정리 (뒤로가기 누르면 종료 흐름 유지)
            getSupportFragmentManager().popBackStack(
                    null,
                    getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE
            );

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
