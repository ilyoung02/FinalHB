package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

// 운동 카테고리랑 종류 불러오는 클래스임

public class WorkoutResponse {
    private int httpStatusCode;
    private String responseMessage;
    private List<BodyPart> data;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public List<BodyPart> getData() {
        return data;
    }

    public static class BodyPart {
        private String bodyPart;
        private List<String> workouts;

        public String getBodyPart() {
            return bodyPart;
        }

        public List<String> getWorkouts() {
            return workouts;
        }
    }
}
