package com.example.healthbuddypro.Profile;

import java.util.List;

public class EditUserProfile {

    // Variables
    private String workoutGoal; // 운동 목표 (String 타입)
    private int workoutYears; // 운동 경력 (int 타입)
    private String bio; // 소개글 (String 타입)
    private GymLocation gymLocation; // 헬스장 위치 (GymLocation 클래스 사용)
    private boolean profileVisible; // 매칭 탭에 프로필 노출 여부 (boolean 타입)
    private List<String> favWorkouts; // 선호하는 운동 리스트 (List<String> 타입)

    public EditUserProfile() {
    }
    // Constructor
    public EditUserProfile(String workoutGoal, int workoutYears, String bio,
                           GymLocation gymLocation, boolean profileVisible,
                           List<String> favWorkouts) {
        this.workoutGoal = workoutGoal;
        this.workoutYears = workoutYears;
        this.bio = bio;
        this.gymLocation = gymLocation;
        this.profileVisible = profileVisible;
        this.favWorkouts = favWorkouts;
    }

    // Getters and Setters
    public String getWorkoutGoal() {
        return workoutGoal;
    }

    public void setWorkoutGoal(String workoutGoal) {
        this.workoutGoal = workoutGoal;
    }

    public int getWorkoutYears() {
        return workoutYears;
    }

    public void setWorkoutYears(int workoutYears) {
        this.workoutYears = workoutYears;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public GymLocation getGymLocation() {
        return gymLocation;
    }

    public void setGymLocation(GymLocation gymLocation) {
        this.gymLocation = gymLocation;
    }

    public boolean isProfileVisible() {
        return profileVisible;
    }

    public void setProfileVisible(boolean profileVisible) {
        this.profileVisible = profileVisible;
    }

    public List<String> getFavWorkouts() {
        return favWorkouts;
    }

    public void setFavWorkouts(List<String> favWorkouts) {
        this.favWorkouts = favWorkouts;
    }

    // Inner class for GymLocation
    public static class GymLocation {
        private double latitude; // 위도 (double 타입)
        private double longitude; // 경도 (double 타입)
        private String region; // 지역 (String 타입)


        public GymLocation() {
        }

        // Constructor
        public GymLocation(double latitude, double longitude, String region) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.region = region;
        }

        // Getters and Setters
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }
}