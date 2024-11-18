package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.R;

public class make_routine04 extends AppCompatActivity {

    private Button completeMakeRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_routine04);

        completeMakeRoutine = findViewById(R.id.completeMakeRoutine);


        // 그냥 메인으로 보내면 될듯 ? -> 메인으로 보내고 운동 카테고리로 갓을때 갱신되는지만 확인하자 -> 이때 메인 넘어갈때 운동이 선택되도록 함
        completeMakeRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(make_routine04.this, MainActivity.class);  // CurrentActivity를 현재 액티비티 이름으로 변경
                intent.putExtra("newTabIndex", 0);  // newTabIndex 값을 0으로 설정하여 전달
                startActivity(intent);
                finish();
            }
        });


    }
}