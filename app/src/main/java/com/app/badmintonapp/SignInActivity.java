package com.app.badmintonapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText etSignupId, etSignupPw, etSignupPwConfirm;
    private MaterialCardView btnProceed;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();
        /*
        // ✅ activity_signin.xml에 id 추가되어 있어야 컴파일 됨
        etSignupId = findViewById(R.id.et_signup_id);
        etSignupPw = findViewById(R.id.et_signup_password);
        etSignupPwConfirm = findViewById(R.id.et_signup_password_confirm);
        */
        btnProceed = findViewById(R.id.btn_signup_proceed);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());
        btnProceed.setOnClickListener(v -> doSignUp());
    }

    private void doSignUp() {
        String email = etSignupId.getText().toString().trim(); // ✅ 아이디칸=이메일
        String pw = etSignupPw.getText().toString().trim();
        String pw2 = etSignupPwConfirm.getText().toString().trim();

        if (email.isEmpty() || pw.isEmpty() || pw2.isEmpty()) {
            Toast.makeText(this, "필수 항목을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pw.equals(pw2)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pw.length() < 6) {
            Toast.makeText(this, "비밀번호는 6자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                        finish(); // 로그인 화면으로 돌아감
                    } else {
                        String msg = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "회원가입 실패";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}