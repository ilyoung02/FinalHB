package com.example.healthbuddypro.Matching.Chat;

public class MatchData {
    private int matchRequestId;
    private int teamId;  // teamId 필드 추가

    public int getMatchRequestId() {
        return matchRequestId;
    }

    public void setMatchRequestId(int matchRequestId) {
        this.matchRequestId = matchRequestId;
    }

    public int getTeamId() {  // teamId의 getter 메서드 추가
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
