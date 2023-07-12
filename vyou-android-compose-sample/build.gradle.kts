@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlinx-serialization")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.vyou.android.compose.sample"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val vyouConfiguration = loadVYouConfiguration()

        resValue("string", "vyou_server_url", "${vyouConfiguration["vyou_server_url"]}")
        resValue("string", "vyou_client_id", "${vyouConfiguration["vyou_client_id"]}")
        resValue("string", "vyou_public_salt", "${vyouConfiguration["vyou_public_salt"]}")
        resValue("string", "vyou_client_uri_scheme", "${vyouConfiguration["vyou_client_uri_scheme"]}")
        resValue("string", "vyou_client_uri_host", "${vyouConfiguration["vyou_client_uri_host"]}")
        resValue("string", "vyou_client_uri_path_pattern", "${vyouConfiguration["vyou_client_uri_path_pattern"]}")
        resValue("string", "vyou_google_server_id", "${vyouConfiguration["vyou_google_server_id"]}")
        resValue("string", "vyou_facebook_client_id", "${vyouConfiguration["vyou_facebook_client_id"]}")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(libs.lifecycle.runtime.compose)
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.activity.compose)
    implementation(libs.compose.compiler)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.navigation)
    implementation(libs.compose.ui.tooling)

    implementation(libs.kotlinx.serialization)

    implementation(libs.koin.compose)

    implementation(libs.play.services.auth)
    implementation(libs.facebook.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.junit.ktx)
    androidTestImplementation(libs.test.espressocore)

    implementation(project(":vyou-kmm-client"))
}

fun loadVYouConfiguration(): Map<String, String> {
    val properties = gradleLocalProperties(rootDir)

    val clientId = properties["vyou.client.id"] as? String ?: System.getenv("VYOU_CLIENT_ID")
    val serverUrl = properties["vyou.server.url"] as? String ?: System.getenv("VYOU_SERVER_URL")
    val publicSalt = properties["vyou.public.salt"] as? String ?: System.getenv("VYOU_PUBLIC_SALT")
    val clientUri = properties["vyou.client.uri"] as? String ?: System.getenv("VYOU_CLIENT_URI")

    val valuesUrl = clientUri.split("://")
    val scheme = valuesUrl[0]
    val hostPath = valuesUrl[1]
    val host: String
    val pathPattern: String
    if (hostPath.contains("/")) {
        host = hostPath.split("/")[0]
        pathPattern = hostPath.replace(host, "")
    } else {
        host = hostPath.split("\\?")[0]
        pathPattern = ""
    }

    val googleId = properties["vyou.google.server.id"] as? String ?: System.getenv("VYOU_GOOGLE_SERVER_ID")
    val facebookId = properties["vyou.facebook.client.id"] as? String ?: System.getenv("VYOU_FACEBOOK_CLIENT_ID")

    return mapOf(
        "vyou_client_id" to clientId,
        "vyou_server_url" to serverUrl,
        "vyou_public_salt" to publicSalt,
        "vyou_client_uri_scheme" to scheme,
        "vyou_client_uri_host" to host,
        "vyou_client_uri_path_pattern" to pathPattern,
        "vyou_google_server_id" to googleId,
        "vyou_facebook_client_id" to facebookId
    )
}