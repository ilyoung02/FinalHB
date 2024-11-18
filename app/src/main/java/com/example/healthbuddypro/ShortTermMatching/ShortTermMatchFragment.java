package com.example.healthbuddypro.ShortTermMatching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthbuddypro.Matching.MatchFragment;
import com.example.healthbuddypro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortTermMatchFragment extends Fragment implements WritePostFragment.OnPostSubmitListener {

    private RecyclerView dayRecyclerView;
    private RecyclerView shortTermMatchList;
    private MatchListAdapter matchListAdapter;
    private DayOfWeekAdapter dayAdapter;
    private Map<String, List<ShortMatchPost>> matchData;  // 날짜별 매칭 데이터를 저장하기 위한 맵

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_short_term_match, container, false);

        // 버튼 초기화
        Button btn1To1Matching = view.findViewById(R.id.btn_1to1_matching);
        Button btnShortTermMatching = view.findViewById(R.id.btn_short_term_matching);
        Button btnWritePost = view.findViewById(R.id.btn_write_post); // 글쓰기 버튼

        // ShortTermMatchFragment로 넘어왔을 때, 단기 매칭 버튼을 기본 선택 상태로 설정
        btnShortTermMatching.setSelected(true);

        // 1:1 매칭 버튼 클릭 리스너
        btn1To1Matching.setOnClickListener(v -> {
            btn1To1Matching.setSelected(true);
            btnShortTermMatching.setSelected(false);

            // MatchFragment로 전환
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.fragment_container, new MatchFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // 글쓰기 버튼 클릭 시 WritePostFragment로 전환
        btnWritePost.setOnClickListener(v -> {
            WritePostFragment writePostFragment = new WritePostFragment();
            writePostFragment.setOnPostSubmitListener(ShortTermMatchFragment.this);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, writePostFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // 요일 선택 RecyclerView와 매칭 리스트 RecyclerView 초기화
        dayRecyclerView = view.findViewById(R.id.day_of_week_recycler_view);
        shortTermMatchList = view.findViewById(R.id.short_term_match_list);

        setupDays();  // 요일 선택 RecyclerView 설정
        setupMatchList();  // 매칭 목록 설정

        return view;
    }

    private void setupDays() {
        List<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("월");
        daysOfWeek.add("화");
        daysOfWeek.add("수");
        daysOfWeek.add("목");
        daysOfWeek.add("금");
        daysOfWeek.add("토");
        daysOfWeek.add("일");

        dayAdapter = new DayOfWeekAdapter(daysOfWeek, this::onDaySelected);
        dayRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dayRecyclerView.setAdapter(dayAdapter);
    }

    private void setupMatchList() {
        shortTermMatchList.setLayoutManager(new LinearLayoutManager(getContext()));

        // 매칭 데이터 초기화
        matchData = new HashMap<>();

        // 예시 데이터 삽입 (MatchPost 객체 사용)
        addMatchData("월", new ShortMatchPost(1, 2, "홍길동", "열심히 할 사람!!🔥", "어깨", "중리동", "성별무관"));
        addMatchData("화", new ShortMatchPost(3, 4, "방일영", "잘 부탁드려요!", "가슴", "서구", "남성전용"));

        // 기본 데이터를 리스트에 설정
        matchListAdapter = new MatchListAdapter(new ArrayList<>());
        shortTermMatchList.setAdapter(matchListAdapter);

        // 첫 번째 데이터 보여주기 (월요일)
        filterMatchList("월");
    }

    private void addMatchData(String day, ShortMatchPost match) {
        if (!matchData.containsKey(day)) {
            matchData.put(day, new ArrayList<>());
        }
        matchData.get(day).add(match);
    }

    private void filterMatchList(String day) {
        List<ShortMatchPost> filteredList = matchData.getOrDefault(day, new ArrayList<>());
        matchListAdapter.updateList(filteredList);
    }

    private void onDaySelected(String day) {
        filterMatchList(day);  // 선택된 요일에 맞는 매칭 목록 표시
    }

    @Override
    public void onPostSubmitted(String title, String health, String content, String location, String category) {
        // 글 작성 완료 시 호출되는 메서드
        int senderId = 1; // 임시로 사용자 ID 설정
        int receiverId = 2; // 임시로 매칭 대상자 ID 설정

        // 새로운 매칭 데이터를 현재 선택된 요일에 추가
        addMatchData("월", new ShortMatchPost(senderId, receiverId, title, health, content, location, category));

        // 매칭 목록을 갱신
        filterMatchList("월"); // 여기서 "월"을 선택된 요일로 대체할 수 있음
    }
}
