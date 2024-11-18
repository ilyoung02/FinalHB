package com.example.healthbuddypro.ShortTermMatching;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchResponseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_response);

        // 매칭 수락/거절 처리
        int matchRequestId = getIntent().getIntExtra("matchRequestId", -1);

        // 예를 들어, 사용자가 수락 버튼을 눌렀다고 가정
        ShortMatchRequestStatus requestStatus = new ShortMatchRequestStatus("ACCEPTED");

        ApiService apiService = RetrofitClient.getApiService();
//        apiService.respondMatchRequest(matchRequestId, requestStatus).enqueue(new Callback<ShortMatchResponse>() {
//            @Override
//            public void onResponse(Call<ShortMatchResponse> call, Response<ShortMatchResponse> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(MatchResponseActivity.this, "매칭 신청 수락 성공", Toast.LENGTH_SHORT).show();
//                    // 채팅화면으로 이동 등 추가 작업 가능
//                } else {
//                    Toast.makeText(MatchResponseActivity.this, "매칭 신청 수락 실패", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ShortMatchResponse> call, Throwable t) {
//                Toast.makeText(MatchResponseActivity.this, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
