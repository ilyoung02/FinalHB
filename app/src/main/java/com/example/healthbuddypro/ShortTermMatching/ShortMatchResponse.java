package com.example.healthbuddypro.ShortTermMatching;

public class ShortMatchResponse {
    private int code;
    private String message;
    private Data data;

    public static class Data {
        private int matchRequestId;
        private int teamId; // 매칭 수락 시 팀 ID도 포함될 수 있음

        public int getMatchRequestId() {
            return matchRequestId;
        }

        public int getTeamId() {
            return teamId;
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }
}
