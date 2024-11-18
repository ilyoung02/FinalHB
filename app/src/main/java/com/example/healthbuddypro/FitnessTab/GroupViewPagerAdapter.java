package com.example.healthbuddypro.FitnessTab;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.healthbuddypro.FitnessTab.Routine.RoutineFragment;

public class GroupViewPagerAdapter extends FragmentPagerAdapter {

    public GroupViewPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RoutineFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new ManageFragment();
            case 3:
                return new RankFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4; // Number of tabs
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "루틴";
            case 1:
                return "채팅";
            case 2:
                return "관리";
            case 3:
                return "랭킹";
            default:
                return null;
        }
    }
}
