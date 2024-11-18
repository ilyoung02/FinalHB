package com.example.healthbuddypro.Matching.Chat;

public class MatchResponse {
    private int code;
    private String message;
    private MatchData data;

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

    public MatchData getData() {
        return data;
    }

    public void setData(MatchData data) {
        this.data = data;
    }
}
