// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugin.android.application)
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.kotlin.serialization)
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.0-1.0.8")
        classpath(libs.plugin.kmp.nativecoroutines)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}