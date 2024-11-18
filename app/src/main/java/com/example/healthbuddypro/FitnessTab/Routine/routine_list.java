package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthbuddypro.R;

import java.util.ArrayList;

public class routine_list extends AppCompatActivity {

    private ListView listView;
    private RoutineListAdapter adapter;
    private ArrayList<Routine> routineList;

    private Button btn_createRoutine;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routine_list);

        listView = findViewById(R.id.routineListView);
        btn_createRoutine = findViewById(R.id.btn_createRoutine);
        backbtn = findViewById(R.id.backbtn);

        // 뒤로가기
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //루틴 생성 버튼
        btn_createRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), make_routine01.class);
                startActivity(intent);
            }
        });


        routineList = new ArrayList<>();
        routineList.add(new Routine("홍길동 헬스버디", "다이어트 운동")); // 임의로 박아둔거임

        adapter = new RoutineListAdapter(this, R.layout.activity_routine_list_item, routineList);
        listView.setAdapter(adapter);


    }
}