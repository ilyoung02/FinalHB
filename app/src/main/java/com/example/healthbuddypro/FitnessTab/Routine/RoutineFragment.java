// RoutineFragment 파일 -> 그날 루틴 수행 완료여부 내부저장소로해서 프래그먼트 변경되는거 추가한 부분임 마지막에 추가하자
package com.example.healthbuddypro.FitnessTab.Routine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineFragment extends Fragment {

    private int routineId;
    private int userId;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        sharedPreferences = requireActivity().getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        userId = getUserIdFromStorage();
        routineId = getRoutineIdFromStorage();

        // 오늘 요일에 해당하는 루틴 정보 조회
        checkTodayRoutine(routineId);

        return view;
    }

    // 오늘 요일을 가져와 /api/routines/{routineId}/details?day= 요청을 통해 루틴 정보를 확인하고, Fragment를 전환하는 메서드
    private void checkTodayRoutine(int routineId) {
        if (routineId == -1) {
            replaceFragment(new GroupLockedFragment()); // 루틴 ID가 없으면 GroupLockedFragment 표시
            return;
        }

        // 루틴 완료 여부를 내부 저장소에서 확인
        if (isRoutineCompleted()) {
            replaceFragment(new RoutineCompletedFragment());
            return;
        }

        String todayDay = getTodayDay();
        ApiService apiService = RetrofitClient.getApiService();

        // 1. 오늘의 루틴 정보 확인
        Call<RoutineDetailsResponse> call = apiService.getRoutineDetails(routineId, todayDay);
        call.enqueue(new Callback<RoutineDetailsResponse>() {
            @Override
            public void onResponse(Call<RoutineDetailsResponse> call, Response<RoutineDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    replaceFragment(new RoutineStartFragment()); // 루틴 정보가 있으면 루틴 시작 화면 표시
                } else {
                    // 루틴이 없으면 NoRoutineFragment 표시
                    replaceFragment(new NoRoutineFragment());
                }
            }
            @Override
            public void onFailure(Call<RoutineDetailsResponse> call, Throwable t) {
                Log.e("RoutineFragment", "Error fetching today's routine", t);
                replaceFragment(new ErrorFragment()); // 오류 발생 시 ErrorFragment 표시
            }
        });
    }

    // 루틴 수행 완료 상태를 저장하는 메서드
    public void saveRoutineCompletionStatus() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRoutineComplete_" + userId, true);
        editor.putString("completionDate", getTodayDateString());
        editor.apply();
    }

    // 루틴 완료 여부를 확인하는 메서드
    private boolean isRoutineCompleted() {
        String TAG = "루틴 완료 여부";
        Log.d(TAG, "isRoutineCompleted: Checking routine completion status");

        SharedPreferences routinePrefs = requireActivity().getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        String lastCompletionDay = routinePrefs.getString("routineCompleteDay_" + userId, ""); // 마지막 완료된 요일
        boolean isComplete = routinePrefs.getBoolean("isRoutineComplete_" + userId, false); // 완료 여부

        String todayDay = getTodayDay(); // 오늘의 요일

        Log.d(TAG, "isRoutineCompleted: lastCompletionDay = " + lastCompletionDay + ", todayDay = " + todayDay + ", isComplete = " + isComplete);

        // 오늘의 요일과 저장된 완료된 요일을 비교
        if (!lastCompletionDay.equals(todayDay)) {
            Log.d(TAG, "isRoutineCompleted: Completion day mismatch, resetting completion status");
            clearRoutineCompletionStatus(); // 요일이 다르면 완료 상태 초기화
            return false;
        }

        return isComplete;
    }



    // 루틴 완료 상태 초기화
    private void clearRoutineCompletionStatus() {
        Log.d("루틴 완료 여부 초기화", "루틴 완료 여부 초기화: Clearing routine completion status");
        SharedPreferences routinePrefs = requireActivity().getSharedPreferences("RoutinePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = routinePrefs.edit();
        editor.remove("isRoutineComplete_" + userId); // 완료 여부 초기화
        editor.remove("routineCompleteDay_" + userId); // 완료된 날짜 초기화
        editor.apply();
    }


    // 오늘의 날짜 문자열을 반환하는 메서드 (yyyyMMdd 형식)
    private String getTodayDateString() {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
    }

    // 오늘의 요일을 대문자 형식으로 반환하는 메서드 (예: "MON")
    private String getTodayDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E", Locale.getDefault());
        return dateFormat.format(new Date()).toUpperCase();
    }

    // 내부 저장소에서 routineId를 가져오는 메서드
    private int getRoutineIdFromStorage() {
        Context context = getActivity();
        if (context != null && userId != -1) {
            return context.getSharedPreferences("routineId" + userId + "_prefs", Context.MODE_PRIVATE)
                    .getInt("routineId", -1);
        }
        return -1;
    }

    // userId를 내부 저장소에서 가져오는 메서드
    private int getUserIdFromStorage() {
        Context context = getActivity();
        if (context != null) {
            return context.getSharedPreferences("localID", Context.MODE_PRIVATE)
                    .getInt("userId", -1);
        }
        return -1;
    }

    // Fragment를 동적으로 교체하는 메서드
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
