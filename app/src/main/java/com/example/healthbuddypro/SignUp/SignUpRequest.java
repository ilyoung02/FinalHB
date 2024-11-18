package com.example.healthbuddypro.SignUp;

import android.util.Log;

public class SignUpRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String birthDate;
    private String gender;
    private String region;

    public SignUpRequest(String username, String password, String confirmPassword, String nickname, String birthDate, String gender, String region) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.region = region;
    }

    public String getUsername() {
        Log.d("getUsername", this.username);
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        Log.d("getPassword", this.password);
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNickname() {
        Log.d("getNickname", this.nickname);
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthDate()
    {
        Log.d("getBirthDate", this.birthDate);
        return birthDate;

    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        Log.d("getGender", this.gender);
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        Log.d("getRegion", this.region);
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
