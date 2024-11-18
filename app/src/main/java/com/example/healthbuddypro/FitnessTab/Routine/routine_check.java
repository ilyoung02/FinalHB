// 수정된 routine_check.java 파일

package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.example.healthbuddypro.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class routine_check extends AppCompatActivity {

    private ListView routineCheckListView;
    private TextView todayDate, workoutTimerView;
    private int routineId, userId;
    private RoutineCheckAdapter adapter;
    private Button btn_completeTodayWorkout;
    private ImageButton workoutTimer_startBtn, timer;

    // 타이머 관련 변수 선언
    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_check);

        timer = findViewById(R.id.timer);
        workoutTimerView = findViewById(R.id.workoutTimerView);
        workoutTimer_startBtn = findViewById(R.id.workoutTimer_startBtn);
        btn_completeTodayWorkout = findViewById(R.id.btn_completeTodayWorkout);
        routineCheckListView = findViewById(R.id.routineCheck_listview);
        todayDate = findViewById(R.id.todayDate);

        SharedPreferences prefs = getSharedPreferences("localID", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        routineId = getRoutineIdFromStorage(userId);

        setTodayDate();
        loadRoutineDetails();

        // 완료 버튼 클릭 시 운동 정보 전송
        btn_completeTodayWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWorkoutCompletion();
                Intent intent = new Intent(routine_check.this, complete_today_workout.class);
                startActivity(intent);
            }
        });

        // 휴식 타이머 -> 들어갓다가 시간끝나면 자동 복귀됨
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(routine_check.this, timer.class);
                startActivity(intent);
            }
        });

        // 타이머 버튼 클릭 시 시작 및 일시정지 기능 구현
        workoutTimer_startBtn.setOnClickListener(view -> {
            if (!isRunning) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                workoutTimer_startBtn.setImageResource(R.drawable.icon_pause); // 일시정지 아이콘으로 변경
                isRunning = true;
            } else {
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimerThread);
                workoutTimer_startBtn.setImageResource(R.drawable.icon_playbtn); // 재생 아이콘으로 변경
                isRunning = false;
            }
        });
    }

    // 타이머를 업데이트하는 스레드
    private Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            mins = mins % 60;

            workoutTimerView.setText(String.format("%02d:%02d:%02d", hours, mins, secs));
            handler.postDelayed(this, 1000);
        }
    };

    // 루틴 아이디를 내부 저장소에서 가져오는 메서드
    private int getRoutineIdFromStorage(int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("routineId" + userId + "_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("routineId", -1);
    }

    // 오늘 날짜 표시 메서드
    private void setTodayDate() {
        String currentDate = new SimpleDateFormat("E MM.dd", Locale.getDefault()).format(new Date());
        todayDate.setText(currentDate);
    }

    // 오늘 요일을 넣어서 오늘 요일에 해당하는 루틴 운동 정보 로딩
    private void loadRoutineDetails() {
        String day = new SimpleDateFormat("E", Locale.getDefault()).format(new Date()).toUpperCase();
        ApiService service = RetrofitClient.getApiService();
        Call<RoutineDetailsResponse> call = service.getRoutineDetails(routineId, day);

        call.enqueue(new Callback<RoutineDetailsResponse>() {
            @Override
            public void onResponse(Call<RoutineDetailsResponse> call, Response<RoutineDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RoutineDetailsResponse.Data data = response.body().getData();
                    List<RoutineDetailsResponse.Workout> workouts = data.getWorkouts();
                    setupWorkoutList(workouts);
                } else {
                    Toast.makeText(routine_check.this, "루틴 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoutineDetailsResponse> call, Throwable t) {
                Toast.makeText(routine_check.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 운동 목록을 설정하는 메서드
    private void setupWorkoutList(List<RoutineDetailsResponse.Workout> workouts) {
        adapter = new RoutineCheckAdapter(this, workouts);
        routineCheckListView.setAdapter(adapter);
    }

    //memo : 운동 완료 데이터 전송 메서드 -> RoutineCheckAdapter에서 체크 상태를 받아와서 전송함
    private void sendWorkoutCompletion() {
        // RoutineCheckAdapter에서 수집한 세트별 운동 완료 상태 데이터 가져오기
        List<RoutineCompletionRequest.WorkoutCompletion> workoutCompletions = adapter.getWorkoutCompletionData();

        // 로그 추가
        for (RoutineCompletionRequest.WorkoutCompletion completion : workoutCompletions) {
            Log.d("routine_check", "Workout: " + completion.getWorkout());
            for (RoutineCompletionRequest.SetCompletion set : completion.getSets()) {
                Log.d("routine_check", "Set: " + set.getNum() + ", Rep: " + set.getRep() + ", Completed: " + set.isCompleted());
            }
        }

        // 현재 날짜와 시간
        String currentDate = new SimpleDateFormat("yyyy.MM.dd 12:00", Locale.getDefault()).format(new Date());
        RoutineCompletionRequest completionRequest = new RoutineCompletionRequest(userId, currentDate, workoutCompletions);

        // API 호출
        Call<RoutineCompletionResponse> call = RetrofitClient.getApiService().completeRoutine(routineId, getTodayDay(), completionRequest);

        call.enqueue(new Callback<RoutineCompletionResponse>() {
            @Override
            public void onResponse(Call<RoutineCompletionResponse> call, Response<RoutineCompletionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RoutineCompletionResponse.Data responseData = response.body().getData();
                    //memo : 루틴 응답 오면 내부로 응답 여부 저장
                    saveRoutineCompletionStatus();
                    Toast.makeText(routine_check.this, "운동 완료! 포인트: " + responseData.getPoints() + ", D-Day: D+" + responseData.getDDay(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(routine_check.this, complete_today_workout.class);
                    intent.putExtra("dDay", responseData.getDDay());
                    intent.putExtra("points", responseData.getPoints());
                    startActivity(intent);
                } else {
                    Toast.makeText(routine_check.this, "운동 완료 기록 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoutineCompletionResponse> call, Throwable t) {
                Toast.makeText(routine_check.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // memo : 루틴 수행 완료 여부를 운동완료 버튼 눌렀을때 오는 response 의 여부로 요일이 바뀌는 것을 기준으로 값을 초기화해서 그날 운동을 했는지 여부 파악
    private void saveRoutineCompletionStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRoutineComplete_" + userId, true);
        editor.putString("routineCompleteDay_" + userId, getTodayDay());  // 현재 요일 저장
        editor.apply();
    }


    // 오늘 요일 반환 메서드
    private String getTodayDay() {
        return new SimpleDateFormat("E", Locale.getDefault()).format(new Date()).toUpperCase();
    }
}
