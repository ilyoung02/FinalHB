package com.example.healthbuddypro.Community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthbuddypro.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;
    private final Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvUsername.setText(post.getUsername());
        holder.tvPostTime.setText(post.getPostTime());
        holder.tvPostContent.setText(post.getContent());
        // 프로필 이미지 및 좋아요/댓글 버튼 설정 (생략 가능)
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProfile;
        TextView tvUsername, tvPostTime, tvPostContent;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
        }
    }
}
