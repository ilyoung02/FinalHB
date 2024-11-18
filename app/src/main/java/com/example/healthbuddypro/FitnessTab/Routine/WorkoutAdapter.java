package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthbuddypro.R;

import java.util.List;


public class WorkoutAdapter extends BaseAdapter {

    private Context context;
    private List<Exercise> exerciseList;
    private OnDeleteClickListener onDeleteClickListener;

    public WorkoutAdapter(Context context, List<Exercise> exerciseList, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public int getCount() {
        return exerciseList.size();
    }

    @Override
    public Object getItem(int position) {
        return exerciseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // 기존의 TextView를 사용하지 않고 LinearLayout으로 운동 정보를 추가하는 구조로 변경
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_add_workout_item, parent, false);
        }

        // 운동 정보가 표시될 레이아웃 가져오기
        LinearLayout exerciseLayout = convertView.findViewById(R.id.exerciseLayout);

        // 운동 정보 세팅
        Exercise exercise = (Exercise) getItem(position);

        // 운동 이름과 세트/반복 정보를 동적으로 추가
        TextView workoutName = new TextView(context);
        workoutName.setText(exercise.getName());
        workoutName.setTextSize(16);  // 크기 설정
        workoutName.setPadding(10, 10, 10, 10);  // 여백 설정

        TextView workoutDetails = new TextView(context);
        workoutDetails.setText(exercise.getSetCount() + "세트 x " + exercise.getReps() + "회");
        workoutDetails.setTextSize(14);  // 크기 설정
        workoutDetails.setPadding(10, 10, 10, 10);  // 여백 설정

        // 레이아웃에 운동 정보 추가
        exerciseLayout.addView(workoutName);
        exerciseLayout.addView(workoutDetails);

        // 삭제 버튼 이벤트 처리
        ImageButton deleteButton = convertView.findViewById(R.id.fix_deleteBtn); // todo : 이거 아이디 맞는지 확인??
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(position); // 삭제 인터페이스 호출
            }
        });

        return convertView;
    }

    // 삭제 인터페이스
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
