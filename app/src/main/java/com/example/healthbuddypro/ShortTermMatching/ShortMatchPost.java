package com.example.healthbuddypro.ShortTermMatching;

public class ShortMatchPost {
    private int senderId; // 단기 매칭 요청 보내는 사용자 id
    private int receiverId; // 단기 매칭 요청 받는 사용자 id
    private String title; // 글쓰기 제목
    private String content; // 내용
    private String location; // 지역
    private String category; // 성별 카테고리
    private String health;

    public ShortMatchPost(int senderId, int receiverId, String title, String content, String health, String location, String category) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.title = title;
        this.content = content;
        this.location = location;
        this.category = category;
        this.health = health;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getTitle() {
        return title;
    }

    public String getHealth() {
        return health;
    }

    public String getContent() {
        return content;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }
}
