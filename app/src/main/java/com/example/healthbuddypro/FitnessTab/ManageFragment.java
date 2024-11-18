package com.example.healthbuddypro.FitnessTab;

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
import com.example.healthbuddypro.FitnessTab.quit_hb01; // Import the quit_hb01 activity

public class ManageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        ImageButton quitButton = view.findViewById(R.id.btn_hbdQuit);

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), quit_hb01.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
