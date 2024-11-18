package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class routine_list_item extends AppCompatActivity {

    private TextView hbdName, statusText;
    private ImageButton deleteRoutineList, edit_routinelist;
    private int teamId, userId, routineId;

    //memo : 루틴 목록 뺄거면 필요없음
    // todo : 루틴 목록에서 삭제 메소드 만들기
    //  헬스버디가 끝나서 진행종료 된 루틴은 수정은 불가 삭제만 가능하도록 하기
    //  진행중인 목록은 색상 바꿔서 표시하기 - 완료
    //  진행중인 목록 클릭하면 진행중인 루틴에 대해서만 설정한 루틴 정보들 종합으로 볼수있게하기 - api 나오면 ㄱㄱ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routine_list_item);

        hbdName = findViewById(R.id.hbdName);
        statusText = findViewById(R.id.status_text);
        deleteRoutineList = findViewById(R.id.delete_routinelist);
        edit_routinelist = findViewById(R.id.edit_routinelist);

        teamId = getTeamIdFromStorage();
        userId = getUserIdFromStorage();

        // 루틴 수정 버튼
        edit_routinelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), fix_routine.class);
                startActivity(intent);
            }
        });

        // 헬스버디 닉네임 표시
        loadHealthBuddyInfo();

        // 진행 상태 표시
        loadRoutineStatus();

        // 삭제 버튼 클릭 이벤트
//        deleteRoutineList.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    // 내부 저장소에서 teamId 가져오기
    private int getTeamIdFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_" + userId + "_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt("teamId", -1);
    }

    // 내부 저장소에서 userId 가져오기
    private int getUserIdFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("localID", MODE_PRIVATE);
        return sharedPreferences.getInt("userId", -1);
    }

    // 헬스버디 닉네임 정보를 불러와 hbdName에 설정
    private void loadHealthBuddyInfo() {
        getUserIdFromStorage(); // userId 가져오기
        getTeamIdFromStorage(); // teamId 가져오기

        if (teamId == -1 || userId == -1) {
            Log.e("routine_list_item", "teamId 또는 userId가 없습니다.");
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<MatchedUserResponse> call = apiService.getMatchedUserInfo(teamId, userId);

        call.enqueue(new Callback<MatchedUserResponse>() {
            @Override
            public void onResponse(Call<MatchedUserResponse> call, Response<MatchedUserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MatchedUserResponse.MatchedUserData data = response.body().getData();
                    String nickname = data.getNickname();
                    TextView hbdName = findViewById(R.id.hbdName);
                    hbdName.setText(nickname);
                } else {
                    Log.e("routine_list_item", "헬스버디 정보를 불러오는데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<MatchedUserResponse> call, Throwable t) {
                Log.e("routine_list_item", "네트워크 오류: " + t.getMessage());
            }
        });
    }


    // 진행 상태를 불러와 statusText에 설정
    private void loadRoutineStatus() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<TeamStatusResponse> call = apiService.getTeamStatus(teamId);

        call.enqueue(new Callback<TeamStatusResponse>() {
            @Override
            public void onResponse(Call<TeamStatusResponse> call, Response<TeamStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String teamStatus = response.body().getData().getTeamStatus();
                    if ("진행".equals(teamStatus)) {
                        statusText.setText("진행 중");
                        statusText.setBackgroundColor(getResources().getColor(R.color.progressing_color)); // 진행 중인 색상
                    } else {
                        statusText.setText("진행 종료");
                        statusText.setBackgroundColor(getResources().getColor(R.color.white)); // 진행 완료 색상
                    }
                } else {
                    Toast.makeText(routine_list_item.this, "진행 상태를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamStatusResponse> call, Throwable t) {
                Toast.makeText(routine_list_item.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 삭제 확인 팝업
//    private void showDeleteConfirmationDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("삭제 확인")
//                .setMessage("이 루틴을 삭제하시겠습니까?")
//                .setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteRoutine();
//                    }
//                })
//                .setNegativeButton("아니요", null)
//                .show();
//    }

    // todo : 삭제 하는 메소드 추가하기 -> deleteRoutine();
}
