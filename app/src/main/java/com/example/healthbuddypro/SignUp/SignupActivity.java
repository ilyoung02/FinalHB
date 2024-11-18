package com.example.healthbuddypro.SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.Login.LoginActivity;
import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.example.healthbuddypro.SignUp.SignUpRequest;
import com.example.healthbuddypro.SignUp.SignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextNickname, editTextUsername, editTextPassword, editTextPasswordConfirm, editTextYear, editTextMonth, editTextDay;
    private RadioGroup radioGroupGender;
    private Spinner spinnerLocation;

    // 지역명과 코드 매핑
    private String[] locations = {"대구 북구", "대구 서구", "대구 동구", "대구 중구", "대구 달서구", "대구 수성구", "대구 남구", "대구 달성군", "대구 군위군"};
    private String[] locationMappings = {"BUKGU", "SEOGU", "DONGGU", "JUNGGU", "DALSEOGU", "SUSEONGGU", "NAMGU", "DALSEONGGUN", "GUNWIGUN"};
    private String gender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // UI 요소 초기화
        editTextNickname = findViewById(R.id.editTextNickname);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextYear = findViewById(R.id.editTextYear);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextDay = findViewById(R.id.editTextDay);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        spinnerLocation = findViewById(R.id.spinnerLocation);

        // 지역 선택을 위한 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(adapter);

        // 성별 선택 리스너
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMale) {
                    gender = "MAN";
                } else if (checkedId == R.id.radioFemale) {
                    gender = "WOMAN";
                }
            }
        });

        // 회원가입 버튼 클릭 리스너
        findViewById(R.id.buttonSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignup();
            }
        });
    }

    private void performSignup() {
        String nickname = editTextNickname.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextPasswordConfirm.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String month = editTextMonth.getText().toString().trim();
        String day = editTextDay.getText().toString().trim();

        // 지역명 매핑
        String selectedLocation = locationMappings[spinnerLocation.getSelectedItemPosition()]; // 위치에 따른 코드 매핑

        if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(year) || TextUtils.isEmpty(month) ||
                TextUtils.isEmpty(day) || TextUtils.isEmpty(selectedLocation)) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (gender == null) {
            Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 회원가입 요청 생성
        String birthDate = year + "." + month + "." + day + " 12:00";
        SignUpRequest signUpRequest = new SignUpRequest(username, password, confirmPassword, nickname, birthDate, gender, selectedLocation);

        ApiService apiService = RetrofitClient.getApiService();
        Call<SignUpResponse> call = apiService.signUp(signUpRequest);

        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 성공적으로 회원가입이 된 경우 profileId와 userId를 저장
                    SignUpResponse.Data data = response.body().getData();
                    if (data != null) {
                        int profileId = data.getProfileId(); // SignUpResponse에서 profileId 가져오기
                        int userId = data.getUserId(); // SignUpResponse에서 userId 가져오기

                        saveProfileDataToSharedPreferences(profileId, userId);

                        Toast.makeText(SignupActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "회원가입 실패: 응답 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 회원가입 실패 시 상태 코드와 오류 메시지 출력
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("SignUpError", "회원가입 실패: " + response.code() + " " + errorBody);
                        Toast.makeText(SignupActivity.this, "회원가입 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.e("SignUpFailure", "네트워크 오류: " + t.getMessage());
                Toast.makeText(SignupActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfileDataToSharedPreferences(int profileId, int userId) {
        // SharedPreferences에 profileId와 userId 저장
        SharedPreferences sharedPreferences = getSharedPreferences("HealthBuddyProPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("profileId", profileId);  // profileId 저장
        editor.putInt("userId", userId);     // userId 저장
        editor.apply();  // 변경사항을 저장
    }
}
