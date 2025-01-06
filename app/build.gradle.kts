plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = "35.0.0"

    namespace = "com.archko.pdfviewer"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "com.archko.pdfviewer"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
        multiDexEnabled = true
        ndk {
            abiFilters += listOf("arm64-v8a")
        }

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        named("debug") {
            storeFile = rootProject.file("key.jks")
            storePassword = ""
            keyAlias = ""
            keyPassword = ""
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    val abiCodes = mapOf("arm64-v8a" to 1)
    android.applicationVariants.all {
        val buildType = this.buildType.name
        val flavorName = this.flavorName
        val variant = this
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                //修改apk名称
                if (buildType == "release") {
                    this.outputFileName = "pdfviewer-${variant.versionName}.apk"
                } else if (buildType == "debug") {
                    this.outputFileName = "pdfviewer-${buildType}_${variant.versionName}.apk"
                }
            }
        }
    }
}

dependencies {
    api(libs.androidx.appcompat) {
        exclude(group = "androidx.recyclerview", module = "recyclerview")
    }

    implementation(project(":vudroid"))
}
