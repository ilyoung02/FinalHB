package com.example.healthbuddypro.Matching.Chat;

public class MatchRequestData {
    private int matchRequestId;
    private int senderId;
    private int receiverId;
    private String status;

    public MatchRequestData() {} // Firestore를 위한 빈 생성자

    public MatchRequestData(int matchRequestId, int senderId, int receiverId, String status) {
        this.matchRequestId = matchRequestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public int getMatchRequestId() {
        return matchRequestId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getStatus() {
        return status;
    }
}
