package com.example.healthbuddypro.FitnessTab.Routine;

public class TeamStatusResponse {
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
        private String teamStatus;

        public String getTeamStatus() {
            return teamStatus;
        }

        public void setTeamStatus(String teamStatus) {
            this.teamStatus = teamStatus;
        }
    }
}
