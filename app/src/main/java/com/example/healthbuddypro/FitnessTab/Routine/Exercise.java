package com.example.healthbuddypro.FitnessTab.Routine;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    private String name; // 운동종목명
    private int setCount; // 갯수
    private int reps; // 세트수

    // 생성자
    public Exercise(String name, int setCount, int reps) {
        this.name = name;
        this.setCount = setCount;
        this.reps = reps;
    }

    // Getter 메서드
    public String getName() {
        return name;
    }

    public int getSetCount() {
        return setCount;
    }

    public int getReps() {
        return reps;
    }

    // Parcelable 구현
    protected Exercise(Parcel in) {
        name = in.readString();
        setCount = in.readInt();
        reps = in.readInt();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(setCount);
        dest.writeInt(reps);
    }
}
