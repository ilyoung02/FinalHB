package com.example.healthbuddypro.Matching;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.healthbuddypro.ApiService;
import com.example.healthbuddypro.R;
import com.example.healthbuddypro.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePagerAdapter extends RecyclerView.Adapter<ProfilePagerAdapter.ProfileViewHolder> {

    private final List<UserProfile> profiles;
    private final OnProfileClickListener listener;

    public ProfilePagerAdapter(List<UserProfile> profiles, OnProfileClickListener listener) {
        this.profiles = profiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        UserProfile profile = profiles.get(position);

        // 프로필 데이터 바인딩
        holder.nicknameTextView.setText(profile.getNickName());
        holder.genderTextView.setText(profile.getGender());
        holder.ageTextView.setText(String.valueOf(profile.getAge()));

        // 좋아요 버튼 클릭 리스너 설정
        holder.btnLike.setOnClickListener(v -> {
            sendLikeRequest(profile.getProfileId(), holder.itemView.getContext());  // 좋아요 요청 보내기
        });

        // 이미지 로딩 (Glide 사용) 및 기본 이미지 설정
        Glide.with(holder.itemView.getContext())
                .load(profile.getImageUrl())
                .placeholder(R.drawable.default_user_img) // 기본 이미지
                .into(holder.profileImageView);

        // 프로필 항목 클릭 리스너
        holder.itemView.setOnClickListener(v -> listener.onProfileClick(profile));
    }

    // 좋아요 요청 메서드
    private void sendLikeRequest(int profileId, Context context) {
        ApiService apiService = RetrofitClient.getApiService();

        // Retrofit으로 백엔드 요청
        Call<LikeResponse> call = apiService.likeProfile(profileId);
        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response.isSuccessful()) {
                    // 성공적으로 좋아요 처리 시 사용자에게 알림
                    Toast.makeText(context, "좋아요 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "좋아요 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                Toast.makeText(context, "오류 발생: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    // ViewHolder 클래스
    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nicknameTextView, genderTextView, ageTextView;
        Button btnLike;

        // 데이터 바인딩
        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_profile.xml 파일 참고
            profileImageView = itemView.findViewById(R.id.profile_image);
            nicknameTextView = itemView.findViewById(R.id.profile_name);
            genderTextView = itemView.findViewById(R.id.profile_gender);
            ageTextView = itemView.findViewById(R.id.profile_age);

            btnLike = itemView.findViewById(R.id.btnLike);  // 좋아요 버튼 추가
        }
    }

    // 클릭 리스너 인터페이스
    public interface OnProfileClickListener {
        void onProfileClick(UserProfile profile);
    }
}
