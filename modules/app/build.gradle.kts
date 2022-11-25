@file:Suppress("LocalVariableName")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("dev.iurysouza.livematch.android-application")
}

android {
    signingConfigs {
        create("release") {
            keyAlias = getLocalProperty("keyAlias")
            keyPassword = getLocalProperty("keyPassword")
            storePassword = getLocalProperty("storePassword")
            storeFile = file(getLocalProperty("storeFile"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
        val USE_MOCK_URL: String by project
        val API_URL: String by project
        val FOOTBALL_DATA_BASE_URL: String by project
        val MOCK_API_URL: String by project

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            buildConfigField("String", "API_URL", API_URL)
            buildConfigField(
                type = "String",
                name = "FOOTBALL_DATA_BASE_URL",
                value = FOOTBALL_DATA_BASE_URL
            )
        }
        getByName("debug") {
            buildConfigField(
                type = "String",
                name = "FOOTBALL_DATA_BASE_URL",
                value = if (USE_MOCK_URL.toBoolean()) MOCK_API_URL else FOOTBALL_DATA_BASE_URL
            )
            buildConfigField(
                type = "String",
                name = "API_URL",
                value = if (USE_MOCK_URL.toBoolean()) MOCK_API_URL else API_URL
            )
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2022.11.00"))
    implementation("androidx.compose.material:material")
    implementation(libs.bundles.composeMinimal)
    debugImplementation(libs.androidx.compose.ui.uiTooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.lifecycle.viewModelKtx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)
    implementation(libs.google.acompanist.systemuicontroller)
    implementation(libs.google.acompanist.navigation.animation)

    implementation(libs.retrofit.core)
    implementation(libs.squareup.retrofit.moshi.converter)
    implementation(libs.squareup.okhttp.loggingInterceptor)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.squareup.moshi.moshiAdapters)
    kapt(libs.squareup.moshi.kotlinCodegen)
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(libs.bundles.arrow)
    implementation(libs.halilibo.composeRichttext.richtextCommonmark)

    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-svg:2.2.2")

    testImplementation(libs.squareup.okhttp.mockwebserver)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.bundles.kotestBundle)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.browser:browser:1.4.0")
}

fun getLocalProperty(key: String) = gradleLocalProperties(rootDir).getProperty(key)
