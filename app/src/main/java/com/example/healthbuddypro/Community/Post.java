package com.example.healthbuddypro.Community;

public class Post {
    private final String username;
    private final String postTime;
    private final String content;

    public Post(String username, String postTime, String content) {
        this.username = username;
        this.postTime = postTime;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getContent() {
        return content;
    }
}
