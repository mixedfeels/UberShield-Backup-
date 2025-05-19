plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "br.fecap.pi.ubershield"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.fecap.pi.ubershield"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.volley)

    //glr essa é a dependencia http do servidor, sem isso o banco n funciona, podem estranhar mas n tira nmral
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dependencias do Mapa
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation("com.google.maps.android:android-maps-utils:2.2.3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    implementation("com.google.android.libraries.places:places:2.7.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation ("androidx.biometric:biometric:1.1.0")

    // Animação
    implementation("com.airbnb.android:lottie:6.1.0")

    // Imagem
    implementation("de.hdodenhof:circleimageview:3.1.0")
}
