package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.healthbuddypro.R;

public class GroupLockedFragment extends Fragment {

    private Button btn_makeRoutine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_locked, container, false);

        btn_makeRoutine = view.findViewById(R.id.btn_makeRoutine);

        btn_makeRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), make_routine01.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
