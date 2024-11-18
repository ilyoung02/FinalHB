package com.example.healthbuddypro.Matching;
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.List;

public class UserProfile implements Serializable {
    private int profileId;
    private int UserId;
    private String nickname;
    private String imageUrl;
    private String gender;
    private int age;
    private int workoutYears;  // 구력(운동 경력)
    private int likeCount;
    private List<String> favWorkouts;
    private String workoutGoal;
    private String region;
    private String bio;

    public UserProfile(String workoutGoal, String region, String bio, int profileId, int UserId, String nickname, String imageUrl, String gender, int age, int workoutYears, int likeCount, List<String> favWorkouts) {
        this.profileId = profileId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.gender = gender;
        this.age = age;
        this.workoutYears = workoutYears;
        this.likeCount = likeCount;
        this.UserId = UserId;
        this.workoutGoal = workoutGoal;
        this.region = region;
        this.bio = bio;
        this.favWorkouts = favWorkouts;
    }

    public int getUserId() {
        return UserId;
    }
    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    // 프로필 ID
    public int getProfileId() {
        return profileId;
    }
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    // 닉네임
    public String getNickName() {
        return nickname;
    }
    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    // 이미지
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 성별
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    // 나이
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public int getWorkoutYears() {
        return workoutYears;  // 구력(운동 경력) 반환 메서드
    }
    public void setWorkoutYears(int workoutYears) {
        this.workoutYears = workoutYears;
    }

    // 좋아요 수
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<String> getFavWorkouts() {
        return favWorkouts;
    }

    public void setFavWorkouts(List<String> favWorkouts) {
        this.favWorkouts = favWorkouts;
    }

    public String getWorkoutGoal() {
        return workoutGoal;
    }

    public void setWorkoutGoal(String workoutGoal) {
        this.workoutGoal = workoutGoal;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // quals 및 hashCode 메서드를 추가하여 객체 비교 및 해시 기반 컬렉션에서 사용
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProfile that = (UserProfile) o;

        return profileId == that.profileId;
    }

    @Override
    public int hashCode() {
        return profileId;
    }
}

