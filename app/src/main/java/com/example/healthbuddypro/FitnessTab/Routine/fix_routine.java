package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fix_routine extends AppCompatActivity {

    private EditText editRoutineTitle;
    private ListView listViewFixRoutineList;
    private Button btnSave, btn_routineReset;
    private ImageButton backButton;

    private RoutineDayAdapter routineDayAdapter;
    private List<RoutineDay> routineDayList = new ArrayList<>();
    private int routineId;

    private static final String TAG = "fix_routine";

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
        setContentView(R.layout.activity_fix_routine);

        btn_routineReset = findViewById(R.id.btn_routineReset);
        editRoutineTitle = findViewById(R.id.edit_routineTitle);
        listViewFixRoutineList = findViewById(R.id.listview_fixRoutinelist);
        btnSave = findViewById(R.id.btn_save);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> finish());

        routineId = getRoutineId();

        routineDayAdapter = new RoutineDayAdapter(this, routineDayList, this);
        listViewFixRoutineList.setAdapter(routineDayAdapter);

        fetchRoutineDetails();

        btnSave.setOnClickListener(view -> {
            saveRoutineChanges();
            Intent intent = new Intent(fix_routine.this, MainActivity.class);
            intent.putExtra("newTabIndex", 0);
            intent.putExtra("routine_updated", true);
            startActivity(intent);
            finish();
        });

        // 루틴 리셋 버튼
        btn_routineReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fix_routine.this, make_routine01.class);
                startActivity(intent);
            }
        });
    }

    private int getRoutineId() {
        SharedPreferences userPreferences = getSharedPreferences("localID", Context.MODE_PRIVATE);
        int userId = userPreferences.getInt("userId", -1);

        if (userId != -1) {
            SharedPreferences routinePreferences = getSharedPreferences("routineId" + userId + "_prefs", Context.MODE_PRIVATE);
            return routinePreferences.getInt("routineId", -1);
        } else {
            Log.e(TAG, "User ID not found in local storage.");
            return -1;
        }
    }

    private void fetchRoutineDetails() {
        ApiService apiService = RetrofitClient.getApiService();
        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

        for (String day : daysOfWeek) {
            Call<RoutineDetailsResponse> call = apiService.getRoutineDetails(routineId, day);
            call.enqueue(new Callback<RoutineDetailsResponse>() {
                @Override
                public void onResponse(Call<RoutineDetailsResponse> call, Response<RoutineDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                        RoutineDetailsResponse.Data data = response.body().getData();
                        List<RoutineDetailsResponse.Workout> workouts = data.getWorkouts();
                        routineDayList.add(new RoutineDay(day, workouts));
                        routineDayAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Failed to load routine details for " + day);
                    }
                }

                @Override
                public void onFailure(Call<RoutineDetailsResponse> call, Throwable t) {
                    Toast.makeText(fix_routine.this, "Error fetching details for " + day, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void saveRoutineChanges() {
        String updatedTitle = editRoutineTitle.getText().toString().trim();
        List<RoutineFixRequest.DayRoutine> dayRoutineList = new ArrayList<>();

        for (RoutineDay routineDay : routineDayList) {
            List<RoutineFixRequest.WorkoutRoutine> workoutRoutines = new ArrayList<>();
            for (RoutineDetailsResponse.Workout workout : routineDay.getWorkouts()) {
                String workoutEnglish = exerciseMapping.getOrDefault(workout.getWorkout(), workout.getWorkout());
                workoutRoutines.add(new RoutineFixRequest.WorkoutRoutine(workoutEnglish, workout.getCounts(), workout.getRep()));
            }
            dayRoutineList.add(new RoutineFixRequest.DayRoutine(routineDay.getDay(), workoutRoutines));
        }

        RoutineFixRequest routineRequest = new RoutineFixRequest(updatedTitle, dayRoutineList);



        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.updateRoutine(routineId, routineRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(fix_routine.this, "루틴 수정 완료", Toast.LENGTH_SHORT).show();
                    // 최종 데이터셋을 로그로 출력
                    Log.d(TAG, "Sending RoutineFixRequest: " + routineRequest.toString());
                } else {
                    Toast.makeText(fix_routine.this, "루틴 수정 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(fix_routine.this, "루틴 수정 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 101) { // EditRoutine_AddExercise에서 돌아온 경우
                ArrayList<Exercise> selectedExercises = data.getParcelableArrayListExtra("selectedExercisesEditRoutine");
                String day = data.getStringExtra("day");

                // 받아온 selectedExercises와 day 값을 로그로 확인
                Log.d(TAG, "Received selectedExercises: " + (selectedExercises != null ? selectedExercises.size() : "null"));
                Log.d(TAG, "Received day: " + day);

                if (selectedExercises != null && day != null) {
                    RoutineDay routineDay = null;
                    for (RoutineDay rd : routineDayList) {
                        if (rd.getDay().equals(day)) {
                            routineDay = rd;
                            break;
                        }
                    }

                    if (routineDay == null) {
                        routineDay = new RoutineDay(day, new ArrayList<>());
                        routineDayList.add(routineDay);
                    }

                    // 운동 항목 추가 및 로그 출력
                    for (Exercise exercise : selectedExercises) {
                        Log.d(TAG, "Added Exercise: " + exercise.getName() + ", Sets: " + exercise.getSetCount() + ", Reps: " + exercise.getReps());
                        RoutineDetailsResponse.Workout workout = new RoutineDetailsResponse.Workout(
                                exercise.getName(), exercise.getSetCount(), exercise.getReps()
                        );
                        routineDay.getWorkouts().add(workout);
                    }

                    routineDayAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "No exercises received from EditRoutine_AddExercise or day is null.");
                }
            }
        }
    }
}
