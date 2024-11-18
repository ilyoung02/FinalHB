package com.example.healthbuddypro.FitnessTab.Routine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;

import java.util.Calendar;

public class make_routine02 extends AppCompatActivity {

    //todo : 이건 안쓰는 페이지임 혹시 몰라 남겨둠
    private TextView startDateText, endDateText;
    private ImageButton startDateButton, endDateButton, backButton;
    private String selectedStartDate, selectedEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_routine02);

        startDateText = findViewById(R.id.routine_startDate);
        endDateText = findViewById(R.id.routine_endDate);
        startDateButton = findViewById(R.id.setRoutine_startDate);
        endDateButton = findViewById(R.id.setRoutine_endDate);
        backButton = findViewById(R.id.backButton);

        // 시작일 선택
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((date, year, month, day) -> {
                    selectedStartDate = String.format("%02d.%02d.%02d", year % 100, month + 1, day);
                    startDateText.setText(selectedStartDate);
                });
            }
        });

        // 종료일 선택
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog((date, year, month, day) -> {
                    selectedEndDate = String.format("%02d.%02d.%02d", year % 100, month + 1, day);
                    endDateText.setText(selectedEndDate);
                });
            }
        });

        // 다음 단계 버튼 클릭 시
        findViewById(R.id.nextBtn02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(make_routine02.this, make_routine03.class);
                intent.putExtra("startDate", selectedStartDate);
                intent.putExtra("endDate", selectedEndDate);
                startActivity(intent);
            }
        });

        // 뒤로가기 버튼 클릭 시 이전 페이지로 이동
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // 현재 액티비티를 종료하고 이전 화면으로 이동
            }
        });
    }

    // 날짜 선택 다이얼로그
    private void showDatePickerDialog(DatePickerCallback callback) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            callback.onDateSelected(view, year1, month1, day1);
        }, year, month, day);

        datePickerDialog.show();
    }

    private interface DatePickerCallback {
        void onDateSelected(DatePicker view, int year, int month, int day);
    }
}
