package com.example.healthbuddypro.Gym;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.healthbuddypro.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class GymFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PlacesClient placesClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gym, container, false);

        // Places API 초기화
        Places.initialize(requireContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireContext());

        // Google Maps 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            searchNearbyGyms();
        } else {
            requestLocationPermission();
        }

        // 강제로 헬스장 마커 추가
        addManualGymMarkers();
    }

    // 강제로 헬스장 마커를 추가하는 메서드
    private void addManualGymMarkers() {
        // 대구 서구에 있는 헬스장 예시 좌표
        LatLng gym1 = new LatLng(35.870563, 128.558995); // 대구 서구의 예시 좌표
        mMap.addMarker(new MarkerOptions()
                .position(gym1)
                .title("블랙짐")
                .snippet("https://blog.naver.com/alsgh9226") // URL을 snippet에 추가
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        LatLng gym2 = new LatLng(35.870309, 128.548584); // 또 다른 헬스장 좌표
        mMap.addMarker(new MarkerOptions()
                .position(gym2)
                .title("디짐")
                .snippet("https://blog.naver.com/deegym") // URL 추가
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        LatLng gym3 = new LatLng(35.872467, 128.563219); // 추가 예시
        mMap.addMarker(new MarkerOptions()
                .position(gym3)
                .title("짐 팩토")
                .snippet("https://blog.naver.com/gymfacto") // URL 추가
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        LatLng gym5 = new LatLng(35.867862, 128.555915); // 추가 예시
        mMap.addMarker(new MarkerOptions()
                .position(gym5)
                .title("누난 피트니스")
                .snippet("https://www.nunangirl.com/") // URL 추가
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        LatLng gym6 = new LatLng(35.866182, 128.556178); // 추가 예시
        mMap.addMarker(new MarkerOptions()
                .position(gym6)
                .title("다힘 휘트니스")
                .snippet("https://blog.naver.com/mloveslove") // URL 추가
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // 지도에 추가한 마커들에 대해 카메라 위치 설정
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gym1, 12));

        // InfoWindow 클릭 리스너 설정
        mMap.setOnInfoWindowClickListener(marker -> {
            // 정보 창 클릭 시 웹 페이지로 이동
            String url = marker.getSnippet(); // URL이 snippet에 포함되었다고 가정
            if (url != null && !url.isEmpty()) {
                openUrlInBrowser(url); // 웹 브라우저에서 열기
            }
        });
    }

    // URL을 브라우저에서 여는 메서드
    private void openUrlInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    // 위치 권한 확인 및 사용자 위치 활성화
    private void enableUserLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                requestLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("GymFragment", "SecurityException: " + e.getMessage());
        }
    }

    // 위치 권한 요청 메서드
    private void requestLocationPermission() {
        // 권한 요청 이유를 보여줄 필요가 있으면 설명
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.d("GymFragment", "위치 권한이 필요합니다.");
            // 사용자가 거부한 적이 있는 경우 다시 설명하고 요청
        }

        // 권한 요청
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                searchNearbyGyms();
            } else {
                // 권한이 거부된 경우
                Log.e("GymFragment", "위치 권한이 거부되었습니다.");
            }
        }
    }

    // 주변 헬스장 검색 메서드
    private void searchNearbyGyms() {
        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.BUSINESS_STATUS);
                FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

                placesClient.findCurrentPlace(request).addOnSuccessListener(response -> {
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place place = placeLikelihood.getPlace();

                        Log.d("PlacesAPI", "Place: " + place.getName() + ", Business Status: " + place.getBusinessStatus());
                        // 각 장소의 비즈니스 상태를 로그로 출력하여 어떤 비즈니스 상태인지 확인

                        // 헬스장 필터링
                        if (place.getBusinessStatus() != null && place.getBusinessStatus().equals(Place.BusinessStatus.OPERATIONAL)) {
                            LatLng gymLatLng = place.getLatLng();
                            if (gymLatLng != null) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(gymLatLng)
                                        .title(place.getName())
                                        .snippet(place.getAddress()));
                            }
                        }
                    }
                }).addOnFailureListener(exception -> Log.e("PlacesAPI", "Place not found: " + exception.getMessage()));
            } else {
                requestLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("GymFragment", "SecurityException during search: " + e.getMessage());
        }
    }
}