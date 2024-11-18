package com.example.healthbuddypro.Fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthbuddypro.R;

public class RoutineFragment extends Fragment {

    private ImageButton btnRoutineList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        //TODO : 루틴 설정을 다하고 RoutineFragment 로 넘어왔을 때 설정한 정보를 오늘의 운동에 표시되도록 서버로부터 받아와서
        // 그 정보들이 갱신되어 화면에 나타나도록 해야함 -> 설정 안했을때는 group_locked 화면이 떠야하고 / 설정이 되면 그 정보가 떠야함
        // moved_routineFragment 은 화면 덧씌우기용이라 여기에 동작 코드를 추가해야함

//        // ImageButton 초기화
//        btnRoutineList = view.findViewById(R.id.btn_routineList);
//
//        // 버튼 클릭 리스너 설정
//        btnRoutineList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), routine_list.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }
}
