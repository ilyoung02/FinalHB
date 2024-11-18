package com.example.healthbuddypro.Matching.Chat;

public class Message {

    private String content;
    private boolean isSender;
    private String senderName;  // 메시지 보낸 사람의 이름

    // 기본 생성자 (Firestore에서 역직렬화 시 필요)
    public Message() {
    }

    // 필드를 초기화하는 생성자
    public Message(String content, boolean isSender, String senderName) {
        this.content = content;
        this.isSender = isSender;
        this.senderName = senderName;
    }

    // getter와 setter 추가
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSender() {
        return isSender;
    }
    public void setSender(boolean sender) {
        isSender = sender;
    }

    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
