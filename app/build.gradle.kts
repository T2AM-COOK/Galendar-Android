plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    // viewBinding 사용
    buildFeatures {
        viewBinding = true
    }
}

android {
    namespace = "com.example.galendar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.galendar"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // 큰따옴표로 변경
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // 큰따옴표로 변경

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")//코루틴


}