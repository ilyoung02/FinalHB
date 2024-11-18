package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthbuddypro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RoutineCheckAdapter extends BaseAdapter {

    private Context context;
    private List<RoutineDetailsResponse.Workout> workouts;
    private List<List<CheckBox>> workoutCheckBoxes = new ArrayList<>();
    private List<Boolean> isExpandedList; // 항목 확장 여부를 저장하는 리스트

    // memo : 한글 운동 이름을 영어로 매핑하는 해시맵
    private final HashMap<String, String> exerciseMapping = new HashMap<String, String>() {{
        // 등
        put("랫 풀 다운", "LAT_PULLDOWN");
        put("바벨 로우", "BARBELL_ROW");
        put("덤벨 로우", "DUMBBELL_ROW");
        put("데드리프트", "DEADLIFT");
        put("시티드 로우", "SEATED_ROW");
        put("풀업", "PULL_UP");
        put("케이블 로우", "CABLE_ROW");
        put("티 바 로우", "T_BAR_ROW");

        // 가슴
        put("벤치 프레스", "BENCH_PRESS");
        put("인클라인 벤치 프레스", "INCLINE_BENCH_PRESS");
        put("디클라인 벤치 프레스", "DECLINE_BENCH_PRESS");
        put("덤벨 플라이", "DUMBBELL_FLY");
        put("체스트 프레스", "CHEST_PRESS");
        put("펙덱 플라이", "PEC_DECK_FLY");
        put("푸쉬업", "PUSH_UP");
        put("케이블 크로스오버", "CABLE_CROSSOVER");

        // 하체
        put("스쿼트", "SQUAT");
        put("레그 프레스", "LEG_PRESS");
        put("런지", "LUNGE");
        put("레그 컬", "LEG_CURL");
        put("레그 익스텐션", "LEG_EXTENSION");
        put("카프 레이즈", "CALF_RAISE");
        put("힙 쓰러스트", "HIP_THRUST");
        put("스티프 레그 데드리프트", "STIFF_LEG_DEADLIFT");

        // 유산소
        put("러닝머신", "TREADMILL");
        put("자전거", "BICYCLE");
        put("스텝퍼", "STEPPER");
        put("로잉머신", "ROWING_MACHINE");
        put("점핑잭", "JUMPING_JACK");
        put("버피", "BURPEE");
        put("마운틴 클라이머", "MOUNTAIN_CLIMBER");
        put("사이클", "CYCLE");

        // 어깨
        put("숄더 프레스", "SHOULDER_PRESS");
        put("사이드 레터럴 레이즈", "SIDE_LATERAL_RAISE");
        put("프론트 레이즈", "FRONT_RAISE");
        put("리어 델트 플라이", "REAR_DELT_FLY");
        put("업라이트 로우", "UPRIGHT_ROW");
        put("덤벨 숄더 프레스", "DUMBBELL_SHOULDER_PRESS");
        put("케이블 레이즈", "CABLE_RAISE");
        put("아놀드 프레스", "ARNOLD_PRESS");

        // 이두
        put("바벨 컬", "BARBELL_CURL");
        put("덤벨 컬", "DUMBBELL_CURL");
        put("해머 컬", "HAMMER_CURL");
        put("프리처 컬", "PREACHER_CURL");
        put("컨센트레이션 컬", "CONCENTRATION_CURL");
        put("케이블 컬", "CABLE_CURL");
        put("머신 컬", "MACHINE_CURL");
        put("크로스 바디 해머 컬", "CROSS_BODY_HAMMER_CURL");

        // 삼두
        put("트라이셉스 익스텐션", "TRICEPS_EXTENSION");
        put("덤벨 킥백", "DUMBBELL_KICKBACK");
        put("케이블 푸쉬다운", "CABLE_PUSH_DOWN");
        put("프렌치 프레스", "FRENCH_PRESS");
        put("스컬 크러셔", "SKULL_CRUSHER");
        put("오버헤드 익스텐션", "OVERHEAD_EXTENSION");
        put("딥스", "DIPS");
        put("트라이앵글 푸쉬업", "TRIANGLE_PUSH_UP");

        // 복근
        put("크런치", "CRUNCH");
        put("레그레이즈", "LEG_RAISE");
        put("플랭크", "PLANK");
        put("싯업", "SIT_UP");
        put("러시안 트위스트", "RUSSIAN_TWIST");
        put("바이시클 크런치", "BICYCLE_CRUNCH");
        put("힐터치", "HEEL_TOUCH");
        put("하이 플랭크", "HIGH_PLANK");
    }};

    public RoutineCheckAdapter(Context context, List<RoutineDetailsResponse.Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
        this.isExpandedList = new ArrayList<>();

        // 모든 항목의 초기 확장 상태는 false로 설정
        for (int i = 0; i < workouts.size(); i++) {
            isExpandedList.add(false);
        }
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //memo : 세트별 체크 동적 표시하는 메소드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
        convertView = LayoutInflater.from(context).inflate(R.layout.activity_routine_check_exercise_item, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
    } else {
        holder = (ViewHolder) convertView.getTag();
    }

    RoutineDetailsResponse.Workout workout = workouts.get(position);

    // 운동명 및 세트 수 정보 표시
    holder.exerciseName.setText(workout.getWorkout());
    holder.setInfo.setText(workout.getCounts() + "세트 x " + workout.getRep() + "회");

    // 세트별 체크 항목 초기화
    holder.setsContainer.removeAllViews();
    List<CheckBox> checkBoxes = new ArrayList<>();

    // 클릭 시 세트별 체크 항목 표시 상태 토글
    holder.exerciseName.setOnClickListener(v -> {
        isExpandedList.set(position, !isExpandedList.get(position));
        notifyDataSetChanged(); // 뷰 갱신
    });

    // 세트별 체크 항목 표시
    if (isExpandedList.get(position)) {
        for (int i = 1; i <= workout.getCounts(); i++) {
            View customCheckboxView = LayoutInflater.from(context).inflate(R.layout.activity_custom_checkbox_item, holder.setsContainer, false);
            TextView customCheckBoxText = customCheckboxView.findViewById(R.id.customCheckBoxText); // 세트와 회 정보를 표시하는 텍스트뷰
            CheckBox checkBox = customCheckboxView.findViewById(R.id.customCheckBox);

            // 세트와 회 정보 설정
            customCheckBoxText.setText(i + "세트 x " + workout.getRep() + "회");
            checkBox.setChecked(false);  // 초기 상태
            holder.setsContainer.addView(customCheckboxView);
            checkBoxes.add(checkBox);
        }
    }
        // 운동별 체크박스 리스트 추가
        if (workoutCheckBoxes.size() > position) {
            workoutCheckBoxes.set(position, checkBoxes);
        } else {
            workoutCheckBoxes.add(checkBoxes);
        }

    return convertView;
}


    // 세트별 체크 항목 데이터를 가져오는 메서드
    public List<RoutineCompletionRequest.WorkoutCompletion> getWorkoutCompletionData() {
        List<RoutineCompletionRequest.WorkoutCompletion> workoutCompletionData = new ArrayList<>();

        for (int i = 0; i < workouts.size(); i++) {
            RoutineDetailsResponse.Workout workout = workouts.get(i);
            String workoutName = exerciseMapping.getOrDefault(workout.getWorkout(), workout.getWorkout());
            List<RoutineCompletionRequest.SetCompletion> setCompletions = new ArrayList<>();

            // 각 세트별로 체크된 상태를 확인하여 SetCompletion 객체 생성
            List<CheckBox> checkBoxes = workoutCheckBoxes.get(i);
            for (int j = 0; j < checkBoxes.size(); j++) {
                CheckBox checkBox = checkBoxes.get(j);
                boolean completed = checkBox.isChecked();
                int num = j + 1; // 세트 번호
                int rep = workout.getRep(); // 세트당 반복 횟수

                // 세트별 상태 추가
                setCompletions.add(new RoutineCompletionRequest.SetCompletion(num, rep, completed));
            }

            // 운동 이름과 세트 정보 추가
            workoutCompletionData.add(new RoutineCompletionRequest.WorkoutCompletion(workoutName, setCompletions));
        }

        return workoutCompletionData;
    }



    static class ViewHolder {
        TextView exerciseName, setInfo;
        LinearLayout setsContainer;

        ViewHolder(View view) {
            exerciseName = view.findViewById(R.id.exerciseName);
            setInfo = view.findViewById(R.id.setInfo);
            setsContainer = view.findViewById(R.id.setsContainer);
        }
    }
}
