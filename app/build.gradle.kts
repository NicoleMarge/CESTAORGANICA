plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // SERIALIZATION
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"

    // ROOM
    kotlin("kapt")
}

android {

    namespace = "com.example.huertohogardefinitiveedition"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.huertohogardefinitiveedition"

        minSdk = 24

        targetSdk = 36

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    kotlinOptions {

        jvmTarget = "11"
    }

    buildFeatures {

        compose = true
    }
}

dependencies {

    // =========================
    // ANDROID CORE
    // =========================

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)

    implementation(
        platform(libs.androidx.compose.bom)
    )

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.ui.graphics)

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )

    implementation(libs.androidx.compose.material3)

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7"
    )

    implementation(
        "androidx.compose.runtime:runtime-livedata:1.5.4"
    )

    // =========================
    // NAVIGATION
    // =========================

    implementation(
        "androidx.navigation:navigation-compose:2.7.7"
    )

    // =========================
    // ICONS
    // =========================

    implementation(
        "androidx.compose.material:material-icons-core"
    )

    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    // =========================
    // ROOM DATABASE
    // =========================

    implementation(
        "androidx.room:room-runtime:2.6.1"
    )

    implementation(
        "androidx.room:room-ktx:2.6.1"
    )

    kapt(
        "androidx.room:room-compiler:2.6.1"
    )

    // =========================
    // CAMERAX
    // =========================

    val camerax_version = "1.3.3"

    implementation(
        "androidx.camera:camera-core:$camerax_version"
    )

    implementation(
        "androidx.camera:camera-camera2:$camerax_version"
    )

    implementation(
        "androidx.camera:camera-lifecycle:$camerax_version"
    )

    implementation(
        "androidx.camera:camera-view:$camerax_version"
    )

    // =========================
    // QR
    // =========================

    implementation(
        "com.google.zxing:core:3.5.3"
    )

    implementation(
        "com.journeyapps:zxing-android-embedded:4.3.0"
    )

    implementation(
        "com.google.mlkit:barcode-scanning:17.2.0"
    )

    // =========================
    // SUPABASE
    // =========================

    implementation(
        "io.github.jan-tennert.supabase:postgrest-kt:2.5.4"
    )

    implementation(
        "io.github.jan-tennert.supabase:realtime-kt:2.5.4"
    )

    implementation(
        "io.github.jan-tennert.supabase:gotrue-kt:2.5.4"
    )

    implementation(
        "io.ktor:ktor-client-okhttp:2.3.12"
    )

    implementation(
        "io.ktor:ktor-client-android:2.3.12"
    )

    implementation(
        "io.ktor:ktor-client-content-negotiation:2.3.12"
    )

    implementation(
        "io.ktor:ktor-serialization-kotlinx-json:2.3.12"
    )

    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
    )

    // =========================
    // TESTS
    // =========================

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )

    debugImplementation(
        libs.androidx.compose.ui.test.manifest
    )
}