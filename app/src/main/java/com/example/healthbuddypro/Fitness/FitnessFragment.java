package com.example.healthbuddypro.Fitness;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.example.healthbuddypro.FitnessTab.Routine.RoutineFragment;
import com.google.android.material.tabs.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.healthbuddypro.FitnessTab.GroupViewPagerAdapter;
import com.example.healthbuddypro.R;

public class FitnessFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_fitness 레이아웃을 inflate
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        GroupViewPagerAdapter adapter = new GroupViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
