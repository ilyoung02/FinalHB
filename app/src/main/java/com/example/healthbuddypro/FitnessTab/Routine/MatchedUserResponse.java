// MatchedUserResponse.java
package com.example.healthbuddypro.FitnessTab.Routine;

public class MatchedUserResponse {
    private int code;
    private String message;
    private MatchedUserData data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public MatchedUserData getData() {
        return data;
    }

    public static class MatchedUserData {
        private int userId;
        private String nickname;

        public int getUserId() {
            return userId;
        }

        public String getNickname() {
            return nickname;
        }
    }
}
