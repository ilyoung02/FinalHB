package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.R;

public class complete_today_workout extends AppCompatActivity {

    private Button completeButton;
    private TextView DdayView, rankPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_complete_today_workout);

        completeButton = findViewById(R.id.completeButton);
        DdayView = findViewById(R.id.DdayView);
        rankPoints = findViewById(R.id.rankPoints);

        // routine_check 에서 완료버튼 눌렀을때 전달되도록 한 값들 받아와줌
        Intent intent = getIntent();
        int dDay = intent.getIntExtra("dDay", 0); // 디데이 값
        int points = intent.getIntExtra("points", 0); // 포인트 값

        // TextView에 데이터 설정
        DdayView.setText("D + " + dDay);
        rankPoints.setText(points + " points");

        // 완료 버튼 클릭 시 메인 화면으로 이동
        // todo : 오늘 하루 루틴 다하고 나면 나오는 프래그먼트를 또 추가해줘야할듯 -> 추가함
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(complete_today_workout.this, MainActivity.class);
                intent.putExtra("newTabIndex", 0);  // newTabIndex 값을 0으로 설정하여 전달
                startActivity(intent);
                finish(); // 액티비티 한번 정리 -> 뒤로 못감
            }
        });
    }
}
