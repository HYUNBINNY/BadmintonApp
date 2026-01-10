package com.app.badmintonapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class ActivityMinigame extends Fragment {

    // UI 요소
    private TextView tvScore, tvStatus, tvOverlayMsg, tvOverlaySub, btnRestart;
    private ImageView ivShuttlecock, ivOpponent, btnHelp;
    private Button btnSmash, btnDrive, btnHairpin, btnClear;
    private Button btnRestartYes, btnRestartNo, btnStartInitial;
    private LinearLayout layoutOverlay, controlPanel, layoutOverlayButtons;

    // 게임 변수
    private int score = 0;
    private boolean isWaitingForAction = false;
    private ShotType currentIncomingShot;
    private Random random = new Random();

    // 애니메이션 제어용
    private ObjectAnimator currentAnimator;
    private Handler gameHandler = new Handler(Looper.getMainLooper());
    private Runnable nextTurnRunnable;

    private enum ShotType {
        FAST("빠르게 날아온다!", "🚀"),
        HIGH("높게 날아온다!", "☁️"),
        STRAIGHT("일직선으로 온다!", "📏"),
        NET("네트 앞 근접!", "🕸️"),
        CHANCE("지그재그 찬스볼!", "✨");

        final String desc;
        final String icon;

        ShotType(String desc, String icon) {
            this.desc = desc;
            this.icon = icon;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_minigame, container, false);

        // UI 연결
        tvScore = view.findViewById(R.id.tv_score);
        tvStatus = view.findViewById(R.id.tv_status);
        tvOverlayMsg = view.findViewById(R.id.tv_overlay_msg);
        tvOverlaySub = view.findViewById(R.id.tv_overlay_sub);
        btnRestart = view.findViewById(R.id.btn_restart);

        ivShuttlecock = view.findViewById(R.id.iv_shuttlecock);
        ivOpponent = view.findViewById(R.id.iv_opponent);
        btnHelp = view.findViewById(R.id.btn_help);

        btnSmash = view.findViewById(R.id.btn_smash);
        btnDrive = view.findViewById(R.id.btn_drive);
        btnHairpin = view.findViewById(R.id.btn_hairpin);
        btnClear = view.findViewById(R.id.btn_clear);

        layoutOverlay = view.findViewById(R.id.layout_overlay);
        layoutOverlayButtons = view.findViewById(R.id.layout_overlay_buttons);
        btnRestartYes = view.findViewById(R.id.btn_restart_yes);
        btnRestartNo = view.findViewById(R.id.btn_restart_no);
        btnStartInitial = view.findViewById(R.id.btn_start_initial);

        controlPanel = view.findViewById(R.id.control_panel);

        // --- 리스너 설정 ---

        // 1. 처음 들어왔을 때 시작 화면 설정
        showInitialStartScreen();

        // 2. 상단 재시작(새로고침) 아이콘
        btnRestart.setOnClickListener(v -> {
            Toast.makeText(getContext(), "게임을 재시작합니다.", Toast.LENGTH_SHORT).show();
            resetGame();
        });

        // 3. 오버레이 버튼들
        btnStartInitial.setOnClickListener(v -> startGame()); // 최초 시작
        btnRestartYes.setOnClickListener(v -> resetGame());   // 실패 후 재시작 (예)
        btnRestartNo.setOnClickListener(v -> {                // 실패 후 나가기 (아니오)
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack(); // 뒤로가기
            }
        });

        btnHelp.setOnClickListener(v -> showHelpDialog());

        // 4. 공격 버튼들
        btnSmash.setOnClickListener(v -> processPlayerAction("SMASH"));
        btnDrive.setOnClickListener(v -> processPlayerAction("DRIVE"));
        btnHairpin.setOnClickListener(v -> processPlayerAction("HAIRPIN"));
        btnClear.setOnClickListener(v -> processPlayerAction("CLEAR"));

        return view;
    }

    // 앱 처음 켰을 때 화면 세팅
    private void showInitialStartScreen() {
        layoutOverlay.setVisibility(View.VISIBLE);
        tvOverlayMsg.setText("전략 배드민턴\n리턴 게임");
        tvOverlaySub.setVisibility(View.GONE); // 질문 숨김
        layoutOverlayButtons.setVisibility(View.GONE); // 예/아니오 숨김
        btnStartInitial.setVisibility(View.VISIBLE); // 시작 버튼 보임
    }

    // 게임 재시작 (초기화)
    private void resetGame() {
        if (currentAnimator != null && currentAnimator.isRunning()) {
            currentAnimator.cancel();
        }
        if (nextTurnRunnable != null) {
            gameHandler.removeCallbacks(nextTurnRunnable);
        }

        startGame();
    }

    private void startGame() {
        score = 0;
        updateScore();

        // 오버레이 숨기기
        layoutOverlay.setVisibility(View.GONE);
        controlPanel.setVisibility(View.VISIBLE);

        startNextTurn();
    }

    private void startNextTurn() {
        isWaitingForAction = true;

        int rand = random.nextInt(100);
        if (rand < 5) currentIncomingShot = ShotType.CHANCE;
        else {
            ShotType[] types = {ShotType.FAST, ShotType.HIGH, ShotType.STRAIGHT, ShotType.NET};
            currentIncomingShot = types[random.nextInt(types.length)];
        }

        tvStatus.setText(currentIncomingShot.icon + " " + currentIncomingShot.desc);
        tvStatus.setVisibility(View.VISIBLE);
        playShuttlecockAnimation(currentIncomingShot);
    }

    private void playShuttlecockAnimation(ShotType type) {
        ivShuttlecock.setVisibility(View.VISIBLE);
        ivShuttlecock.setTranslationX(0);
        ivShuttlecock.setTranslationY(-300);

        currentAnimator = ObjectAnimator.ofFloat(ivShuttlecock, "translationY", -300f, 600f); // 바닥까지

        long baseDuration = 1500;

        if (type == ShotType.FAST) {
            baseDuration = 600;
            currentAnimator.setInterpolator(new LinearInterpolator());
        } else if (type == ShotType.HIGH) {
            baseDuration = 2000;
            currentAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        } else if (type == ShotType.CHANCE) {
            baseDuration = 2500;
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(ivShuttlecock, "translationX", -100f, 100f);
            animatorX.setDuration(300);
            animatorX.setRepeatCount(8);
            animatorX.setRepeatMode(ObjectAnimator.REVERSE);
            animatorX.start();
        }

        float speedFactor = Math.max(0.4f, 1.0f - (score * 0.03f));
        long finalDuration = (long) (baseDuration * speedFactor);

        currentAnimator.setDuration(finalDuration);

        // ★ 핵심: 애니메이션 리스너 추가 (타임아웃 감지)
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 애니메이션이 끝났는데 아직 사용자가 안 눌렀다면? -> 시간 초과 실패!
                if (isWaitingForAction) {
                    isWaitingForAction = false; // 중복 실행 방지
                    gameOver("시간 초과!\n너무 늦었습니다.");
                }
            }
        });

        currentAnimator.start();
    }

    private void processPlayerAction(String playerAction) {
        if (!isWaitingForAction) return;
        isWaitingForAction = false;

        ivShuttlecock.setVisibility(View.INVISIBLE);
        if (currentAnimator != null) {
            currentAnimator.removeAllListeners(); // 리스너 제거 (성공했으니 타임아웃 안 뜨게)
            currentAnimator.cancel();
        }

        int winChance = 15;

        switch (currentIncomingShot) {
            case FAST:
                if (playerAction.equals("CLEAR")) winChance = 90;
                else if (playerAction.equals("DRIVE")) winChance = 50;
                break;
            case HIGH:
                if (playerAction.equals("SMASH")) winChance = 90;
                else if (playerAction.equals("CLEAR")) winChance = 50;
                break;
            case STRAIGHT:
                if (playerAction.equals("DRIVE")) winChance = 90;
                else if (playerAction.equals("HAIRPIN")) winChance = 50;
                break;
            case NET:
                if (playerAction.equals("HAIRPIN")) winChance = 90;
                else if (playerAction.equals("CLEAR")) winChance = 50;
                break;
            case CHANCE:
                winChance = 90;
                break;
        }

        int roll = random.nextInt(100) + 1;

        if (roll <= winChance) {
            // 성공
            score++;
            updateScore();
            String msg = (score > 10) ? "엄청난 반응속도!" : "나이스 샷!";
            Toast.makeText(getContext(), msg + " (" + winChance + "% 성공)", Toast.LENGTH_SHORT).show();

            nextTurnRunnable = this::startNextTurn;
            gameHandler.postDelayed(nextTurnRunnable, 1000);

        } else {
            // ★ 실패 (버튼은 눌렀으나 확률로 실패함)
            gameOver("기술 실패!\n(" + winChance + "% 확률 실패)");
        }
    }

    private void updateScore() {
        tvScore.setText("SCORE: " + score);
    }

    // ★ 게임 오버 처리 (문구와 예/아니오 버튼 표시)
    private void gameOver(String reason) {
        layoutOverlay.setVisibility(View.VISIBLE);

        // 문구 설정
        tvOverlayMsg.setText("GAME OVER\n" + reason);
        tvOverlaySub.setText("최종 점수: " + score + "\n재시작하겠습니까?");

        // 예/아니오 버튼 보이기
        tvOverlaySub.setVisibility(View.VISIBLE);
        layoutOverlayButtons.setVisibility(View.VISIBLE);

        // 최초 시작 버튼 숨기기
        btnStartInitial.setVisibility(View.GONE);
    }

    private void showHelpDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("💡 승리 전략 가이드")
                .setMessage("타이밍을 놓치면 실패합니다!\n\n" +
                        "🚀 빠르게 온다 -> [클리어]\n" +
                        "☁️ 높게 온다 -> [스매시]\n" +
                        "📏 일직선이다 -> [드라이브]\n" +
                        "🕸️ 네트 근접 -> [헤어핀]\n" +
                        "✨ 찬스볼 -> 아무거나\n\n" +
                        "상성에 맞으면 90%, 틀리면 15% 확률로 성공합니다.")
                .setPositiveButton("확인", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (gameHandler != null) {
            gameHandler.removeCallbacksAndMessages(null);
        }
        if (currentAnimator != null) {
            currentAnimator.removeAllListeners();
            currentAnimator.cancel();
        }
    }
}