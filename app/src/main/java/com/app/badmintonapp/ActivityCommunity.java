package com.app.badmintonapp; // 패키지명 확인

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityCommunity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 방금 만든 XML과 연결
        View view = inflater.inflate(R.layout.activity_commuity, container, false);

        // (선택사항) 글쓰기 버튼 기능 예시
        FloatingActionButton btnWrite = view.findViewById(R.id.btn_commuity); // XML에 id가 없으면 추가해야 함
        if (btnWrite != null) {
            btnWrite.setOnClickListener(v ->
                    Toast.makeText(getContext(), "글쓰기 화면 준비 중!", Toast.LENGTH_SHORT).show()
            );
        }

        return view;
    }
}