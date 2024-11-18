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
import androidx.fragment.app.FragmentTransaction;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineStartFragment extends Fragment {

    private Button btnRoutineList;
    private Button btn_startRoutine;
    private TextView teamName, todayDate, routineTitle;
    private int routineId;


    // todo : 테스트 한다고 바로 routine_check 로 이동되게 해둠 -> 다시 원복 해놨음
    // memo : 서버에서 workout 받아오고 운동부위는 여기서 매핑해서 보여줌 -> 백이 자동 매핑되게 안해둠
    private final Map<String, String> workoutToBodyPartMap = new HashMap<String, String>() {{
        put("랫 풀 다운", "등");
        put("바벨 로우", "등");
        put("덤벨 로우", "등");
        put("데드리프트", "등");
        put("시티드 로우", "등");
        put("풀업", "등");
        put("케이블 로우", "등");
        put("티 바 로우", "등");

        put("벤치 프레스", "가슴");
        put("인클라인 벤치 프레스", "가슴");
        put("디클라인 벤치 프레스", "가슴");
        put("덤벨 플라이", "가슴");
        put("체스트 프레스", "가슴");
        put("펙덱 플라이", "가슴");
        put("푸쉬업", "가슴");
        put("케이블 크로스오버", "가슴");

        put("스쿼트", "하체");
        put("레그 프레스", "하체");
        put("런지", "하체");
        put("레그 컬", "하체");
        put("레그 익스텐션", "하체");
        put("카프 레이즈", "하체");
        put("힙 쓰러스트", "하체");
        put("스티프 레그 데드리프트", "하체");

        put("러닝머신", "유산소");
        put("자전거", "유산소");
        put("스텝퍼", "유산소");
        put("로잉머신", "유산소");
        put("점핑잭", "유산소");
        put("버피", "유산소");
        put("마운틴 클라이머", "유산소");
        put("사이클", "유산소");

        put("숄더 프레스", "어깨");
        put("사이드 레터럴 레이즈", "어깨");
        put("프론트 레이즈", "어깨");
        put("리어 델트 플라이", "어깨");
        put("업라이트 로우", "어깨");
        put("덤벨 숄더 프레스", "어깨");
        put("케이블 레이즈", "어깨");
        put("아놀드 프레스", "어깨");

        put("바벨 컬", "이두");
        put("덤벨 컬", "이두");
        put("해머 컬", "이두");
        put("프리처 컬", "이두");
        put("컨센트레이션 컬", "이두");
        put("케이블 컬", "이두");
        put("머신 컬", "이두");
        put("크로스 바디 해머 컬", "이두");

        put("트라이셉스 익스텐션", "삼두");
        put("덤벨 킥백", "삼두");
        put("케이블 푸쉬다운", "삼두");
        put("프렌치 프레스", "삼두");
        put("스컬 크러셔", "삼두");
        put("오버헤드 익스텐션", "삼두");
        put("딥스", "삼두");
        put("트라이앵글 푸쉬업", "삼두");

        put("크런치", "복근");
        put("레그레이즈", "복근");
        put("플랭크", "복근");
        put("싯업", "복근");
        put("러시안 트위스트", "복근");
        put("바이시클 크런치", "복근");
        put("힐터치", "복근");
        put("하이 플랭크", "복근");
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routinestart, container, false);

        routineId = getRoutineIdFromStorage(); // 루틴 아이디 불러오기

        btnRoutineList = view.findViewById(R.id.btn_routineList);
        btn_startRoutine = view.findViewById(R.id.btn_startRoutine);
        teamName = view.findViewById(R.id.teamName);
        todayDate = view.findViewById(R.id.todayDate);
        routineTitle = view.findViewById(R.id.routineTitle);

        setTodayDate(todayDate);
        loadTodayRoutine(routineId);
        loadBuddyNickname(); // 헬스버디 이름 로드
        loadRoutineTitle(getRoutineIdFromStorage()); // 루틴 제목 가져옴

        btn_startRoutine.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), check_location.class);
            startActivity(intent);
        });

        //memo : 루틴 목록 버튼 -> 루틴 수정으로 이동하도록 변경
        btnRoutineList.setOnClickListener(view12 -> {
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

    // 오늘 운동 정보를 불러오는 함수
    private void loadTodayRoutine(int routineId) {
        String day = new SimpleDateFormat("E", Locale.getDefault()).format(new Date()).toUpperCase();
        ApiService service = RetrofitClient.getApiService();
        Call<RoutineDetailsResponse> call = service.getRoutineDetails(routineId, day);

        call.enqueue(new Callback<RoutineDetailsResponse>() {
            @Override
            public void onResponse(Call<RoutineDetailsResponse> call, Response<RoutineDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RoutineDetailsResponse.Data data = response.body().getData();
                    List<RoutineDetailsResponse.Workout> workouts = data.getWorkouts(); // data에서 workouts 리스트 가져오기

                    if (workouts != null && !workouts.isEmpty()) {
                        // 오늘 요일의 루틴 데이터가 있을 경우 루틴 정보 표시
                        displayTodayRoutine(workouts);
                    } else {
                        // 오늘 요일에 해당하는 루틴이 없을 경우 NoRoutineFragment 표시
                        replaceFragment(new NoRoutineFragment());
                    }
                } else {
                    Toast.makeText(getActivity(), "오늘의 운동 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoutineDetailsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 오늘 요일에 해당하는 루틴 정보만 표시해주는 함수
    private void displayTodayRoutine(List<RoutineDetailsResponse.Workout> workouts) {
        View rootView = getView();
        if (rootView == null) return;

        ViewGroup todayRoutineList = rootView.findViewById(R.id.todayRoutineList);
        todayRoutineList.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        for (RoutineDetailsResponse.Workout workout : workouts) {
            View workoutView = inflater.inflate(R.layout.activity_item_today_routine, todayRoutineList, false);

            TextView bodyPartText = workoutView.findViewById(R.id.bodyPart);
            TextView workoutNameText = workoutView.findViewById(R.id.workoutName);

            // 운동명으로 운동 부위를 매핑해서 bodyPart 가져오기
            String bodyPart = workoutToBodyPartMap.getOrDefault(workout.getWorkout(), "운동 부위 없음");

            // 화면에 운동 부위와 운동명을 설정
            bodyPartText.setText(bodyPart);
            workoutNameText.setText(workout.getWorkout());

            // 해당 운동 정보를 todayRoutineList에 추가
            todayRoutineList.addView(workoutView);
        }
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


    // NoRoutineFragment를 표시하는 replaceFragment 메서드
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
