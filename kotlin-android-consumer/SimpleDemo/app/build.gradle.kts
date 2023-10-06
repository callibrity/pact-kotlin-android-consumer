plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("au.com.dius.pact")
}

android {
    namespace = "com.example.simpledemo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.simpledemo"
        minSdk = 24
        targetSdk = 33
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.beust:klaxon:5.6")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.squareup.retrofit2:converter-gson:VERSION")


    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0'")
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.4.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:3.12.1")
//    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
//    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-experimental-adapter:1.0.0")

//MockWebserver
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")

    // #Pact
//    testImplementation("au.com.dius.pact.consumer:junit5:4.1.9")
    testImplementation("au.com.dius.pact.consumer:java8:4.1.9")
    testImplementation("au.com.dius.pact.consumer:junit:4.4.4")
    testImplementation("au.com.dius:pact-jvm-provider-gradle_2.11:3.5.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

//    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
//    testImplementation("org.assertj:assertj-core:3.9.1")
//    testImplementation("au.com.dius.pact.consumer:junit5:4.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
pact {
    publish {
        pactBrokerUrl = "http://localhost:9292"
        pactBrokerUsername = "pact_workshop"
        pactBrokerPassword = "pact_workshop"
        version = "3.5.29"
    }
}
