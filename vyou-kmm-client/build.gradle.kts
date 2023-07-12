import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.android.library")
    id("maven-publish")
    id("com.google.devtools.ksp")
    id("com.rickclephas.kmp.nativecoroutines")
    id("io.github.luca992.multiplatform-swiftpackage") version "2.0.5-arm64"
    id("dev.icerock.moko.kswift") version "0.6.1"
}

group = "com.vyou"
version = "1.0.0"

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "VYouCore"
        }
        it.mavenPublication {
            artifactId = "client-ios"
        }
    }

    android {
        publishLibraryVariants("release")
        mavenPublication {
            artifactId = "client-android"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.datetime)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.auth)
                implementation(libs.slf4j)
                implementation(libs.multiplatform.settings)
                api(libs.koin.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.koin.test)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.koin.android)
                implementation(libs.ktor.client.android)
                implementation(libs.androidx.security.crypto)
                implementation(libs.bcrypt)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.junit)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.test.junit.ktx)
                implementation(libs.test.espressocore)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications.withType<MavenPublication> {
        if (name == "kotlinMultiplatform") {
            artifactId = "client-kmm"
        }
    }
    repositories {
        maven {
            val s3Map = getS3Configuration()
            url = uri(s3Map["url"]!!)
            credentials(AwsCredentials::class) {
                accessKey = s3Map["accessKey"]!!
                secretKey = s3Map["secretKey"]!!
            }
        }
    }
}

fun getVYouVersion(): String {
    return gradleLocalProperties(rootDir).getProperty("vyou.version") ?: System.getenv("VYOU_VERSION")
}

fun getS3Configuration(): Map<String, String> {
    val properties = gradleLocalProperties(rootDir)
    val url = properties["s3.publish.url"] as? String ?: System.getenv("S3_PUBLISH_URL")
    val accessKey = properties["s3.access.key"] as? String ?: System.getenv("S3_ACCESS_KEY")
    val secretKey = properties["s3.secret.key"] as? String ?: System.getenv("S3_SECRET_KEY")
    return mapOf("url" to url, "accessKey" to accessKey, "secretKey" to secretKey)
}

multiplatformSwiftPackage {
    packageName("VYouCore")
    swiftToolsVersion("5.3")
    targetPlatforms {
        iOS { v("13") }
    }
    outputDirectory(File(rootDir, "../vyou-ios/vyou-kmm"))
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
}