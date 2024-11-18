package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

public class RoutineResponse {
    private int teamId;
    private String title;
    private List<DayRoutine> days;
    private int dday;
    private List<RoutineExercise> data;


    public int getDday() {
        return dday;
    }

    public List<RoutineExercise> getData() {
        return data;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<DayRoutine> getDays() {
        return days;
    }

    public void setDays(List<DayRoutine> days) {
        this.days = days;
    }

    public static class DayRoutine {
        private String day;
        private List<Exercise> workouts;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public List<Exercise> getWorkouts() {
            return workouts;
        }

        public void setWorkouts(List<Exercise> workouts) {
            this.workouts = workouts;
        }
    }
}
