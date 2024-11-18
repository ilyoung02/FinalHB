package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

public class RoutineDetailsResponse {
    private int code;
    private String message;
    private Data data; // `data`를 Data 객체로 변경하여 title과 workouts를 포함

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private String title;
        private List<Workout> workouts;

        public String getTitle() {
            return title;
        }

        public List<Workout> getWorkouts() {
            return workouts;
        }
    }

    public static class Workout {
        private String workout;
        private int counts;
        private int rep;
        private boolean isExpanded = false;  // 세트 펼침 상태
        private List<Set> sets;

        public Workout(String workout, int counts, int rep) {
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

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

        public List<Set> getSets() {
            return sets;
        }

        public static class Set {
            private int num;
            private int rep;
            private boolean completed;

            public Set(int num, int rep, boolean completed) {
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

            public void setCompleted(boolean completed) {
                this.completed = completed;
            }
        }
    }
}
