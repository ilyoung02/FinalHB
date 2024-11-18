package com.example.healthbuddypro.Profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.healthbuddypro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkoutListActivity extends Activity {

    private ListView categoryListView, exerciseListView;
    private LinearLayout selectedItemsLayout;
    private Button completeButton, closeButton;
    private ArrayList<String> selectedExercises;
    private Map<String, String[]> exerciseMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        categoryListView = findViewById(R.id.categoryListView);
        exerciseListView = findViewById(R.id.exerciseListView);
        selectedItemsLayout = findViewById(R.id.selectedItemsLayout);
        completeButton = findViewById(R.id.completeButton);
        closeButton = findViewById(R.id.closeButton);
        selectedExercises = new ArrayList<>();

        // 운동 카테고리와 해당 운동 데이터
        String[] categories = {"가슴", "등", "하체", "유산소", "어깨", "이두", "삼두", "복근"};
        exerciseMap = new HashMap<>();
        exerciseMap.put("가슴", new String[]{"벤치 프레스", "인클라인 벤치 프레스", "디클라인 벤치 프레스", "덤벨 플라이", "체스트 프레스", "펙덱 플라이", "푸쉬업", "케이블 크로스오버"});
        exerciseMap.put("등", new String[]{"랫 풀 다운", "바벨 로우", "덤벨 로우", "데드리프트", "시티드 로우", "풀업", "케이블 로우", "티 바 로우"});
        exerciseMap.put("하체", new String[]{"스쿼트", "레그 프레스", "런지", "레그 컬", "레그 익스텐션", "카프 레이즈", "힙 쓰러스트", "스티프 레그 데드리프트"});
        exerciseMap.put("유산소", new String[]{"러닝머신", "자전거", "스텝퍼", "로잉머신", "점핑잭", "버피", "마운틴 클라이머", "사이클"});
        exerciseMap.put("어깨", new String[]{"숄더 프레스", "사이드 레터럴 레이즈", "프론트 레이즈", "리어 델트 플라이", "업라이트 로우", "덤벨 숄더 프레스", "케이블 레이즈", "아놀드 프레스"});
        exerciseMap.put("이두", new String[]{"바벨 컬", "덤벨 컬", "해머 컬", "프리처 컬", "컨센트레이션 컬", "케이블 컬", "머신 컬", "크로스 바디 해머 컬"});
        exerciseMap.put("삼두", new String[]{"트라이셉스 익스텐션", "덤벨 킥백", "케이블 푸쉬다운", "프렌치 프레스", "스컬 크러셔", "오버헤드 익스텐션", "딥스", "트라이앵글 푸쉬업"});
        exerciseMap.put("복근", new String[]{"크런치", "레그레이즈", "플랭크", "싯업", "러시안 트위스트", "바이시클 크런치", "힐터치", "하이 플랭크"});

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                String[] exercises = exerciseMap.get(selectedCategory);
                ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(WorkoutListActivity.this, android.R.layout.simple_list_item_1, exercises);
                exerciseListView.setAdapter(exerciseAdapter);
            }
        });

        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedExercise = (String) parent.getItemAtPosition(position);
                if (!selectedExercises.contains(selectedExercise)) {
                    selectedExercises.add(selectedExercise);
                    displaySelectedExercise(selectedExercise);
                }
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("selectedExercises", selectedExercises);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displaySelectedExercise(String exercise) {
        TextView textView = new TextView(this);
        textView.setText(exercise);
        selectedItemsLayout.addView(textView);
    }
}
