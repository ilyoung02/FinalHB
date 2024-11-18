package com.example.healthbuddypro.Community;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthbuddypro.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private EditText etPostContent;
    private Button btnPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        // Toolbar 설정
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("커뮤니티");
            }
        }

        etPostContent = view.findViewById(R.id.etPostContent);
        btnPost = view.findViewById(R.id.btnPost);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        // 예시 데이터 추가
        postList.add(new Post("사용자1", "10분 전", "첫 번째 게시물 내용입니다."));
        postList.add(new Post("사용자2", "20분 전", "두 번째 게시물 내용입니다."));
        postList.add(new Post("사용자3", "30분 전", "세 번째 게시물 내용입니다."));

        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etPostContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    // 새로운 게시물 추가
                    postList.add(0, new Post("새 사용자", "방금 전", content));
                    postAdapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);
                    etPostContent.setText(""); // 입력 필드 초기화
                }
            }
        });

        return view;
    }
}
