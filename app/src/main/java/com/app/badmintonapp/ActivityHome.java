package com.app.badmintonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ActivityHome extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. 화면(XML) 연결
        View view = inflater.inflate(R.layout.activity_home, container, false);

        // 2. "더 많은 이야기 보기" 버튼 찾기

        View btnMoreStories = view.findViewById(R.id.btn_commuity);

        // 3. 클릭 이벤트 설정 (소통 공간으로 이동)
        if (btnMoreStories != null) {
            btnMoreStories.setOnClickListener(v -> {
                // MainActivity의 loadFragment 기능을 빌려와서 화면을 전환합니다.
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).loadFragment(new ActivityCommunity(), true);
                }
            });
        } else {
            // (참고용) 만약 ID를 못 찾았다면 개발자에게 알려줌 (테스트 후 지워도 됨)
            // System.out.println("버튼을 찾을 수 없습니다. XML ID를 확인하세요.");
        }

        return view;
    }
}