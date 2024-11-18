plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.healthbuddypro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healthbuddypro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 구글맵 + placeAPI
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-places:17.1.0")
    implementation ("com.google.android.libraries.places:places:3.5.0")

    implementation("com.google.android.gms:play-services-auth:21.2.0")

    //Retrofit 의존성 설정
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // 이미지 업로드 glide 설정
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.firebase.firestore)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // 파이어베이스 의존성 추가
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.9.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}