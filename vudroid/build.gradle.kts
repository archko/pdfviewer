plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "org.vudroid"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        encoding = "UTF-8"
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
dependencies {
    //api("com.artifex.mupdf:mupdf-fitz:1.25.0")
    api(libs.androidx.core.ktx)
    api("io.legere:pdfiumandroid:1.0.24")
}