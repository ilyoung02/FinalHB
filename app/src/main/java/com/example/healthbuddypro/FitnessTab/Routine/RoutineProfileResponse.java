package com.example.healthbuddypro.FitnessTab.Routine;

import java.util.List;

public class RoutineProfileResponse {
    private int code;
    private String message;
    private ProfileData data;

    // Getter and Setter
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

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }

    // Nested class for profile data
    public static class ProfileData {
        private int userId;
        private String image;
        private String nickname;
        private String gender;
        private int age;
        private int workoutYears;
        private List<String> favWorkouts;
        private String workoutGoal;
        private String region;
        private String bio;
        private int likeCount;
        private List<Review> review;

        // Getters and Setters
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getWorkoutYears() {
            return workoutYears;
        }

        public void setWorkoutYears(int workoutYears) {
            this.workoutYears = workoutYears;
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

        public int getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public List<Review> getReview() {
            return review;
        }

        public void setReview(List<Review> review) {
            this.review = review;
        }

        // Nested class for review data
        public static class Review {
            private String writer;
            private String content;

            // Getters and Setters
            public String getWriter() {
                return writer;
            }

            public void setWriter(String writer) {
                this.writer = writer;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
