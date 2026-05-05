import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization) // ** ADDED for Navigation **
    alias(libs.plugins.ksp) // **ADDED** for Room
    alias(libs.plugins.androidx.room) // **ADDED** for Room
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.room.sqlite.wrapper) // **ADDED** for Room
            implementation(libs.androidx.material.icons.core)
            implementation(libs.androidx.material)
            implementation(libs.material.icons.extended)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization.json) // ** ADDED for Navigation **
            implementation(libs.androidx.navigation.compose) // ** ADDED for Navigation **
            implementation(libs.kotlinx.datetime) // ** ADDED **
            val room_version = "2.8.4"
            implementation("androidx.room:room-runtime:$room_version")
            implementation("androidx.room:room-runtime:2.8.4")
            implementation("androidx.sqlite:sqlite-bundled:2.5.0-alpha13")
            implementation(libs.androidx.sqlite.bundled) // **ADDED** for Room
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "edu.moravian.csci395.flashfocus"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "edu.moravian.csci395.flashfocus"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler) // **ADDED** for Room
    add("kspIosSimulatorArm64", libs.androidx.room.compiler) // **ADDED** for Room
    // add("kspIosX64", libs.androidx.room.compiler) // **ADDED** for Room
    add("kspIosArm64", libs.androidx.room.compiler) // **ADDED** for Room
    debugImplementation(libs.compose.uiTooling)
}

room {
    schemaDirectory("$projectDir/schemas")
}