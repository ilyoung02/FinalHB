package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

public class RoutineCompletionRequest {
    private int userId;
    private String date;
    private List<WorkoutCompletion> workouts;

    public RoutineCompletionRequest(int userId, String date, List<WorkoutCompletion> workouts) {
        this.userId = userId;
        this.date = date;
        this.workouts = workouts;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public List<WorkoutCompletion> getWorkouts() {
        return workouts;
    }

    public static class WorkoutCompletion {
        private String workout;
        private List<SetCompletion> sets;

        public WorkoutCompletion(String workout, List<SetCompletion> sets) {
            this.workout = workout;
            this.sets = sets;
        }

        public String getWorkout() {
            return workout;
        }

        public List<SetCompletion> getSets() {
            return sets;
        }
    }

    public static class SetCompletion {
        private int num;
        private int rep;
        private boolean completed;

        public SetCompletion(int num, int rep, boolean completed) {
            this.num = num;
            this.rep = rep;
            this.completed = completed;
        }

        public int getNum() {
            return num;
        }

        public int getRep() {
            return rep;
        }

        public boolean isCompleted() {
            return completed;
        }
    }
}
