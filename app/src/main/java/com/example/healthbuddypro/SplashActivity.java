package com.example.healthbuddypro;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddypro.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        VideoView videoView = findViewById(R.id.splash_video_view);

        // 동영상 파일의 URI 설정
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo);
        videoView.setVideoURI(videoUri);

        // 동영상 재생
        videoView.start();

        // 동영상이 끝나면 메인 액티비티로 전환
        videoView.setOnCompletionListener(mp -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // 스플래시 액티비티를 종료
        });

        // 동영상 재생 중 오류가 발생하면 메인 액티비티로 전환
        videoView.setOnErrorListener((mp, what, extra) -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // 스플래시 액티비티를 종료
            return true;
        });
    }
}
