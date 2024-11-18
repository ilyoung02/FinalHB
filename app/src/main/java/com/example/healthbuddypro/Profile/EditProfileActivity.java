package com.example.healthbuddypro.Profile;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.Login.LoginActivity;
import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.Profile.EditUserProfile.GymLocation;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SET_LOCATION = 1;
    private static final int REQUEST_CODE_WORKOUT_LIST = 2;
    private static final String LOCATION_PREFS = "LocationData";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private final String BASE_URL = "http://165.229.89.154:8080/";
    private final String[] locationMappings = {"BUKGU", "SEOGU", "DONGGU", "JUNGGU", "DALSEOGU", "SUSEONGGU", "NAMGU", "DALSEONGGUN", "GUNWIGUN"};
    private final HashMap<String, String> goalMapping = new HashMap<String, String>() {{
        put("체중 감량", "LOSE_WEIGHT");
        put("근육 증가", "GAIN_MUSCLE");
        put("지구력 향상", "IMPROVE_ENDURANCE");
        put("근력 향상", "INCREASE_STRENGTH");
        put("유연성 향상", "IMPROVE_FLEXIBILITY");
        put("건강 유지", "MAINTAIN_HEALTH");
        put("심폐 능력 향상", "IMPROVE_CARDIO");
        put("부상 회복", "RECOVER_FROM_INJURY");
        put("균형 향상", "IMPROVE_BALANCE");
    }};
    private final HashMap<String, String> exerciseMapping = new HashMap<String, String>() {{
        // 등
        put("랫 풀 다운", "LAT_PULLDOWN");
        put("바벨 로우", "BARBELL_ROW");
        put("덤벨 로우", "DUMBBELL_ROW");
        put("데드리프트", "DEADLIFT");
        put("시티드 로우", "SEATED_ROW");
        put("풀업", "PULL_UP");
        put("케이블 로우", "CABLE_ROW");
        put("티 바 로우", "T_BAR_ROW");

        // 가슴
        put("벤치 프레스", "BENCH_PRESS");
        put("인클라인 벤치 프레스", "INCLINE_BENCH_PRESS");
        put("디클라인 벤치 프레스", "DECLINE_BENCH_PRESS");
        put("덤벨 플라이", "DUMBBELL_FLY");
        put("체스트 프레스", "CHEST_PRESS");
        put("펙덱 플라이", "PEC_DECK_FLY");
        put("푸쉬업", "PUSH_UP");
        put("케이블 크로스오버", "CABLE_CROSSOVER");

        // 하체
        put("스쿼트", "SQUAT");
        put("레그 프레스", "LEG_PRESS");
        put("런지", "LUNGE");
        put("레그 컬", "LEG_CURL");
        put("레그 익스텐션", "LEG_EXTENSION");
        put("카프 레이즈", "CALF_RAISE");
        put("힙 쓰러스트", "HIP_THRUST");
        put("스티프 레그 데드리프트", "STIFF_LEG_DEADLIFT");

        // 유산소
        put("러닝머신", "TREADMILL");
        put("자전거", "BICYCLE");
        put("스텝퍼", "STEPPER");
        put("로잉머신", "ROWING_MACHINE");
        put("점핑잭", "JUMPING_JACK");
        put("버피", "BURPEE");
        put("마운틴 클라이머", "MOUNTAIN_CLIMBER");
        put("사이클", "CYCLE");

        // 어깨
        put("숄더 프레스", "SHOULDER_PRESS");
        put("사이드 레터럴 레이즈", "SIDE_LATERAL_RAISE");
        put("프론트 레이즈", "FRONT_RAISE");
        put("리어 델트 플라이", "REAR_DELT_FLY");
        put("업라이트 로우", "UPRIGHT_ROW");
        put("덤벨 숄더 프레스", "DUMBBELL_SHOULDER_PRESS");
        put("케이블 레이즈", "CABLE_RAISE");
        put("아놀드 프레스", "ARNOLD_PRESS");

        // 이두
        put("바벨 컬", "BARBELL_CURL");
        put("덤벨 컬", "DUMBBELL_CURL");
        put("해머 컬", "HAMMER_CURL");
        put("프리처 컬", "PREACHER_CURL");
        put("컨센트레이션 컬", "CONCENTRATION_CURL");
        put("케이블 컬", "CABLE_CURL");
        put("머신 컬", "MACHINE_CURL");
        put("크로스 바디 해머 컬", "CROSS_BODY_HAMMER_CURL");

        // 삼두
        put("트라이셉스 익스텐션", "TRICEPS_EXTENSION");
        put("덤벨 킥백", "DUMBBELL_KICKBACK");
        put("케이블 푸쉬다운", "CABLE_PUSH_DOWN");
        put("프렌치 프레스", "FRENCH_PRESS");
        put("스컬 크러셔", "SKULL_CRUSHER");
        put("오버헤드 익스텐션", "OVERHEAD_EXTENSION");
        put("딥스", "DIPS");
        put("트라이앵글 푸쉬업", "TRIANGLE_PUSH_UP");

        // 복근
        put("크런치", "CRUNCH");
        put("레그레이즈", "LEG_RAISE");
        put("플랭크", "PLANK");
        put("싯업", "SIT_UP");
        put("러시안 트위스트", "RUSSIAN_TWIST");
        put("바이시클 크런치", "BICYCLE_CRUNCH");
        put("힐터치", "HEEL_TOUCH");
        put("하이 플랭크", "HIGH_PLANK");
    }};

    private Uri uri;
    private ImageView imageView;
    private TextView gymLocation, changePhotoButton, favWorkouts;
    private Spinner workoutGoalSpinner, regionSpinner;
    private EditText workoutExperienceInput, introductionInput;
    private RadioGroup profileVisibleRadioGroup;
    private Button confirmButton, cancelButton, gymLocationBtn, workoutsBtn;

    // 갤러리에서 이미지 선택 결과를 처리할 ActivityResultLauncher
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    uri = result.getData().getData();  // 선택한 이미지의 URI 가져오기
                    if (uri != null) {
                        imageView.setImageURI(uri);    // ImageView에 선택한 이미지 표시
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initializeViews();
        loadGymLocation();
        requestPermissions();

        gymLocationBtn.setOnClickListener(v -> openLocationPicker());
        cancelButton.setOnClickListener(v -> finish());
        confirmButton.setOnClickListener(v -> updateProfile());

        // 갤러리에서 이미지 선택하는 버튼 리스너 추가
        changePhotoButton.setOnClickListener(v -> openGallery());

        // workoutsBtn 클릭 리스너 추가
        workoutsBtn.setOnClickListener(v -> openWorkoutList());


    }

    // 화면에 나타나는 것들 선택
    private void initializeViews() {
        imageView = findViewById(R.id.profile_image);
        workoutGoalSpinner = findViewById(R.id.workout_goal_spinner);
        regionSpinner = findViewById(R.id.region_spinner);
        confirmButton = findViewById(R.id.confirm_button);
        cancelButton = findViewById(R.id.cancel_button);
        gymLocationBtn = findViewById(R.id.gym_location_button);
        gymLocation = findViewById(R.id.gym_location);
        workoutExperienceInput = findViewById(R.id.workout_experience_input);
        introductionInput = findViewById(R.id.introduction_input);
        favWorkouts = findViewById(R.id.fav_Workouts); // Initialize favWorkouts TextView
        workoutsBtn = findViewById(R.id.fav_Workouts_button);

        profileVisibleRadioGroup = findViewById(R.id.radioGroupProfileVisible);
        changePhotoButton = findViewById(R.id.change_photo);  // 사진 변경 버튼

        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_goals_array, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutGoalSpinner.setAdapter(goalAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this,
                R.array.regions_array, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionAdapter);
    }

    private void openWorkoutList() {
        Intent intent = new Intent(this, WorkoutListActivity.class);
        startActivityForResult(intent, REQUEST_CODE_WORKOUT_LIST);
    }

    private void openLocationPicker() {
        Intent setLocationIntent = new Intent(getApplicationContext(), set_location.class);
        startActivityForResult(setLocationIntent, REQUEST_CODE_SET_LOCATION);
    }

    private void loadGymLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat(KEY_LATITUDE, 0);
        float longitude = sharedPreferences.getFloat(KEY_LONGITUDE, 0);

        if (latitude != 0 && longitude != 0) {
            gymLocation.setText(String.format("위도: %s, 경도: %s", latitude, longitude));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle location selection
        if (requestCode == REQUEST_CODE_SET_LOCATION && resultCode == RESULT_OK) {
            if (data != null) {
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                saveGymLocation(latitude, longitude);
                gymLocation.setText(String.format("위도: %s, 경도: %s", latitude, longitude));
                gymLocationBtn.setText("등록 완료");
                showToast("위치가 설정되었습니다: \n" + gymLocation.getText());
            }
        }

        if (requestCode == REQUEST_CODE_WORKOUT_LIST && resultCode == RESULT_OK && data != null) {
            ArrayList<String> selectedExercises = data.getStringArrayListExtra("selectedExercises");
            if (selectedExercises != null) {
                // Get existing text and append new selections
                String existingExercises = favWorkouts.getText().toString();
                StringBuilder sb = new StringBuilder(existingExercises);

                for (String exercise : selectedExercises) {
                    if (!existingExercises.contains(exercise)) {
                        sb.append(exercise).append(", ");
                    }
                }

                // Remove the last comma and space
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 2);
                }

                favWorkouts.setText(sb.toString());
            }
        }
    }


    private void saveGymLocation(double latitude, double longitude) {
        SharedPreferences sharedPreferences = getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(KEY_LATITUDE, (float) latitude);
        editor.putFloat(KEY_LONGITUDE, (float) longitude);
        editor.apply();
    }

    // 갤러리를 여는 메서드
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);  // 갤러리 실행
    }

    private void updateProfile() {
        String selectedGoalKorean = workoutGoalSpinner.getSelectedItem().toString();
        String selectedGoal = goalMapping.get(selectedGoalKorean);

        String workoutExperience = workoutExperienceInput.getText().toString();
        String introduction = introductionInput.getText().toString();

        // favWorkoutsInput에서 입력받은 문자열을 쉼표로 나눠서 List로 변환
        String favWorkoutsInputString = favWorkouts.getText().toString();
        List<String> selectedExercises = Arrays.asList(favWorkoutsInputString.split("\\s*,\\s*"));

        // Map selected exercises to their corresponding enum values
        List<String> mappedExercises = new ArrayList<>();
        for (String exercise : selectedExercises) {
            String mappedExercise = exerciseMapping.get(exercise);
            if (mappedExercise != null) {
                mappedExercises.add(mappedExercise);
            }
        }

        int selectedRadioButtonId = profileVisibleRadioGroup.getCheckedRadioButtonId();
        int selectedRegionIndex = regionSpinner.getSelectedItemPosition();
        String selectedRegion = locationMappings[selectedRegionIndex];
        boolean isProfileVisible = profileVisibleRadioGroup.getCheckedRadioButtonId() == R.id.radioYes;

        SharedPreferences sharedPreferences = getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat(KEY_LATITUDE, 0);
        float longitude = sharedPreferences.getFloat(KEY_LONGITUDE, 0);

        // 필수 값 검증
        if (selectedGoal.isEmpty() || workoutExperience.isEmpty() || introduction.isEmpty() ||
                mappedExercises.isEmpty() || latitude == 0 || longitude == 0 || selectedRegion.isEmpty()) {
            showToast("모든 필수 정보를 입력하세요.");
            return;
        }

        if (selectedRadioButtonId == -1) {
            showToast("프로필 공개 여부를 선택하세요.");
            return;
        }

        GymLocation gymLocation = new GymLocation(latitude, longitude, selectedRegion);
        EditUserProfile profile = new EditUserProfile(
                selectedGoal,
                Integer.parseInt(workoutExperience),
                introduction,
                gymLocation,
                isProfileVisible,
                mappedExercises  // Use the mapped exercises
        );

        MultipartBody.Part imagePart = createImagePart();
        RequestBody profileRequest = createProfileRequest(profile);

        updateProfileApiCall(profileRequest, imagePart);
    }

    private MultipartBody.Part createImagePart() {
        if (uri != null) {
            String path = getRealPathFromURI(uri);
            Log.d("ImagePart", "Image URI: " + uri.toString());
            if (path != null) {
                File file = new File(path);

                Log.d("ImagePart", "Image Path: " + path);
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                } else {
                    Log.e("FileError", "File not found: " + path);
                    showToast("이미지 파일을 찾을 수 없습니다.");
                }
            } else {
                Log.e("FileError", "Unable to get path from URI: " + uri);
                showToast("이미지 경로를 가져올 수 없습니다.");
            }
        }

        // 이미지가 없을 경우 기본 이미지 사용
        Log.d("ImagePart", "Image URI is null, using default image.");
        return createDefaultImagePart();
    }

    // 기본 이미지를 사용하여 MultipartBody.Part 생성
    private MultipartBody.Part createDefaultImagePart() {
        // 기본 이미지인 default_profile_image를 불러옴
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_image);
        if (bitmap != null) {
            // 비트맵을 JPEG 파일로 변환
            File defaultImageFile = new File(getCacheDir(), "default_profile_image.jpg");
            try (FileOutputStream fos = new FileOutputStream(defaultImageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                Log.e("FileError", "Error creating default image file: " + e.getMessage());
                return null;
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), defaultImageFile);
            return MultipartBody.Part.createFormData("file", defaultImageFile.getName(), requestFile);
        } else {
            Log.e("ImageError", "Default image not found.");
            return null;
        }
    }

    private RequestBody createProfileRequest(EditUserProfile profile) {
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        return RequestBody.create(MediaType.parse("application/json"), json);
    }

    private void updateProfileApiCall(RequestBody profileRequest, MultipartBody.Part imagePart) {
        // SharedPreferences에서 profileId 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("localID", MODE_PRIVATE);
        int profileId = sharedPreferences.getInt("profileId", -1);  // 기본값으로 -1 설정

        if (profileId == -1) {
            Log.e("ProfileUpdate", "Profile ID not found in SharedPreferences."); // profileId가 없을 경우 로그 추가
            showToast("프로필 ID를 찾을 수 없습니다.");
            return; // profileId가 없으면 메서드 종료
        }

        ApiService profileService = RetrofitClient.getApiService();

        Log.d("ProfileUpdate", "Profile Request: " + profileRequest.toString()); // 프로필 요청 로그 추가
        if (imagePart != null) {
            Log.d("ProfileUpdate", "Image Part: " + imagePart.toString()); // 이미지 파일 로그 추가
        } else {
            Log.d("ProfileUpdate", "Image Part is null"); // 이미지 파일이 없을 때 로그 추가
        }

        Call<EditProfileResponse> call = profileService.updateProfile(profileId, imagePart, profileRequest);

        Log.d("ProfileUpdate", "Profile Request URL: " + call.request().url());
        Log.d("ProfileUpdate", "Profile Request Body: " + profileRequest.toString());

        call.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("ProfileUpdate", "Profile update success: " + response.body()); // 성공 로그 추가
                    showToast("프로필이 업데이트되었습니다.");
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("ProfileUpdate", "Profile update failed: " + response.code()); // 실패 로그 추가
                    showToast("업데이트 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                Log.e("ProfileUpdate", "Network error: " + t.getMessage()); // 네트워크 오류 로그 추가
                showToast("네트워크 오류: " + t.getMessage());
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast("권한이 승인되었습니다.");
            } else {
                showToast("권한이 거부되었습니다. 이 기능을 사용하려면 권한이 필요합니다.");
                // 로그캣에 오류 메시지 출력
                Log.e("PermissionError", "권한이 거부되었습니다. 권한이 없으면 이 기능을 사용할 수 없습니다.");
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String path = null;

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }

        return path;
    }

    private void requestPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // 필요한 권한 요청
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            // 이미 권한이 있는 경우
            showToast("이미 권한이 승인되었습니다.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}