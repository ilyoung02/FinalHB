package com.example.healthbuddypro.FitnessTab.Routine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class item_routinefix extends AppCompatActivity {

    private Button btn_addExercise;
    private LinearLayout container;
    private static final String TAG = "item_routinefix";

    // 요일별 운동 데이터 관리
    private Map<String, List<RoutineDetailsResponse.Workout>> dayWorkoutMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_routinefix);

        container = findViewById(R.id.workoutContainer);
        btn_addExercise = findViewById(R.id.btn_addExercise);

        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        for (String day : daysOfWeek) {
            fetchRoutineDetails(day);
        }

        btn_addExercise.setOnClickListener(view -> {
            Intent intent = new Intent(item_routinefix.this, EditRoutine_AddExercise.class);
            startActivity(intent);
        });
    }

    private void fetchRoutineDetails(String day) {
        ApiService apiService = RetrofitClient.getApiService();
        int routineId = getRoutineId();

        Call<RoutineDetailsResponse> call = apiService.getRoutineDetails(routineId, day);
        call.enqueue(new Callback<RoutineDetailsResponse>() {
            @Override
            public void onResponse(Call<RoutineDetailsResponse> call, Response<RoutineDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    RoutineDetailsResponse.Data data = response.body().getData();
                    dayWorkoutMap.put(day, data.getWorkouts());
                    addDayItem(day, data.getWorkouts());
                } else {
                    Log.e(TAG, "Failed to load routine for day: " + day);
                }
            }

            @Override
            public void onFailure(Call<RoutineDetailsResponse> call, Throwable t) {
                Toast.makeText(item_routinefix.this, "Error loading routine for " + day, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDayItem(String day, List<RoutineDetailsResponse.Workout> workouts) {
        View dayItemView = LayoutInflater.from(this).inflate(R.layout.activity_item_routinefix, container, false);

        TextView routineDate = dayItemView.findViewById(R.id.routineDate);
        LinearLayout workoutContainer = dayItemView.findViewById(R.id.workoutContainer);
        ImageButton btn_dateEdit = dayItemView.findViewById(R.id.btn_dateEdit);

        routineDate.setText(convertDayToKorean(day));

        for (RoutineDetailsResponse.Workout workout : workouts) {
            addWorkoutItem(workoutContainer, workouts, workout);
        }

        btn_dateEdit.setOnClickListener(view -> showDayEditDialog(day, routineDate));

        container.addView(dayItemView);
    }

    private void showDayEditDialog(String currentDay, TextView routineDate) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_dialog_edit_day, null);
        TextView currentDayTextView = dialogView.findViewById(R.id.tv_current_day);
        Spinner daySpinner = dialogView.findViewById(R.id.spinner_edit_day);

        currentDayTextView.setText(convertDayToKorean(currentDay));

        String[] days = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        final String[] selectedDay = {currentDay};
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay[0] = convertDayToEnglish(days[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        new AlertDialog.Builder(this)
                .setTitle("요일 정보 수정")
                .setView(dialogView)
                .setPositiveButton("확인", (dialog, which) -> {
                    String newDay = selectedDay[0];
                    if (!currentDay.equals(newDay)) {
                        dayWorkoutMap.put(newDay, dayWorkoutMap.remove(currentDay));
                        routineDate.setText(convertDayToKorean(newDay));
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }


    private void addWorkoutItem(LinearLayout workoutContainer, List<RoutineDetailsResponse.Workout> workouts, RoutineDetailsResponse.Workout workout) {
        View workoutItemView = LayoutInflater.from(this).inflate(R.layout.item_workout, workoutContainer, false);

        TextView workoutInfo = workoutItemView.findViewById(R.id.workoutInfo);
        Button deleteButton = workoutItemView.findViewById(R.id.deleteButton);

        workoutInfo.setText(workout.getWorkout() + " - " + workout.getRep() + "회 x " + workout.getCounts() + "세트");

        // 삭제 버튼 클릭으로 삭제된 항목은 전송 시 제외
        deleteButton.setOnClickListener(v -> {
            workoutContainer.removeView(workoutItemView);
            workouts.remove(workout); // 전달된 workouts 리스트에서 해당 workout을 삭제
        });

        workoutContainer.addView(workoutItemView);
    }


    private String convertDayToKorean(String day) {
        switch (day.toUpperCase(Locale.ROOT)) {
            case "MON": return "월요일";
            case "TUE": return "화요일";
            case "WED": return "수요일";
            case "THU": return "목요일";
            case "FRI": return "금요일";
            case "SAT": return "토요일";
            case "SUN": return "일요일";
            default: return "";
        }
    }

    private String convertDayToEnglish(String koreanDay) {
        switch (koreanDay) {
            case "월요일": return "MON";
            case "화요일": return "TUE";
            case "수요일": return "WED";
            case "목요일": return "THU";
            case "금요일": return "FRI";
            case "토요일": return "SAT";
            case "일요일": return "SUN";
            default: return "";
        }
    }

    private int getUserIdFromStorage() {
        SharedPreferences preferences = getSharedPreferences("localID", Context.MODE_PRIVATE);
        return preferences.getInt("userId", -1);
    }

    private int getRoutineId() {
        int userId = getUserIdFromStorage();
        if (userId != -1) {
            SharedPreferences routinePreferences = getSharedPreferences("routineId" + userId + "_prefs", Context.MODE_PRIVATE);
            return routinePreferences.getInt("routineId", -1);
        }
        Log.e(TAG, "User ID not found or routine ID retrieval failed.");
        return -1;
    }
}
