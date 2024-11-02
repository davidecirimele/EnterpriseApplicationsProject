plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
}

android {
    namespace = "com.example.ecommercefront_end"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecommercefront_end"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // JSON e Logging
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.logging.interceptor)
    implementation(libs.gson.v2101)

    // Ktor client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)

    // UI e immagini
    implementation(libs.coil.compose.v240)
    implementation(libs.coil.v222)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // AndroidX Core e Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Material Design e Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material) // Mantieni Material 2 per BottomNavigation
    implementation(libs.androidx.material3.v111) // Material 3
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Navigation e sicurezza
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.security.crypto)
    implementation(libs.jwtdecode)

    // Firebase e altre librerie Google
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.places)

    // Room e persistenza dei dati
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit per networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Altre dipendenze
    implementation(libs.volley)
    implementation(libs.accompanist.permissions)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material (Material 2)
    implementation(libs.androidx.material.v175)

// Material3 (Material You)
    implementation(libs.androidx.material3.v110)

}


kotlin {
    jvmToolchain(21)
}