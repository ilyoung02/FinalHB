package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

// memo : 각 요일에 해당하는 운동 정보를 저장하는 데이터 클래스
public class RoutineDay {
    private String day;  // 요일 (예: "MON", "TUE" 등)
    private List<RoutineDetailsResponse.Workout> workouts;  // 해당 요일의 운동 목록

    public RoutineDay(String day, List<RoutineDetailsResponse.Workout> workouts) {
        this.day = day;
        this.workouts = workouts;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<RoutineDetailsResponse.Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<RoutineDetailsResponse.Workout> workouts) {
        this.workouts = workouts;
    }
}
