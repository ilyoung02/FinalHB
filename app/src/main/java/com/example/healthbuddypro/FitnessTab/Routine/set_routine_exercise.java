package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class set_routine_exercise extends AppCompatActivity {

    private LinearLayout exerciseContainer;
    private String routineTitle;
    private ArrayList<String> selectedDays;
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private Button btn_makeRoutineComplete;
    private ImageButton backButton;

    // 요일별 운동 리스트를 저장하는 Map
    private Map<String, List<RoutineRequest.Workout>> workoutsByDay = new HashMap<>();

    // 한글 운동 이름을 영어로 매핑하는 해시맵
    private final HashMap<String, String> exerciseMapping = new HashMap<String, String>() {{
        // 등
        put("랫 풀 다운", "LAT_PULLDOWN");
        put("바벨 로우", "BARBELL_ROW");
        put("덤벨 로우", "DUMBBELL_ROW");
        put("데드리프트", "DEADLIFT");
        put("시티드 로우", "SEATED_ROW");
        put("풀업", "PULL_UP");
        put("케이블 로우", "CABLE_ROW");
        put("티 바 로우", "T_BAR_ROW");

        // 가슴
        put("벤치 프레스", "BENCH_PRESS");
        put("인클라인 벤치 프레스", "INCLINE_BENCH_PRESS");
        put("디클라인 벤치 프레스", "DECLINE_BENCH_PRESS");
        put("덤벨 플라이", "DUMBBELL_FLY");
        put("체스트 프레스", "CHEST_PRESS");
        put("펙덱 플라이", "PEC_DECK_FLY");
        put("푸쉬업", "PUSH_UP");
        put("케이블 크로스오버", "CABLE_CROSSOVER");

        // 하체
        put("스쿼트", "SQUAT");
        put("레그 프레스", "LEG_PRESS");
        put("런지", "LUNGE");
        put("레그 컬", "LEG_CURL");
        put("레그 익스텐션", "LEG_EXTENSION");
        put("카프 레이즈", "CALF_RAISE");
        put("힙 쓰러스트", "HIP_THRUST");
        put("스티프 레그 데드리프트", "STIFF_LEG_DEADLIFT");

        // 유산소
        put("러닝머신", "TREADMILL");
        put("자전거", "BICYCLE");
        put("스텝퍼", "STEPPER");
        put("로잉머신", "ROWING_MACHINE");
        put("점핑잭", "JUMPING_JACK");
        put("버피", "BURPEE");
        put("마운틴 클라이머", "MOUNTAIN_CLIMBER");
        put("사이클", "CYCLE");

        // 어깨
        put("숄더 프레스", "SHOULDER_PRESS");
        put("사이드 레터럴 레이즈", "SIDE_LATERAL_RAISE");
        put("프론트 레이즈", "FRONT_RAISE");
        put("리어 델트 플라이", "REAR_DELT_FLY");
        put("업라이트 로우", "UPRIGHT_ROW");
        put("덤벨 숄더 프레스", "DUMBBELL_SHOULDER_PRESS");
        put("케이블 레이즈", "CABLE_RAISE");
        put("아놀드 프레스", "ARNOLD_PRESS");

        // 이두
        put("바벨 컬", "BARBELL_CURL");
        put("덤벨 컬", "DUMBBELL_CURL");
        put("해머 컬", "HAMMER_CURL");
        put("프리처 컬", "PREACHER_CURL");
        put("컨센트레이션 컬", "CONCENTRATION_CURL");
        put("케이블 컬", "CABLE_CURL");
        put("머신 컬", "MACHINE_CURL");
        put("크로스 바디 해머 컬", "CROSS_BODY_HAMMER_CURL");

        // 삼두
        put("트라이셉스 익스텐션", "TRICEPS_EXTENSION");
        put("덤벨 킥백", "DUMBBELL_KICKBACK");
        put("케이블 푸쉬다운", "CABLE_PUSH_DOWN");
        put("프렌치 프레스", "FRENCH_PRESS");
        put("스컬 크러셔", "SKULL_CRUSHER");
        put("오버헤드 익스텐션", "OVERHEAD_EXTENSION");
        put("딥스", "DIPS");
        put("트라이앵글 푸쉬업", "TRIANGLE_PUSH_UP");

        // 복근
        put("크런치", "CRUNCH");
        put("레그레이즈", "LEG_RAISE");
        put("플랭크", "PLANK");
        put("싯업", "SIT_UP");
        put("러시안 트위스트", "RUSSIAN_TWIST");
        put("바이시클 크런치", "BICYCLE_CRUNCH");
        put("힐터치", "HEEL_TOUCH");
        put("하이 플랭크", "HIGH_PLANK");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_routine_exercise);

        exerciseContainer = findViewById(R.id.exerciseContainer);
        backButton = findViewById(R.id.backButton);
        btn_makeRoutineComplete = findViewById(R.id.btn_makeRoutineComplete);

        // 이전 액티비티에서 데이터 가져오기
        Intent intent = getIntent();
        routineTitle = intent.getStringExtra("routineTitle");
        selectedDays = intent.getStringArrayListExtra("selectedDays");

        // 선택된 요일에 맞게 동적 레이아웃 추가
        if (selectedDays != null && !selectedDays.isEmpty()) {
            for (String day : selectedDays) {
                addDayLayout(day);
                workoutsByDay.put(day, new ArrayList<>()); // 요일에 맞는 빈 리스트 추가
            }
        }

        // 뒤로가기 버튼
        backButton.setOnClickListener(view -> finish());

        // 생성 완료 버튼
        btn_makeRoutineComplete.setOnClickListener(view -> sendRoutineToServer());
    }

    private void addDayLayout(final String day) {
        View dayView = getLayoutInflater().inflate(R.layout.activity_add_workout_item, null);

        TextView dayText = dayView.findViewById(R.id.selectedDate);
        dayText.setText(day);

        // 운동 추가 버튼
        dayView.findViewById(R.id.addWorkout).setOnClickListener(v -> {
            Intent intent = new Intent(set_routine_exercise.this, AddExerciseActivity.class);
            startActivityForResult(intent, selectedDays.indexOf(day));
        });

        exerciseContainer.addView(dayView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<Exercise> addedExercises = data.getParcelableArrayListExtra("selectedExercises");
            if (addedExercises != null && !addedExercises.isEmpty()) {
                String day = selectedDays.get(requestCode);
                List<RoutineRequest.Workout> workouts = workoutsByDay.get(day);
                if (workouts == null) workouts = new ArrayList<>();

                for (Exercise exercise : addedExercises) {
                    workouts.add(new RoutineRequest.Workout(exercise.getName(), exercise.getSetCount(), exercise.getReps()));
                }
                workoutsByDay.put(day, workouts);

                LinearLayout layoutForDay = (LinearLayout) exerciseContainer.getChildAt(requestCode);
                for (Exercise exercise : addedExercises) {
                    addExerciseToDay(layoutForDay, exercise);
                }
            }
        }
    }

    // 운동추가하면 동적생성 되는 부분 -> 원래 잘되던 부분 복구할때 쓰자
//    private void addExerciseToDay(LinearLayout dayLayout, Exercise exercise) {
//        TextView workoutInfo = new TextView(this);
//        workoutInfo.setText(exercise.getName() + " - " + exercise.getReps() + "회 x " + exercise.getSetCount() + "세트");
//        dayLayout.addView(workoutInfo);
//    }

    // 운동 항목을 동적으로 추가하는 메서드 - 삭제 항목 추가
    private void addExerciseToDay(LinearLayout dayLayout, Exercise exercise) {
        // 각 운동 항목을 담을 레이아웃 생성
        LinearLayout exerciseEntry = new LinearLayout(this);
        exerciseEntry.setOrientation(LinearLayout.HORIZONTAL);  // 가로 방향 정렬
        exerciseEntry.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        exerciseEntry.setPadding(0, 10, 0, 10); // 상하 여백을 추가

        // 운동 정보 텍스트뷰 생성
        TextView workoutInfo = new TextView(this);
        workoutInfo.setText(exercise.getName() + " " + exercise.getReps() + "회 x " + exercise.getSetCount() + "세트");
        workoutInfo.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
        ));  // 가로 비율을 1로 설정해 공간을 채우도록 함
        workoutInfo.setTextSize(14); // 텍스트 크기 조정
        workoutInfo.setTypeface(null, Typeface.BOLD); // 텍스트 스타일을 bold로 설정
        workoutInfo.setTextColor(Color.parseColor("#000000")); // 텍스트 색상을 블랙으로 설정

        // 삭제 버튼 생성
        Button deleteButton = new Button(this);
        deleteButton.setText("X");  // 버튼에 "X" 텍스트 설정
        deleteButton.setTextSize(16);  // 텍스트 크기 설정
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        deleteButton.setBackgroundColor(Color.TRANSPARENT);  // 배경 투명 설정
        deleteButton.setOnClickListener(v -> dayLayout.removeView(exerciseEntry));  // 버튼 클릭 시 해당 운동 항목 삭제

        // 운동 정보와 삭제 버튼을 레이아웃에 추가
        exerciseEntry.addView(workoutInfo);
        exerciseEntry.addView(deleteButton);

        // 각 운동 항목을 dayLayout에 추가
        dayLayout.addView(exerciseEntry);

        // 항목 하단에 구분선 추가
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));
        divider.setBackgroundColor(Color.GRAY); // 구분선 색상 설정
        dayLayout.addView(divider);
    }

    // 요일 변환 맵핑 메서드
    private String mapKoreanDayToEnglish(String koreanDay) {
        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("월요일", "MON");
        dayMap.put("화요일", "TUE");
        dayMap.put("수요일", "WED");
        dayMap.put("목요일", "THU");
        dayMap.put("금요일", "FRI");
        dayMap.put("토요일", "SAT");
        dayMap.put("일요일", "SUN");

        return dayMap.getOrDefault(koreanDay, koreanDay); // 매핑되지 않으면 원래 값을 반환
    }

    private int getTeamId() {
        // 현재 사용자(userId)를 먼저 가져옴
        SharedPreferences userPreferences = getSharedPreferences("localID", MODE_PRIVATE);
        int userId = userPreferences.getInt("userId", -1);

        // userId가 유효하지 않으면 -1 반환 (teamId가 없음을 나타냄)
        if (userId == -1) {
            Log.e("set_routine_exercise", "User ID not found.");
            return -1;
        }

        // 해당 userId로 teamId를 저장한 SharedPreferences 파일에서 teamId를 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences("user_" + userId + "_prefs", MODE_PRIVATE);
        int teamId = sharedPreferences.getInt("teamId", -1); // teamId가 없으면 -1 반환
        Log.d("teamId 조회@@@@@", "teamId : " + Integer.toString(teamId));

        // teamId 반환
        if (teamId == -1) {
            Log.e("set_routine_exercise", "Team ID not found for userId: " + userId);
        }

        return teamId;
    }


    // 서버로 설정한 루틴 정보들 보내주는 부분
    private void sendRoutineToServer() {
        List<RoutineRequest.DayRoutine> days = new ArrayList<>();
        for (String day : selectedDays) {
            String englishDay = mapKoreanDayToEnglish(day); // 요일을 영어 형식으로 변환
            List<RoutineRequest.Workout> workouts = new ArrayList<>();

            List<RoutineRequest.Workout> originalWorkouts = workoutsByDay.get(day);
            if (originalWorkouts != null) {
                for (RoutineRequest.Workout workout : originalWorkouts) {
                    // workout 이름을 매핑된 영어 이름으로 변환
                    String englishWorkoutName = exerciseMapping.getOrDefault(workout.getWorkout(), workout.getWorkout());
                    workouts.add(new RoutineRequest.Workout(englishWorkoutName, workout.getCounts(), workout.getRep()));
                }
            }

            if (!workouts.isEmpty()) {
                days.add(new RoutineRequest.DayRoutine(englishDay, workouts));
            }
        }

        RoutineRequest routineRequest = new RoutineRequest(getTeamId(), routineTitle, days);
        Call<RoutineCreationResponse> call = RetrofitClient.getApiService().createRoutine(routineRequest);

        call.enqueue(new Callback<RoutineCreationResponse>() {
            @Override
            public void onResponse(Call<RoutineCreationResponse> call, Response<RoutineCreationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int routineId = response.body().getData().getRoutineId();
                    saveRoutineIdToSharedPreferences(routineId);
                    Toast.makeText(set_routine_exercise.this, "루틴이 생성되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(set_routine_exercise.this, make_routine04.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(set_routine_exercise.this, "루틴 생성 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoutineCreationResponse> call, Throwable t) {
                Toast.makeText(set_routine_exercise.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // routineId를 내부 저장소에 저장하는 메서드 -> teamId랑 키값이 다르게 들어가줘야한다
    // routineId를 특정 userId의 SharedPreferences에 저장하는 메서드
    private void saveRoutineIdToSharedPreferences(int routineId) {
        // 현재 사용자(userId)를 가져옴
        SharedPreferences userPreferences = getSharedPreferences("localID", MODE_PRIVATE);
        int userId = userPreferences.getInt("userId", -1);

        // userId가 유효한 경우에만 routineId 저장
        if (userId != -1) {
            SharedPreferences sharedPreferences = getSharedPreferences("routineId" + userId + "_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("routineId", routineId);
            editor.apply();
        } else { // userId가 유효하지 않을때 로그 띄우기
            Log.e("set_routine_exercise", "User ID not found. Cannot save routineId.");
        }
    }

}
