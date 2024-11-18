package com.example.healthbuddypro.FitnessTab.Routine;

public class GymLocationResponse {
    private int code;
    private String message;
    private LocationData data;


    // memo : 지오펜스 인증할때 /api/gymLocation/users/{userId} 응답값 받아오는 클래스

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LocationData getData() {
        return data;
    }

    public static class LocationData {
        private double latitude;
        private double longitude;
        private String region;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getRegion() {
            return region;
        }
    }
}
