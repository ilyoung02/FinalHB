package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoRoutineFragment extends Fragment {

    private Button btnRoutineList;
    private TextView teamName, todayDate, routineTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_routine, container, false);

        loadBuddyNickname();
        loadRoutineTitle(getRoutineIdFromStorage()); // 루틴 제목 가져옴

        btnRoutineList = view.findViewById(R.id.btn_routineList);
        teamName = view.findViewById(R.id.teamName);
        todayDate = view.findViewById(R.id.todayDate);
        routineTitle = view.findViewById(R.id.routineTitle);
        setTodayDate(todayDate);

        //memo : 테스트한다고 루틴 목록으로 연결해둠 -> 루틴 수정으로 다시 바꾸기 - done
        // 루틴 목록 버튼
        btnRoutineList.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), fix_routine.class);
            startActivity(intent);
        });

        return view;
    }

    // 오늘 날짜 설정 (요일 축약 표시)
    private void setTodayDate(TextView textView) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd.EEEE", Locale.KOREA);
        String currentDate = dateFormat.format(new Date());

        // 요일 축약
        String shortDate = currentDate.replaceAll("월요일", "월")
                .replaceAll("화요일", "화")
                .replaceAll("수요일", "수")
                .replaceAll("목요일", "목")
                .replaceAll("금요일", "금")
                .replaceAll("토요일", "토")
                .replaceAll("일요일", "일");

        textView.setText(shortDate);
    }

    // 내부 저장소에서 userId를 가져오는 함수
    private int getUserIdFromStorage() {
        Context context = getActivity();
        if (context != null) {
            return context.getSharedPreferences("localID", Context.MODE_PRIVATE)
                    .getInt("userId", -1);
        }
        return -1;
    }

    // 내부 저장소에서 routineId를 가져오는 함수
    private int getRoutineIdFromStorage() {
        Context context = getActivity();
        if (context != null) {
            int userId = getUserIdFromStorage(); // userId 가져오기
            if (userId != -1) { // 유효한 userId가 있을 경우
                return context.getSharedPreferences("routineId" + userId + "_prefs", Context.MODE_PRIVATE)
                        .getInt("routineId", -1);
            } else {
                Log.e("RoutineFragment", "루틴 시작에서 루틴ID 조회 시 ID 값 조회 오류");
            }
        }
        return -1; // userId 또는 routineId가 없으면 -1 반환
    }

    // 내부 저장소에서 teamId와 userId를 가져와 헬스버디의 이름을 불러오는 메서드
    private void loadBuddyNickname() {
        Context context = getActivity();
        if (context == null) return;

        // 내부 저장소에서 userId를 먼저 가져옴
        SharedPreferences userPreferences = context.getSharedPreferences("localID", Context.MODE_PRIVATE);
        int userId = userPreferences.getInt("userId", -1);

        if (userId == -1) {
            Log.e("RoutineStartFragment", "User ID not found in local storage.");
            return;
        }

        // userId로 구성된 SharedPreferences 파일에서 teamId를 가져옴
        SharedPreferences teamPreferences = context.getSharedPreferences("user_" + userId + "_prefs", Context.MODE_PRIVATE);
        int teamId = teamPreferences.getInt("teamId", -1);

        if (teamId == -1) {
            Log.e("RoutineStartFragment", "Team ID not found for user: " + userId);
            return;
        }

        // 여기서 이제 서버에서 불러옴
        ApiService service = RetrofitClient.getApiService();
        Call<MatchedUserResponse> call = service.getMatchedUser(teamId, userId);

        call.enqueue(new Callback<MatchedUserResponse>() {
            @Override
            public void onResponse(Call<MatchedUserResponse> call, Response<MatchedUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String buddyNickname = response.body().getData().getNickname();
                    teamName.setText(buddyNickname + " 헬스버디"); // 헬스버디 이름을 표시
                } else {
                    Toast.makeText(getActivity(), "헬스버디 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MatchedUserResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 루틴 제목 가져오는 메소드임
    // MON부터 SUN까지 요일별로 순차적으로 요청을 보내고, title을 받으면 요청 중단
    private void loadRoutineTitle(int routineId) {
        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        ApiService service = RetrofitClient.getApiService();

        // 성공적으로 title을 받았는지 확인할 플래그
        final boolean[] isTitleFetched = {false};

        for (String day : daysOfWeek) {
            if (isTitleFetched[0]) break; // title을 받았다면 추가 요청 중단

            Call<RoutineDetailsResponse> call = service.getRoutineDetails(routineId, day);
            call.enqueue(new Callback<RoutineDetailsResponse>() {
                @Override
                public void onResponse(Call<RoutineDetailsResponse> call, Response<RoutineDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        RoutineDetailsResponse.Data data = response.body().getData();
                        String title = data.getTitle(); // 루틴의 title 가져오기

                        if (title != null && !title.isEmpty()) {
                            // title이 있는 경우 TextView에 설정하고, 추가 요청을 막기 위해 플래그 설정
                            routineTitle.setText(title);
                            isTitleFetched[0] = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<RoutineDetailsResponse> call, Throwable t) {
                    Log.e("loadRoutineTitle", "네트워크 오류: " + t.getMessage());
                }
            });
        }
    }
}
