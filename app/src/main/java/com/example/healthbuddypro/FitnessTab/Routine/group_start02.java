package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.Matching.ProfileResponse;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.example.healthbuddypro.Matching.UserProfile;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class group_start02 extends AppCompatActivity {

    private Button btn_startTodayRoutine;
    private TextView myname, buddyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group_start02);

        btn_startTodayRoutine = findViewById(R.id.btn_startTodayRoutine);
        myname = findViewById(R.id.myname);
        buddyName = findViewById(R.id.buddyName);

        // 상대 헬스버디 이름 가지고옴
        loadBuddyNickname();
        loadMyNickname();

        btn_startTodayRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(group_start02.this, routine_check.class);
                startActivity(intent);
            }
        });

    }

    // 내이름 가져오는건 userId = profileId 라고 치고 매칭 상세보기 /api/match/profiles/{profileId} 여기서 들고옴
    private void loadMyNickname() {
        Context context = getApplicationContext();
        if (context == null) return;

        // 내부 저장소에서 profileId (=userId)를 가져옴
        SharedPreferences userPreferences = context.getSharedPreferences("localID", Context.MODE_PRIVATE);
        int profileId = userPreferences.getInt("userId", -1); // profileId = userId

        if (profileId == -1) {
            Log.e("GroupStart02", "Profile ID not found in local storage.");
            return;
        }

        ApiService service = RetrofitClient.getApiService();
        Call<ProfileResponse> call = service.getProfileDetails(profileId);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    com.example.healthbuddypro.Matching.UserProfile userProfile = response.body().getData();
                    String myNickname = userProfile.getNickName();
                    myname.setText(myNickname + "\n나"); // myname 에 내 이름 세팅
                    Log.d("내이름 불러오기", "내 이름 : " + myNickname);

                } else {
                    Toast.makeText(getApplicationContext(), "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    // 내부 저장소에서 teamId와 userId를 가져와 헬스버디의 이름을 불러오는 메서드
    private void loadBuddyNickname() {
        Context context = getApplicationContext();
        if (context == null) return;

        // 내부 저장소에서 userId를 먼저 가져옴
        SharedPreferences userPreferences = context.getSharedPreferences("localID", Context.MODE_PRIVATE);
        int userId = userPreferences.getInt("userId", -1);

        if (userId == -1) {
            Log.e("RoutineStartFragment", "User ID not found in local storage.");
            return;
        }

        // userId로 구성된 SharedPreferences 파일에서 teamId를 가져옴
        SharedPreferences teamPreferences = context.getSharedPreferences("user_" + userId + "_prefs", Context.MODE_PRIVATE);
        int teamId = teamPreferences.getInt("teamId", -1);

        if (teamId == -1) {
            Log.e("RoutineStartFragment", "Team ID not found for user: " + userId);
            return;
        }

        // 여기서 이제 서버에서 불러옴
        ApiService service = RetrofitClient.getApiService();
        Call<MatchedUserResponse> call = service.getMatchedUser(teamId, userId);

        call.enqueue(new Callback<MatchedUserResponse>() {
            @Override
            public void onResponse(Call<MatchedUserResponse> call, Response<MatchedUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String buddyNickname = response.body().getData().getNickname();
                    buddyName.setText(buddyNickname + "\n헬스버디"); // 헬스버디 이름을 표시
                } else {
                    Toast.makeText(getApplicationContext(), "헬스버디 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MatchedUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}