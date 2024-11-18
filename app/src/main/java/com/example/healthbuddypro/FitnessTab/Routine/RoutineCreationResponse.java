package com.example.healthbuddypro.FitnessTab.Routine;

public class RoutineCreationResponse {
    private int code;
    private String message;
    private Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private int routineId;

        public int getRoutineId() {
            return routineId;
        }

        public void setRoutineId(int routineId) {
            this.routineId = routineId;
        }
    }
}
