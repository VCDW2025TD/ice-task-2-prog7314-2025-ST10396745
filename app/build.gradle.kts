plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.memestreamproto"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.memestreamproto"
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
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.activity)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    implementation("com.squareup.retrofit2:retrofit:3.1.0-SNAPSHOT")
    implementation ("com.squareup.moshi:moshi-kotlin:1.14.0")
//    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    //Gson
//    implementation ("com.google.code.gson:gson:2.13.1")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.github.bumptech.glide:gifdecoder:4.16.0")
//    kapt ("com.github.bumptech.glide:compiler:4.16.0")


    // Import the Firebase BoM
//    implementation(platform("com.google.firebase:firebase-bom:34.1.0"))

    // Import the Firebase BoM
        implementation(platform("com.google.firebase:firebase-bom:33.13.0"))



    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Auth
    implementation("com.google.firebase:firebase-auth-ktx")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")


    //Added - Credential Manager (Google Sign In Instructions)
    implementation("androidx.credentials:credentials:1.6.0-alpha04")
    implementation("androidx.credentials:credentials-play-services-auth:1.6.0-alpha04")

    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    //added
    implementation ("com.google.android.libraries.places:places:2.5.0")


    //added - photo overlaying and stuff
    implementation ("com.burhanrashid52:photoeditor:3.0.2")


}