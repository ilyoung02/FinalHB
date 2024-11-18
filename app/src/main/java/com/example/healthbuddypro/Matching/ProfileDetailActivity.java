package com.example.healthbuddypro.Matching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.Matching.Chat.ChatActivity;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView name;
    private TextView age;
    private TextView gender;
    private TextView favWorkouts;
    private TextView workoutGoal;
    private TextView region;
    private TextView bio;
    private TextView workoutYears;
    private TextView likeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // View 초기화
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.profile_name);
        age = findViewById(R.id.profile_age);
        gender = findViewById(R.id.profile_gender);
        favWorkouts = findViewById(R.id.profile_fav_workouts);
        workoutGoal = findViewById(R.id.profile_workout_goal);
        region = findViewById(R.id.profile_region);
        bio = findViewById(R.id.profile_bio);
        workoutYears = findViewById(R.id.profile_workout_years);
        likeCount = findViewById(R.id.like_count);

        // Intent로 전달된 데이터를 받음
        Intent intent = getIntent();
        int profileId = intent.getIntExtra("profileId", -1); // profileId를 받음

        // 받은 데이터를 UI에 표시할 수 있도록 설정
        if (profileId != -1) {
            fetchProfileDetails(profileId); // profileId를 사용하여 백엔드에서 데이터 가져오기
        } else {
            // 전달된 데이터가 없을 경우의 처리
            Toast.makeText(this, "프로필 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        // 채팅 버튼 클릭 처리
        Button btnChat = findViewById(R.id.btn_chat);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetailActivity.this, ChatActivity.class);
                intent.putExtra("profileId", profileId);  // profileId 전달
                startActivity(intent);
            }
        });
    }

    private void fetchProfileDetails(int profileId) {
        ApiService apiService = RetrofitClient.getApiService();

        // 단일 프로필 데이터를 가져오도록 함
        Call<ProfileResponse> call = apiService.getProfileDetails(profileId);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 프로필 데이터 설정
                    UserProfile profile = response.body().getData();
                    updateUI(profile);
                } else {
                    String errorMessage = "프로필 정보를 불러오지 못했습니다. 오류 코드: " + response.code();
                    bio.setText(errorMessage);  // bio로 오류 메시지 표시
                    Toast.makeText(ProfileDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("ProfileDetailActivity", errorMessage);  // Logcat에 오류 메시지 출력
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                String errorMessage = "데이터 로드 실패: " + t.getMessage();
                bio.setText(errorMessage);  // bio로 오류 메시지 표시
                Toast.makeText(ProfileDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ProfileDetailActivity", errorMessage);  // Logcat에 오류 메시지 출력
            }
        });
    }

    private void updateUI(UserProfile profile) {
        // 추가적으로 받은 데이터로 UI 업데이트
        name.setText(profile.getNickName());  // 닉네임 설정
        bio.setText(profile.getBio());  // 소개글 설정
        gender.setText(profile.getGender());  // 성별 설정
        age.setText(profile.getAge() + "세");  // 나이 설정
        favWorkouts.setText("선호 운동 : " + String.join(", ", profile.getFavWorkouts()));  // 선호 운동 설정 (리스트를 문자열로 변환)
        workoutGoal.setText(profile.getWorkoutGoal());  // 운동 목표 설정
        region.setText(profile.getRegion());  // 지역 설정
        workoutYears.setText("구력 : " + profile.getWorkoutYears() + "년");  // 운동 경력 설정
        likeCount.setText(String.valueOf(profile.getLikeCount())); // 좋아요 수를 문자열로 변환하여 설정

        // 이미지 로딩 (Glide 사용)
        String imageUrl = profile.getImageUrl(); // UserProfile 객체에서 이미지 URL을 가져옴
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .error(R.drawable.default_user_img)  // 이미지 로드 실패 시 기본 이미지 설정
                    .into(profileImage);
        } else {
            Glide.with(this)
                    .load(R.drawable.default_user_img)  // 기본 이미지 로드
                    .into(profileImage);
        }
    }
}
