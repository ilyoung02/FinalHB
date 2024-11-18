package com.example.healthbuddypro.FitnessTab.Routine;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;

public class routine_check_exercise_item extends AppCompatActivity {

    private TextView exerciseName, setInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routine_check_exercise_item);

    }
}