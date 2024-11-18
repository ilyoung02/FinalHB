package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

public class RoutineRequest {
    private int teamId;
    private String title;
    private List<DayRoutine> days;

    public RoutineRequest(int teamId, String title, List<DayRoutine> days) {
        this.teamId = teamId;
        this.title = title;
        this.days = days;
    }

    public static class DayRoutine {
        private String day;
        private List<Workout> workouts;

        public DayRoutine(String day, List<Workout> workouts) {
            this.day = day;
            this.workouts = workouts;
        }
    }

    public static class Workout {
        private String workout;
        private int counts;
        private int rep;

        public Workout(String workout, int counts, int rep) {
            this.workout = workout;
            this.counts = counts;
            this.rep = rep;
        }

        // getter 메서드 추가
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

}
