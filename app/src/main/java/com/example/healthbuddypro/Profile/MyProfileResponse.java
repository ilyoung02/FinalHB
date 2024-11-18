package com.example.healthbuddypro.Profile;

import java.util.List;

public class MyProfileResponse {
    private int code;
    private String message;
    private ProfileData data;

    public class ProfileData {
        private int userId;
        private String imageUrl;
        private String nickname;
        private String gender;
        private int age;
        private int workoutYears;
        private List<String> favWorkouts;
        private String workoutGoal;
        private String region;
        private String bio;
        private int likeCount;

        public String getImageUrl() { return imageUrl; }
        public String getNickname() { return nickname; }
        public String getGender() { return gender; }
        public int getAge() { return age; }
        public int getWorkoutYears() { return workoutYears; }
        public List<String> getFavWorkouts() { return favWorkouts; }  // Updated getter
        public String getWorkoutGoal() { return workoutGoal; }
        public String getRegion() { return region; }
        public String getBio() { return bio; }
        public int getLikeCount() { return likeCount; }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // Getter and Setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }

}
