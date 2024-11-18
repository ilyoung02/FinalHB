package com.example.healthbuddypro.FitnessTab.Routine;

public class Routine {
    private String name;
    private String routineTitle;

    public Routine(String name, String routineTitle) {
        this.name = name; // 루틴 목록에서 헬스버디 이름
        this.routineTitle = routineTitle; // 루틴 제목
    }

    public String getName() {
        return name;
    }

    public String getRoutineTitle() {
        return routineTitle;
    }
}
