package com.example.healthbuddypro.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.example.healthbuddypro.SignUp.SignUpResponse;
import com.example.healthbuddypro.SignUp.SignupActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private final String BASE_URL = "http://165.229.89.154:8080/";
    private static final String TAG = "LoginActivity";  // 로그 태그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI 요소 초기화
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        // 로그인 버튼 클릭 리스너
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // 회원가입 버튼 클릭 리스너
        findViewById(R.id.buttonSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 로그인 요청 객체 생성
        LoginRequest loginRequest = new LoginRequest(username, password);

        // Retrofit을 통해 로그인 요청 보내기
        ApiService apiService = RetrofitClient.getApiService();  // context 추가
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 로그인 성공 로그 출력
                    Log.d(TAG, "로그인 성공: " + response.body().toString());

                    LoginResponse.Data data = response.body().getData();

                    int profileId = data.getProfileId(); // SignUpResponse에서 profileId 가져오기
                    int userId = data.getUserId(); // SignUpResponse에서 userId 가져오기

                    saveProfileDataToSharedPreferences(profileId, userId);

                    // SharedPreferences에 로그인 상태 저장
                    SharedPreferences sharedPreferences = getSharedPreferences("HealthBuddyProPrefs",  MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);  // 로그인 상태 저장
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    // 로그인 성공 후 메인 화면으로 이동
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패 - ID, PW 불일치", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "네트워크 오류: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileDataToSharedPreferences(int profileId, int userId) {
        // SharedPreferences에 profileId와 userId 저장
        SharedPreferences sharedPreferences = getSharedPreferences("localID", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("profileId", profileId);  // profileId 저장
        editor.putInt("userId", userId);     // userId 저장
        editor.apply();  // 변경사항을 저장
    }
}
