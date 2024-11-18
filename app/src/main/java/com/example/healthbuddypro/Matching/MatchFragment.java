package com.example.healthbuddypro.Matching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.example.healthbuddypro.ShortTermMatching.ShortTermMatchFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MatchFragment extends Fragment {
    private ViewPager2 viewPager;
    private ProfilePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        viewPager = view.findViewById(R.id.viewPager);

        Button btn1To1Matching = view.findViewById(R.id.btn_1to1_matching);
        Button btnShortTermMatching = view.findViewById(R.id.btn_short_term_matching);

        // 앱 시작 시 1:1 매칭 버튼을 기본 선택 상태로 설정
        btn1To1Matching.setSelected(true);

        // 1:1 매칭 버튼 클릭 리스너
        btn1To1Matching.setOnClickListener(v -> {
            btn1To1Matching.setSelected(true);
            btnShortTermMatching.setSelected(false);

            // 1:1 매칭 화면을 다시 로드 (이 경우엔 아무 동작 필요 없음)
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MatchFragment());
            transaction.commit();
        });

        // 단기 매칭 버튼 클릭 리스너
        btnShortTermMatching.setOnClickListener(v -> {
            btn1To1Matching.setSelected(false);
            btnShortTermMatching.setSelected(true);

            // ShortTermMatchFragment로 전환
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // 프래그먼트 전환 시 애니메이션 추가
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);

            transaction.replace(R.id.fragment_container, new ShortTermMatchFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        //Retrofit으로 백엔드에서 프로필 데이터 가져오기
        fetchProfilesFromBackend();

        return view;
    }

    private void fetchProfilesFromBackend() {
        ApiService apiService = RetrofitClient.getApiService();

        // 백엔드에서 모든 사용자의 프로필 데이터 가져오는 부분
        Call<ProfileListResponse> call = apiService.getProfiles();
        call.enqueue(new Callback<ProfileListResponse>() {
            @Override
            public void onResponse(Call<ProfileListResponse> call, Response<ProfileListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    List<UserProfile> profiles = response.body().getData();
                    if (profiles != null && !profiles.isEmpty()) {
                        setupViewPager(profiles); // 데이터를 ViewPager에 설정
                    } else {
                        Toast.makeText(getContext(), "프로필 목록이 비어 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "데이터를 불러오지 못했습니다. 오류 코드: " + response.code();
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("MatchFragment", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ProfileListResponse> call, Throwable t) {
                String errorMessage = "데이터 로드 실패: " + t.getMessage();
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("MatchFragment", errorMessage); // Logcat에 오류 메시지 출력
            } // 오류 처리
        });
    }

    // 데이터를 item_profile로 보여주는 부분
    // ProfileDetailActivity로 정보 넘겨줌 (ProfileId로 구분 -> 단일 프로필 정보 불러오기)
    private void setupViewPager(List<UserProfile> profiles) {
        adapter = new ProfilePagerAdapter(profiles, profile -> {
            Intent intent = new Intent(getContext(), ProfileDetailActivity.class);
            intent.putExtra("profileId", profile.getProfileId());
            intent.putExtra("nickname", profile.getNickName() != null ? profile.getNickName() : "Unknown");
            intent.putExtra("gender", profile.getGender() != null ? profile.getGender() : "Not specified");
            intent.putExtra("age", profile.getAge());
            intent.putExtra("image", profile.getImageUrl() != null ? profile.getImageUrl() : "");
            intent.putExtra("workoutYears", profile.getWorkoutYears() + "년");
            intent.putExtra("workoutGoal", profile.getWorkoutGoal());
            intent.putExtra("bio", profile.getBio());
            intent.putExtra("region", profile.getRegion());
            intent.putExtra("likeCount", profile.getLikeCount());
            startActivity(intent);
        });
        viewPager.setAdapter(adapter);
    }
}
