plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.galendar"
    compileSdk = 34

    buildFeatures {
        viewBinding = true // View Binding 활성화
        dataBinding = true // Data Binding 활성화
    }

    defaultConfig {
        applicationId = "com.example.galendar"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // 아래 코드를 추가하세요
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2" // Use the latest version if available
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
}

// repositories 블록을 android 블록 외부로 이동
repositories {
    google()
    mavenCentral()
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

    implementation ("androidx.compose.runtime:runtime:1.5.2")
    implementation ("androidx.compose.compiler:compiler:1.5.2")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    //viewmodel
    val lifecycle_version = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9") // 코루틴

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5") // 최신 버전으로 업데이트 가능

    implementation ("com.github.prolificinteractive:material-calendarview:2.0.1") //
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")


    implementation ("com.google.android.material:material:1.12.0")

    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5" )// 버전은 프로젝트에 맞게 조정
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5" )// 버전은 프로젝트에 맞게 조정




}
