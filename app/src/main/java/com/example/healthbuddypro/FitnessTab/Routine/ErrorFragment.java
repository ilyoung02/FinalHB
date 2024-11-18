package com.example.healthbuddypro.FitnessTab.Routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthbuddypro.R;

public class ErrorFragment extends Fragment {

    // 별다른 동작은 없음 그냥 에러 뜨면 이거 나옴

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 프래그먼트 레이아웃을 inflate
        return inflater.inflate(R.layout.fragment_error, container, false);
    }
}
