plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.obpiub.runculter"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.obpiub.runculter"
        minSdk = 26  // Обновлено для совместимости с Яндекс картами (требуют minSdk 26)
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    
    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    
    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    
    // Yandex Maps
    // Облегченная версия (карта, пробки, геолокация, офлайн-карты)
    implementation("com.yandex.android:maps.mobile:4.29.0-lite")
    // Полная версия (включает маршрутизацию, поиск, геокодирование, панорамы)
    // implementation("com.yandex.android:maps.mobile:4.29.0-full")
    
    // Activity & Fragment
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    
    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    
    // Gson
    implementation(libs.gson)
    
    // RecyclerView
    implementation(libs.recyclerview)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}