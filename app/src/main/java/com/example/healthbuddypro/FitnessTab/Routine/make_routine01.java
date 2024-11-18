package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.healthbuddypro.R;

public class make_routine01 extends AppCompatActivity {

    private EditText routineTitleEditText;
    private Button nextStepButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_routine01);

        // UI 요소 초기화
        routineTitleEditText = findViewById(R.id.routineTitleEditText);
        nextStepButton = findViewById(R.id.nextStepButton);
        backButton = findViewById(R.id.backButton);

        // 다음 단계 버튼 클릭 시
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 루틴 제목을 가져와 make_routine03.class로 넘겨주고 페이지 이동 -> 03 에서 다시 루틴제목 데이터를 넘겨줘야함 03에서 값 받는 코드 넣어야함
                String routineTitle = routineTitleEditText.getText().toString().trim();
                Intent intent = new Intent(make_routine01.this, make_routine03.class);
                intent.putExtra("routineTitle", routineTitle); // 루틴 제목 전달
                startActivity(intent);

                // make_routine03에서 set_routine_exercise.class로 루틴 제목을 전달할 부분 구현
//                Intent exerciseIntent = new Intent(make_routine03.this, set_routine_exercise.class);
//                exerciseIntent.putExtra("routineTitle", routineTitle);
//                startActivity(exerciseIntent);
            }
        });

        // 뒤로 가기 버튼
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 페이지로 이동
                finish();
            }
        });
    }
}
