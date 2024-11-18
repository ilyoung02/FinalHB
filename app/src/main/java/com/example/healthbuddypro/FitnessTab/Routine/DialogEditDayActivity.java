package com.example.healthbuddypro.FitnessTab.Routine;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;

public class DialogEditDayActivity extends AppCompatActivity {

    // memo : 루틴 수정에서 요일 수정 팝업임
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dialog_edit_day);

    }
}