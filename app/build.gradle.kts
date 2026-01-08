plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "ru.vsu.task1"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.vsu.task1"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildToolsVersion = "36.1.0"

}

dependencies {
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.navigation.compose)
    val composeBom = platform("androidx.compose:compose-bom:2025.09.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation(libs.androidx.material3)
    // or skip Material Design and build directly on top of foundational components
    implementation(libs.androidx.foundation)
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation(libs.androidx.ui)

    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // UI Tests
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Optional - Add window size utils
    implementation(libs.androidx.adaptive)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.androidx.runtime.livedata)
    // Optional - Integration with RxJava
    implementation(libs.androidx.runtime.rxjava2)

    // Graphics
    implementation(libs.vico.compose)

    // Images from internet
    implementation(libs.coil.compose)

    implementation(libs.retrofit)
// Конвертер для Kotlinx Serialization (рекомендуется)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json) // Убедитесь, что версия актуальна
    implementation(libs.converter.gson)

//    implementation("co.yml:ycharts:2.1.0")

// OkHttp для логгирования запросов (очень полезно для отладки)
    implementation(libs.logging.interceptor)

// ViewModel для Jetpack Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose.v280)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.navigation)

    // time patterns
    implementation(libs.threetenabp)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}