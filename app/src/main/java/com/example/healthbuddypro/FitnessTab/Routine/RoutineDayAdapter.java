package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.R;

import java.util.List;

public class RoutineDayAdapter extends ArrayAdapter<RoutineDay> {
    private LayoutInflater inflater;
    private final AppCompatActivity parentActivity; // AppCompatActivity로 변경

    public RoutineDayAdapter(Context context, List<RoutineDay> routineDays, AppCompatActivity parentActivity) {
        super(context, 0, routineDays);
        this.inflater = LayoutInflater.from(context);
        this.parentActivity = parentActivity; // 호출한 액티비티 인스턴스를 저장
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_item_routinefix, parent, false);
        }

        RoutineDay routineDay = getItem(position);

        TextView dayTitle = convertView.findViewById(R.id.routineDate);
        dayTitle.setText(routineDay.getDay());

        ImageButton editDayButton = convertView.findViewById(R.id.btn_dateEdit);
        editDayButton.setOnClickListener(v -> showDayEditDialog(dayTitle, routineDay));

        Button addWorkoutButton = convertView.findViewById(R.id.btn_addExercise);
        addWorkoutButton.setOnClickListener(v -> {
            // 운동 추가 화면으로 이동
            Intent intent = new Intent(getContext(), EditRoutine_AddExercise.class);
            intent.putExtra("day", routineDay.getDay());
            parentActivity.startActivityForResult(intent, 101); // memo : AddExercise 여기랑 requestCode, putParcelableArrayListExtra의 키값이 달라야함
        });

        LinearLayout workoutContainer = convertView.findViewById(R.id.workoutContainer);
        workoutContainer.removeAllViews();

        for (RoutineDetailsResponse.Workout workout : routineDay.getWorkouts()) {
            View workoutItem = inflater.inflate(R.layout.item_workout, workoutContainer, false);
            TextView workoutInfo = workoutItem.findViewById(R.id.workoutInfo);
            workoutInfo.setText(workout.getWorkout() + " - " + workout.getCounts() + "회 x " + workout.getRep() + "세트");

            ImageButton deleteButton = workoutItem.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(v -> {
                routineDay.getWorkouts().remove(workout);
                parentActivity.runOnUiThread(() -> ((fix_routine) parentActivity).saveRoutineChanges()); // 액티비티의 메서드 호출
                notifyDataSetChanged();
            });

            workoutContainer.addView(workoutItem);
        }

        return convertView;
    }

    private void showDayEditDialog(TextView dayTitle, RoutineDay routineDay) {
        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("요일 선택");
        builder.setItems(daysOfWeek, (dialog, which) -> {
            String selectedDay = daysOfWeek[which];
            dayTitle.setText(selectedDay);
            routineDay.setDay(selectedDay);
            ((fix_routine) parentActivity).saveRoutineChanges(); // 액티비티의 메서드 호출
        });
        builder.show();
    }
}
