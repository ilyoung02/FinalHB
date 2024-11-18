package com.example.healthbuddypro;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthbuddypro.Community.CommunityFragment;
import com.example.healthbuddypro.Fitness.FitnessFragment;
import com.example.healthbuddypro.Gym.GymFragment;
import com.example.healthbuddypro.Matching.MatchFragment;
import com.example.healthbuddypro.Profile.ProfileFragment;
import com.example.healthbuddypro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private int currentTabIndex = 2; // 기본 탭 인덱스는 2로 설정 (MatchFragment)
    private boolean routineUpdated;   // 루틴 업데이트 플래그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Intent로 전달된 newTabIndex 값 확인 (기본값은 2로 설정)
        // routine04 에서 루틴 생성 완료하고 main 으로 넘어올때만 운동 index = 0 이 나옴
        int initialTabIndex = getIntent().getIntExtra("newTabIndex", 2);
        routineUpdated = getIntent().getBooleanExtra("routine_updated", false); // 루틴 업뎃 플래그 확인

        // 기본 화면 설정
        if (savedInstanceState == null) {
            Fragment initialFragment;
            if (initialTabIndex == 0) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_fitness);
                initialFragment = new FitnessFragment();
            } else {
                bottomNavigationView.setSelectedItemId(R.id.navigation_match);
                initialFragment = new MatchFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, initialFragment)
                    .commit();

            // 초기 currentTabIndex 설정
            currentTabIndex = initialTabIndex;
        }

        // 네비게이션 아이템 클릭 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int newTabIndex = 0;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_match) {
                selectedFragment = new MatchFragment();
                newTabIndex = 2;
            } else if (itemId == R.id.navigation_fitness) {
                selectedFragment = new FitnessFragment();
                newTabIndex = 0;
            } else if (itemId == R.id.navigation_gym) {
                selectedFragment = new GymFragment();
                newTabIndex = 1;
            } else if (itemId == R.id.navigation_community) {
                selectedFragment = new CommunityFragment();
                newTabIndex = 3;
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                newTabIndex = 4;
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // 애니메이션 설정
                if (newTabIndex > currentTabIndex) {
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (newTabIndex < currentTabIndex) {
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                }

                transaction.replace(R.id.fragment_container, selectedFragment);
                transaction.commit();

                // 현재 탭 인덱스 업데이트
                currentTabIndex = newTabIndex;
            }

            return true;
        });
    }

}

