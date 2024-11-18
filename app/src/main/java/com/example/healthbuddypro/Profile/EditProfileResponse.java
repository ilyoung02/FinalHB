package com.example.healthbuddypro.Profile;

import java.util.List;

public class EditProfileResponse {
    private int code;
    private String message;
    private Data data;

    // Inner class Data 정의
    public static class Data {
        private String workoutGoal;
        private int workoutYears;
        private String bio;
        private GymLocation gymLocation;
        private boolean profileVisible;
        private List<String> favWorkouts;

        // Getter and Setter for workoutGoal
        public String getWorkoutGoal() {
            return workoutGoal;
        }

        public void setWorkoutGoal(String workoutGoal) {
            this.workoutGoal = workoutGoal;
        }

        // Getter and Setter for workoutYears
        public int getWorkoutYears() {
            return workoutYears;
        }

        public void setWorkoutYears(int workoutYears) {
            this.workoutYears = workoutYears;
        }

        // Getter and Setter for bio
        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        // GymLocation inner class 정의
        public static class GymLocation {
            private double latitude;
            private double longitude;
            private String region;

            // Getter and Setter for latitude
            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            // Getter and Setter for longitude
            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            // Getter and Setter for region
            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }
        }

        // Getter and Setter for gymLocation
        public GymLocation getGymLocation() {
            return gymLocation;
        }

        public void setGymLocation(GymLocation gymLocation) {
            this.gymLocation = gymLocation;
        }

        // Getter and Setter for profileVisible
        public boolean isProfileVisible() {
            return profileVisible;
        }

        public void setProfileVisible(boolean profileVisible) {
            this.profileVisible = profileVisible;
        }

        // Getter and Setter for favWorkouts
        public List<String> getFavWorkouts() {
            return favWorkouts;
        }

        public void setFavWorkouts(List<String> favWorkouts) {
            this.favWorkouts = favWorkouts;
        }
    }

    // Getter and Setter for code
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

    // Getter and Setter for data
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
