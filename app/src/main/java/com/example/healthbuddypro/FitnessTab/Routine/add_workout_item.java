package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;

public class add_workout_item extends AppCompatActivity {

    private TextView addWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_workout_item);

        addWorkout = findViewById(R.id.addWorkout);

        // 운동추가 텍스트뷰 클릭 -> 운동추가 화면
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_workout_item.this, AddExerciseActivity.class);
                startActivity(intent);
            }
        });

    }
}