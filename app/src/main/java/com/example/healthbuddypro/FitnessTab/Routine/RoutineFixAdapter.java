package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthbuddypro.R;

import java.util.List;
import java.util.Locale;

// memo : 루틴 수정할때 item 결합해주는 어댑터
public class RoutineFixAdapter extends BaseAdapter {

    private final Context context;
    private final List<RoutineDay> routineDays;

    public RoutineFixAdapter(Context context, List<RoutineDay> routineDays) {
        this.context = context;
        this.routineDays = routineDays;
    }

    @Override
    public int getCount() {
        return routineDays.size();
    }

    @Override
    public Object getItem(int position) {
        return routineDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_routinefix, parent, false);

        TextView routineDate = view.findViewById(R.id.routineDate);
        LinearLayout workoutContainer = view.findViewById(R.id.workoutContainer);

        RoutineDay day = routineDays.get(position);

        // 한국어 변환 오류뜨면 이거 쓰기
//        routineDate.setText(day.getDay());


        // 요일을 한국어로 변환하여 화면에 표시
        String translatedDay = getKoreanDay(day.getDay());
        routineDate.setText(translatedDay);


        for (RoutineDetailsResponse.Workout workout : day.getWorkouts()) {
            View workoutView = LayoutInflater.from(context).inflate(R.layout.item_workout, workoutContainer, false);

            TextView workoutInfo = workoutView.findViewById(R.id.workoutInfo);
            ImageButton deleteButton = workoutView.findViewById(R.id.deleteButton);

            workoutInfo.setText(workout.getWorkout() + " - " + workout.getRep() + "회 x " + workout.getCounts() + "세트");

            deleteButton.setOnClickListener(v -> workoutContainer.removeView(workoutView));
            workoutContainer.addView(workoutView);
        }

        return view;
    }

    // 요일 매핑 메서드
    private String getKoreanDay(String day) {
        switch (day.toLowerCase(Locale.ROOT)) {
            case "mon":
                return "월요일";
            case "tue":
                return "화요일";
            case "wed":
                return "수요일";
            case "thu":
                return "목요일";
            case "fri":
                return "금요일";
            case "sat":
                return "토요일";
            case "sun":
                return "일요일";
            default:
                return day; // 매핑되지 않은 경우 원본 반환
        }
    }


    public List<RoutineDay> getUpdatedDays() {
        return routineDays;
    }
}
