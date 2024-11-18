package com.example.healthbuddypro.Matching;

// ProfileDetailActivity의 단일 프로필을 받기 위한 Response
public class ProfileResponse {
    private int code;
    private String message;
    private UserProfile data;

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

    public UserProfile getData() {
        return data;
    }
    public void setData(UserProfile data) {
        this.data = data;
    }
}
