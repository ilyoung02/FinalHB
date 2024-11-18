package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;
import com.example.healthbuddypro.temp_DG.set_routine;

import java.util.ArrayList;

public class make_routine03 extends AppCompatActivity {

    private Button nextStepButton;
    private ImageButton backButton;
    private String routineTitle;

    //todo : 루틴 제목, 선택한 요일, 요일에 들어간 각 운동정보들 서버로 넘겨주는 작업 해야함 - done

    // 선택한 요일을 저장할 배열
    private boolean[] selectedDays = new boolean[7]; // 0: 월, 1: 화, 2: 수, 3: 목, 4: 금, 5: 토, 6: 일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_routine03);

        // 데이터 수신
        Intent intent = getIntent();
        routineTitle = intent.getStringExtra("routineTitle");

        // UI 요소 초기화
        nextStepButton = findViewById(R.id.nextBtn03);
        backButton = findViewById(R.id.backButton);

        // 요일 버튼 설정
        setupDayButtons();

        // 다음 단계 버튼 클릭 시
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSelectedDaysWithIntent(); // 선택한 요일과 루틴 제목 전달
            }
        });

        // 뒤로 가기 버튼 클릭 시
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 이전 Activity로 이동
            }
        });
    }

    // 요일 버튼 설정 메소드
    private void setupDayButtons() {
        // 요일 이미지 버튼 초기화 및 클릭 리스너 설정
        int[] dayButtonIds = {
                R.id.mon, R.id.tue, R.id.wed, R.id.thu,
                R.id.fri, R.id.sat, R.id.sun
        };

        for (int i = 0; i < dayButtonIds.length; i++) {
            final int index = i; // final 변수를 사용하여 클릭 리스너에서 접근 가능하게
            ImageButton dayButton = findViewById(dayButtonIds[i]);
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedDays[index] = !selectedDays[index]; // 선택 상태 토글
                    updateDayButtonAppearance(dayButton, selectedDays[index]);
                }
            });
        }
    }

    // 선택된 요일에 따른 버튼 색상 업데이트
    private void updateDayButtonAppearance(ImageButton dayButton, boolean isSelected) {
        if (isSelected) {
            dayButton.setBackgroundResource(R.drawable.date_selected_background); // 선택된 색상
        } else {
            dayButton.setBackgroundColor(getResources().getColor(android.R.color.transparent)); // 기본 색상
        }
    }

    // 선택된 요일을 한국어 형식으로 변환하는 메소드
    private String getDayName(int index) {
        switch (index) {
            case 0: return "월요일";
            case 1: return "화요일";
            case 2: return "수요일";
            case 3: return "목요일";
            case 4: return "금요일";
            case 5: return "토요일";
            case 6: return "일요일";
            default: return "";
        }
    }

    // 선택된 요일을 Intent로 넘기는 메소드
    private void sendSelectedDaysWithIntent() {
        ArrayList<String> selectedDayNames = new ArrayList<>();
        for (int i = 0; i < selectedDays.length; i++) {
            if (selectedDays[i]) {
                selectedDayNames.add(getDayName(i)); // 선택된 요일만 추가
            }
        }
        Intent intent = new Intent(make_routine03.this, set_routine_exercise.class);
        intent.putExtra("routineTitle", routineTitle);
        intent.putStringArrayListExtra("selectedDays", selectedDayNames); // 한국어 요일을 전달
        startActivity(intent);
    }
}
