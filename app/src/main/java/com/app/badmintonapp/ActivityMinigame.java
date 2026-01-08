package com.app.badmintonapp; // ★ 패키지명 확인!

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class ActivityMinigame extends Fragment {

    // 게임 요소
    private ImageView shuttlecock;
    private TextView tvScore, tvGameStatus;
    private LinearLayout layoutGameStart;
    private Button btnStartGame;
    private ConstraintLayout gameContainer;

    // 게임 물리 변수
    private float shuttlecockY; // 셔틀콕의 높이 위치
    private float velocity = 0; // 속도 (양수면 아래로, 음수면 위로)
    private int score = 0;
    private boolean isGameRunning = false;

    // 설정 값
    private final float GRAVITY = 1.5f; // 중력 (내려가는 힘)
    private final float JUMP_FORCE = -25f; // 점프 힘 (터치 시 올라가는 힘)
    private int screenHeight = 0; // 화면 높이

    // 게임 루프를 위한 핸들러
    private Handler handler = new Handler();
    private Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (isGameRunning) {
                updatePhysics(); // 물리 계산
                checkCollision(); // 바닥에 닿았는지 확인
                handler.postDelayed(this, 20); // 0.02초마다 반복 (약 50fps)
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_minigame, container, false);

        // 뷰 찾기
        shuttlecock = view.findViewById(R.id.iv_shuttlecock);
        tvScore = view.findViewById(R.id.tv_score);
        tvGameStatus = view.findViewById(R.id.tv_game_status);
        layoutGameStart = view.findViewById(R.id.layout_game_start);
        btnStartGame = view.findViewById(R.id.btn_start_game);
        gameContainer = view.findViewById(R.id.game_container);

        // 게임 시작 버튼 클릭
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        // 화면 전체 터치 이벤트 (점프)
        gameContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isGameRunning) {
                        jump();
                    }
                }
                return true; // 이벤트 소비
            }
        });

        return view;
    }

    // 게임 시작 초기화
    private void startGame() {
        isGameRunning = true;
        score = 0;
        velocity = 0;
        tvScore.setText("Score: 0");

        // 화면 높이 계산 (경계선 체크용)
        screenHeight = gameContainer.getHeight();

        // 셔틀콕 위치 중앙으로 리셋
        shuttlecockY = screenHeight / 2f;
        shuttlecock.setY(shuttlecockY);

        // 안내창 숨기기
        layoutGameStart.setVisibility(View.GONE);

        // 게임 루프 시작
        handler.post(gameRunnable);
    }

    // 물리 엔진 (위치 업데이트)
    private void updatePhysics() {
        velocity += GRAVITY; // 계속 중력이 더해짐 (점점 빨라짐)
        shuttlecockY += velocity; // 위치 이동

        // 천장에 닿으면 더 이상 못 올라가게 막음 (옵션)
        if (shuttlecockY < 0) {
            shuttlecockY = 0;
            velocity = 0;
        }

        shuttlecock.setY(shuttlecockY); // 실제 화면에 반영
    }

    // 점프 (터치 시)
    private void jump() {
        velocity = JUMP_FORCE; // 위로 솟구침
        score++; // 점수 증가
        tvScore.setText("Score: " + score);
    }

    // 충돌 감지 (바닥에 닿았는지)
    private void checkCollision() {
        // 셔틀콕 높이 + 셔틀콕 이미지 크기가 화면 바닥보다 크면 닿은 것
        if (shuttlecockY + shuttlecock.getHeight() >= screenHeight) {
            gameOver();
        }
    }

    // 게임 오버 처리
    private void gameOver() {
        isGameRunning = false;
        handler.removeCallbacks(gameRunnable); // 루프 정지

        // 안내창 띄우기
        tvGameStatus.setText("게임 오버!\n최종 점수: " + score + "점");
        btnStartGame.setText("다시 하기");
        layoutGameStart.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 화면 벗어나면 게임 정지
        isGameRunning = false;
        handler.removeCallbacks(gameRunnable);
    }
}