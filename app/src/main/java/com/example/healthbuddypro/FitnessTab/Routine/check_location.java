package com.example.healthbuddypro.FitnessTab.Routine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.MainActivity;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class check_location extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton backButton;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private LatLng targetLatLng;
    private final float RADIUS_IN_METERS = 400; // 지오펜스 반경
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_location);

        backButton = findViewById(R.id.backButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        backButton.setOnClickListener(view -> finish());

        // 사용자 위치 정보를 서버에서 불러옴
        fetchGymLocationFromServer();

        // 맵 프래그먼트 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.currentLocation);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 위치 인증 버튼
        Button completeLocationButton = findViewById(R.id.btn_currentLocation_complete);
        completeLocationButton.setOnClickListener(v -> checkLocation());

        getCurrentLocation();
    }

    // 서버에서 헬스장 위치를 불러오는 메서드
//    private void fetchGymLocationFromServer() {
//        // 내부 저장소에서 userId 가져오기
//        SharedPreferences sharedPreferences = getSharedPreferences("localID", Context.MODE_PRIVATE);
//        int userId = sharedPreferences.getInt("userId", -1);
//
//        if (userId == -1) {
//            Toast.makeText(this, "사용자 ID를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Retrofit API 호출
//        ApiService apiService = RetrofitClient.getApiService();
//        Call<GymLocationResponse> call = apiService.getGymLocation(userId);
//
//        call.enqueue(new Callback<GymLocationResponse>() {
//            @Override
//            public void onResponse(Call<GymLocationResponse> call, Response<GymLocationResponse> response) {
//                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
//                    GymLocationResponse.LocationData locationData = response.body().getData();
//                    targetLatLng = new LatLng(locationData.getLatitude(), locationData.getLongitude());
//                    Log.d("헬스장 등록한 위경도", "위도경도 : " + targetLatLng);
//                } else {
//                    Toast.makeText(check_location.this, "헬스장 위치 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GymLocationResponse> call, Throwable t) {
//                Toast.makeText(check_location.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // memo : 수정 전 onMapReady
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//            return;
//        }
//
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//
//        // 헬스장 위치 마커와 반경
//        if (targetLatLng != null) {
//            mMap.addMarker(new MarkerOptions().position(targetLatLng).title("설정된 헬스장 위치"));
//            mMap.addCircle(new CircleOptions()
//                    .center(targetLatLng)
//                    .radius(RADIUS_IN_METERS)
//                    .strokeColor(0x220000FF)
//                    .fillColor(0x220000FF));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 14));
//        }
//    }

    // 서버에서 헬스장 위치를 불러오는 메서드
    private void fetchGymLocationFromServer() {
        SharedPreferences sharedPreferences = getSharedPreferences("localID", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "사용자 ID를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<GymLocationResponse> call = apiService.getGymLocation(userId);

        call.enqueue(new Callback<GymLocationResponse>() {
            @Override
            public void onResponse(Call<GymLocationResponse> call, Response<GymLocationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    GymLocationResponse.LocationData locationData = response.body().getData();
                    targetLatLng = new LatLng(locationData.getLatitude(), locationData.getLongitude());
                    Log.d("헬스장 등록한 위경도", "위도경도 : " + targetLatLng);

                    // 맵 업데이트
                    if (mMap != null) {
                        updateMapWithGymLocation();
                    }
                } else {
                    Toast.makeText(check_location.this, "헬스장 위치 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GymLocationResponse> call, Throwable t) {
                Toast.makeText(check_location.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 헬스장 위치와 반경을 맵에 추가하는 메서드
    private void updateMapWithGymLocation() {
        if (targetLatLng != null) {
            mMap.clear(); // 기존 마커와 반경 제거
            mMap.addMarker(new MarkerOptions().position(targetLatLng).title("설정된 헬스장 위치"));
            mMap.addCircle(new CircleOptions()
                    .center(targetLatLng)
                    .radius(RADIUS_IN_METERS)
                    .strokeColor(0x220000FF) // 파란색 테두리
                    .fillColor(0x220000FF)); // 파란색 반경
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLatLng, 14));
        }
    }


    // 수정 후 onMapReady
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // targetLatLng가 이미 로드된 경우 맵 업데이트
        if (targetLatLng != null) {
            updateMapWithGymLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14));
                } else {
                    Toast.makeText(check_location.this, "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(this, location -> {
                if (location != null && targetLatLng != null) {
                    float[] distance = new float[2];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                            targetLatLng.latitude, targetLatLng.longitude, distance);
                    if (distance[0] <= RADIUS_IN_METERS) {
                        Toast.makeText(check_location.this, "⭐위치인증 성공!! 운동을 시작합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), group_start02.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(check_location.this, "⛔위치인증 실패: 지정한 헬스장으로 이동 후 다시 인증해주세요.", Toast.LENGTH_SHORT).show();
                        Log.d("위치인증", "위치인증 실패 @위치인증 실패 @위치인증 실패 @위치인증 실패 @위치인증 실패  ");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(check_location.this, "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocation();
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
