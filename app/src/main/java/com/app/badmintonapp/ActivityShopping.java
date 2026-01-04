package com.app.badmintonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class ActivityShopping extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;
    private ArrayList<Product> fullProductList = new ArrayList<>(); // 전체 데이터 저장소

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_shopping, container, false);

        recyclerView = view.findViewById(R.id.rv_product_list);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_categories);

        // 1. 데이터 생성 (전체 목록)
        loadData();

        // 2. 리사이클러뷰 설정 (2칸짜리 그리드)
        adapter = new ShoppingAdapter(fullProductList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // 3. 탭 클릭 이벤트 처리
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String category = tab.getText().toString(); // 선택된 탭 이름 (라켓, 의류 등)
                filterList(category);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    // 더미 데이터 생성 함수
    private void loadData() {
        // 이미지(sample_racket)는 아까 만든 xml 파일입니다.
        // 다른 이미지가 없다면 모두 같은걸 쓰거나 기본 아이콘을 쓰세요.
        int defaultImg = R.drawable.shoppingtagracket;

        // 라켓
        fullProductList.add(new Product("요넥스 아스트록스 88D", "259,000원", "옥션", defaultImg, "라켓"));
        fullProductList.add(new Product("빅터 브레이브 소드 12", "189,000원", "쿠팡", defaultImg, "라켓"));
        fullProductList.add(new Product("리닝 에어로나트 9000", "220,000원", "네이버", defaultImg, "라켓"));
        fullProductList.add(new Product("미즈노 포티우스 10", "175,000원", "11번가", defaultImg, "라켓"));
        fullProductList.add(new Product("던롭 그라비통 8.5", "99,000원", "G마켓", defaultImg, "라켓"));

        // 의류
        fullProductList.add(new Product("국가대표 경기복", "45,000원", "요넥스몰", defaultImg, "의류"));
        fullProductList.add(new Product("빅터 4부 반바지", "32,000원", "빅터몰", defaultImg, "의류"));
        fullProductList.add(new Product("웜업 자켓", "89,000원", "옥션", defaultImg, "의류"));
        fullProductList.add(new Product("스포츠 양말 3족", "12,000원", "쿠팡", defaultImg, "의류"));
        fullProductList.add(new Product("경량 바람막이", "65,000원", "네이버", defaultImg, "의류"));

        // 신발
        fullProductList.add(new Product("요넥스 파워크션 65Z", "159,000원", "옥션", defaultImg, "신발"));
        fullProductList.add(new Product("빅터 소닉 A970", "145,000원", "빅터몰", defaultImg, "신발"));
        fullProductList.add(new Product("미즈노 웨이브 클로", "129,000원", "11번가", defaultImg, "신발"));
        fullProductList.add(new Product("아식스 젤 블레이드", "110,000원", "쿠팡", defaultImg, "신발"));
        fullProductList.add(new Product("리닝 레인저 프로", "135,000원", "G마켓", defaultImg, "신발"));

        // 가방
        fullProductList.add(new Product("요넥스 3단 사각 가방", "110,000원", "옥션", defaultImg, "가방"));
        fullProductList.add(new Product("빅터 2단 숄더백", "75,000원", "빅터몰", defaultImg, "가방"));
        fullProductList.add(new Product("배드민턴 전용 백팩", "89,000원", "쿠팡", defaultImg, "가방"));
        fullProductList.add(new Product("간편 숄더백", "45,000원", "네이버", defaultImg, "가방"));
        fullProductList.add(new Product("통기성 신발 주머니", "15,000원", "G마켓", defaultImg, "가방"));

        // 셔틀콕
        fullProductList.add(new Product("강산연 301 (겨울용)", "18,000원", "옥션", defaultImg, "셔틀콕"));
        fullProductList.add(new Product("KBB 79 (대회용)", "21,000원", "쿠팡", defaultImg, "셔틀콕"));
        fullProductList.add(new Product("요넥스 AS-50", "45,000원", "요넥스몰", defaultImg, "셔틀콕"));
        fullProductList.add(new Product("삼화 505 (가성비)", "16,500원", "11번가", defaultImg, "셔틀콕"));
        fullProductList.add(new Product("라이더 303 (연습용)", "15,000원", "G마켓", defaultImg, "셔틀콕"));

        // 기타
        fullProductList.add(new Product("오버 그립 (10개입)", "25,000원", "쿠팡", defaultImg, "기타"));
        fullProductList.add(new Product("무릎 보호대", "35,000원", "네이버", defaultImg, "기타"));
        fullProductList.add(new Product("스포츠 헤어밴드", "8,000원", "옥션", defaultImg, "기타"));
        fullProductList.add(new Product("손목 아대", "7,000원", "빅터몰", defaultImg, "기타"));
        fullProductList.add(new Product("스포츠 물병 1L", "12,000원", "11번가", defaultImg, "기타"));
    }

    // 카테고리에 따라 리스트를 걸러내는 함수
    private void filterList(String category) {
        ArrayList<Product> filteredList = new ArrayList<>();

        if (category.equals("전체")) {
            filteredList.addAll(fullProductList);
        } else {
            for (Product item : fullProductList) {
                if (item.getCategory().equals(category)) {
                    filteredList.add(item);
                }
            }
        }
        // 어댑터에 바뀐 리스트 전달
        adapter.updateList(filteredList);
    }
}