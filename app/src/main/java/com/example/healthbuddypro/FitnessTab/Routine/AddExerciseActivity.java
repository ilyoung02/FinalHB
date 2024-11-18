package com.example.healthbuddypro.FitnessTab.Routine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.example.healthbuddypro.FitnessTab.Routine.WorkoutResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExerciseActivity extends Activity {

    private ListView categoryListView, exerciseListView;
    private LinearLayout selectedItemsLayout;
    private Button completeButton, closeButton;
    private ArrayList<Exercise> selectedExercises;
    private String selectedExerciseName;
    private Map<String, List<String>> exerciseMap;
    private static final String BASE_URL = "http://165.229.89.154:8080/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        categoryListView = findViewById(R.id.categoryListView);
        exerciseListView = findViewById(R.id.exerciseListView);
        selectedItemsLayout = findViewById(R.id.selectedItemsLayout);
        completeButton = findViewById(R.id.completeButton);
        closeButton = findViewById(R.id.closeButton);
        selectedExercises = new ArrayList<>();

        // 서버에서 운동 데이터를 가져옴
        fetchWorkoutsFromServer();

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra("selectedExercises", selectedExercises);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        // 닫기 버튼
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 서버에서 운동 카테고리와 운동 목록을 가져오는 함수
    private void fetchWorkoutsFromServer() {
        ApiService service = RetrofitClient.getApiService();
        Call<WorkoutResponse> call = service.getWorkouts();

        call.enqueue(new Callback<WorkoutResponse>() {
            @Override
            public void onResponse(Call<WorkoutResponse> call, Response<WorkoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupWorkouts(response.body().getData());
                } else {
                    Toast.makeText(AddExerciseActivity.this, "운동 목록을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WorkoutResponse> call, Throwable t) {
                Toast.makeText(AddExerciseActivity.this, "서버 통신 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AddExerciseActivity", t.getMessage());
            }
        });
    }

    // 운동 부위 매핑
    private Map<String, String> bodyPartMap = new HashMap<String, String>() {{
        put("CHEST", "가슴");
        put("CORE", "복근");
        put("LOWER_BODY", "하체");
        put("SHOULDERS", "어깨");
        put("CARDIO", "유산소");
        put("BICEPS", "이두");
        put("BACK", "등");
        put("TRICEPS", "삼두");
    }};

    // 운동 목록을 화면에 설정하는 함수
    private void setupWorkouts(List<WorkoutResponse.BodyPart> bodyParts) {
        exerciseMap = new HashMap<>();

        // 서버에서 받은 운동 데이터를 저장
        for (WorkoutResponse.BodyPart bodyPart : bodyParts) {
            // 영어 카테고리를 한글로 매핑
            String translatedBodyPart = bodyPartMap.get(bodyPart.getBodyPart());
            exerciseMap.put(translatedBodyPart, bodyPart.getWorkouts());
        }

        // 카테고리 목록을 설정
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(exerciseMap.keySet()));
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                List<String> exercises = exerciseMap.get(selectedCategory);
                ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(AddExerciseActivity.this, android.R.layout.simple_list_item_1, exercises);
                exerciseListView.setAdapter(exerciseAdapter);
            }
        });

        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedExerciseName = (String) parent.getItemAtPosition(position);
                showInputDialog();
            }
        });
    }


    // 세트 횟수 선택 팝업
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(selectedExerciseName + " 설정");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(15, 15, 15, 15);

        final EditText setCountInput = new EditText(this);
        setCountInput.setHint("세트 수");
        setCountInput.setInputType(InputType.TYPE_CLASS_NUMBER); // 정수 입력 설정
        layout.addView(setCountInput);

        final EditText repsInput = new EditText(this);
        repsInput.setHint("반복 횟수");
        repsInput.setInputType(InputType.TYPE_CLASS_NUMBER); // 정수 입력 설정
        layout.addView(repsInput);

        builder.setView(layout);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String setCountStr = setCountInput.getText().toString();
                String repsStr = repsInput.getText().toString();

                if (!setCountStr.isEmpty() && !repsStr.isEmpty()) {
                    int setCount = Integer.parseInt(setCountStr); // 문자열을 int로 변환
                    int reps = Integer.parseInt(repsStr); // 문자열을 int로 변환
                    Exercise exercise = new Exercise(selectedExerciseName, setCount, reps);
                    selectedExercises.add(exercise);
                    displaySelectedExercise(exercise);
                }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // 삭제버튼 추가 후 -> 오류나면 밑에 전 저번 돌리기
    private void displaySelectedExercise(Exercise exercise) {
        // 선택된 운동을 포함하는 레이아웃 생성
        LinearLayout exerciseLayout = new LinearLayout(this);
        exerciseLayout.setOrientation(LinearLayout.HORIZONTAL);
        exerciseLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // 운동 정보를 표시하는 TextView 생성
        TextView textView = new TextView(this);
        textView.setText(exercise.getName() + " - " + exercise.getSetCount() + "세트 x " + exercise.getReps() + "회");
        textView.setTextSize(14);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // TextView가 여유 공간을 채우도록 weight 설정
        ));

        // 삭제 버튼 생성
        Button deleteButton = new Button(this);
        deleteButton.setText("X");
        deleteButton.setTextSize(14);
        deleteButton.setBackground(null); // 버튼 배경 투명
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // 삭제 버튼 클릭 이벤트
        deleteButton.setOnClickListener(v -> {
            // 선택된 운동 항목 삭제
            selectedItemsLayout.removeView(exerciseLayout);

            // 데이터 리스트에서도 항목 제거
            selectedExercises.remove(exercise);
            Toast.makeText(this, exercise.getName() + " 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        });

        // 레이아웃에 TextView와 삭제 버튼 추가
        exerciseLayout.addView(textView);
        exerciseLayout.addView(deleteButton);

        // 최상위 레이아웃에 추가
        selectedItemsLayout.addView(exerciseLayout);
    }

    // 삭제 버튼 수정 전 메소드임
    // 선택된 운동을 화면에 표시하는 함수
//    private void displaySelectedExercise(Exercise exercise) {
//        TextView textView = new TextView(this);
//        textView.setText(exercise.getName() + " - " + exercise.getSetCount() + "세트 x " + exercise.getReps() + "회");
//
//        // 텍스트 크기와 스타일 설정
//        textView.setTextSize(14);
//        textView.setTypeface(null, Typeface.BOLD);
//
//        selectedItemsLayout.addView(textView);
//    }
}