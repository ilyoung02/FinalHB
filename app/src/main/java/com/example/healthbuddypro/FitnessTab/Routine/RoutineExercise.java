package com.example.healthbuddypro.FitnessTab.Routine;

public class RoutineExercise {
    private String workout;
    private int counts;
    private int rep;

    // 생성자 추가
    public RoutineExercise(String workout, int counts, int rep) {
        this.workout = workout;
        this.counts = counts;
        this.rep = rep;
    }

    public String getWorkout() {
        return workout;
    }

    public int getCounts() {
        return counts;
    }

    public int getRep() {
        return rep;
    }
}

