package com.example.healthbuddypro.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.Login.LoginActivity;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private static final String BASE_URL = "http://165.229.89.154:8080/";

    private Button buttonLogout;
    private Button btnEdit;
    private TextView profileNickname, profileGender, profileAge, profileExperience, profileLocation, profileGoal, profileBio, likeCount;
    private ImageView profileImage;
    private RecyclerView favWorkoutsRecyclerView;
    private Switch healthBuddySwitch;
    private FavWorkoutsAdapter favWorkoutsAdapter;
    private int profileId;  // Profile ID

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileNickname = view.findViewById(R.id.profile_nickname);
        profileGender = view.findViewById(R.id.profile_gender);
        profileAge = view.findViewById(R.id.profile_age);
        profileExperience = view.findViewById(R.id.profile_experience);
        profileLocation = view.findViewById(R.id.profile_location);
        profileGoal = view.findViewById(R.id.exercise_goal_text);
        profileBio = view.findViewById(R.id.profile_bio_text);
        profileImage = view.findViewById(R.id.profile_image);
        likeCount = view.findViewById(R.id.like_count);
        healthBuddySwitch = view.findViewById(R.id.health_buddy_switch);
        favWorkoutsRecyclerView = view.findViewById(R.id.workouts_recycler_view);

        favWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        buttonLogout = view.findViewById(R.id.buttonLogout);
        btnEdit = view.findViewById(R.id.btn_edit);

        // 로그아웃 버튼
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog();
            }
        });

        // 수정 버튼
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = requireActivity().getSharedPreferences("localID", MODE_PRIVATE);
        profileId = preferences.getInt("profileId", -1);  // If profileId not found, default to -1

        // Check if profileId is valid, and then fetch profile data
        if (profileId == -1) {
            Log.e("ProfileFragment", "Profile ID not found in SharedPreferences.");
            showToast("프로필 ID를 찾을 수 없습니다.");  // You can replace this with your actual toast method
        } else {
            // Fetch profile data
            fetchProfileData(profileId);
        }

        return view;
    }

    // 서버에서 프로필 데이터를 가져오는 메소드
    private void fetchProfileData(int profileId) {
        ApiService profileService = RetrofitClient.getApiService();

        Call<MyProfileResponse> call = profileService.getProfileData(profileId);  // profileId를 전달하여 서버에서 데이터 호출
        call.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 서버에서 받은 프로필 데이터를 UI에 적용
                    MyProfileResponse.ProfileData profileData = response.body().getData();

                    // 프로필 데이터 UI 설정
                    profileNickname.setText(profileData.getNickname());
                    profileGender.setText(profileData.getGender());
                    profileAge.setText(String.valueOf(profileData.getAge()));
                    profileExperience.setText(String.valueOf(profileData.getWorkoutYears()));
                    profileLocation.setText(profileData.getRegion());
                    profileGoal.setText(profileData.getWorkoutGoal());
                    profileBio.setText(profileData.getBio());
                    likeCount.setText(String.valueOf(profileData.getLikeCount()));


                    String imageUrl = profileData.getImageUrl();

                    Log.d("ProfileFragment", "Image URL: " + imageUrl);

                    // 이미지 URL이 유효한지 확인 후 처리
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Glide를 사용해 ImageView에 이미지 로드
                        Glide.with(ProfileFragment.this)
                                .load(imageUrl)
                                .error(R.drawable.default_profile_image)  // 오류 시 기본 이미지 표시
                                .into(profileImage);
                    } else {
                        // 이미지 URL이 없을 경우 기본 이미지 사용
                        Log.e("ProfileFragment", "Image URL is null or empty.");
                        profileImage.setImageResource(R.drawable.default_profile_image);
                    }

                    // 즐겨찾기한 운동 목록 설정
                    List<String> favWorkouts = profileData.getFavWorkouts();
                    favWorkoutsAdapter = new FavWorkoutsAdapter(favWorkouts);
                    favWorkoutsRecyclerView.setAdapter(favWorkoutsAdapter);

                } else {
                    Log.e("ProfileFragment", "Failed to fetch profile data.");
                }
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                Log.e("ProfileFragment", "Error fetching profile data", t);
            }
        });
    }

    // Logout confirmation dialog
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logoutUser();
                    }
                })
                .setNegativeButton("아니오", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Logout user method
    private void logoutUser() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        requireActivity().finish();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
