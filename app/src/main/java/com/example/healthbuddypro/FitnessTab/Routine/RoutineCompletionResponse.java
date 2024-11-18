package com.example.healthbuddypro.FitnessTab.Routine;

public class RoutineCompletionResponse {
    private int code;
    private String message;
    private Data data;

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
        private int points;
        private int dday;

        public int getPoints() {
            return points;
        }

        public int getDDay() {
            return dday;
        }
    }
}
