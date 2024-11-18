// RoutineFixRequest.java
package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

public class RoutineFixRequest {
    private String title;
    private List<DayRoutine> days;

    public RoutineFixRequest(String title, List<DayRoutine> days) {
        this.title = title;
        this.days = days;
    }

    public static class DayRoutine {
        private String day;
        private List<WorkoutRoutine> workouts;

        public DayRoutine(String day, List<WorkoutRoutine> workouts) {
            this.day = day;
            this.workouts = workouts;
        }
    }

    public static class WorkoutRoutine {
        private String workout;
        private int counts;
        private int rep;

        public WorkoutRoutine(String workout, int counts, int rep) {
            this.workout = workout;
            this.counts = counts;
            this.rep = rep;
        }
    }
}
