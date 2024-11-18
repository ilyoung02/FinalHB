package com.example.healthbuddypro.Matching.Chat;

// 서버로 메시지 요청을 보내기 위한 클래스
// 실시간 채팅을 Firebase에서만 처리할 예정이라면 이 파일은 필요 없다.
// 하지만, 백엔드에 메시지를 저장해야 한다면 필수
// 백엔드 사용 여부에 따라 판단

public class MessageRequest {
    private int profileId; // 채팅 대상 프로필 ID
    private String message; // 전송할 메시지

    public MessageRequest(int profileId, String message) {
        this.profileId = profileId;
        this.message = message;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

