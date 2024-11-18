package com.example.healthbuddypro;

import android.content.Context;

import java.net.CookieManager;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://165.229.89.154:8080/";

    public static ApiService getApiService() {
        if (retrofit == null) {
            // 네트워크 로깅을 위해 OkHttp 클라이언트 설정
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp 클라이언트에 쿠키 매니저와 로깅 연결
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging) // 로그 인터셉터
                    .cookieJar(new JavaNetCookieJar(new CookieManager())) // 쿠키 관리 추가
                    .build();

            // Retrofit 빌드
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)  // OkHttp 클라이언트 적용
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

}
