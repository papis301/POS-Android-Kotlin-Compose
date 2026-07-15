import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("androidx.room")
    kotlin("plugin.serialization") version "1.6.0"
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    sourceSets {

        commonMain.dependencies {
            implementation("androidx.room:room-runtime:2.8.4")
            implementation("androidx.sqlite:sqlite-bundled:2.6.1")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            implementation("io.insert-koin:koin-core:4.0.0")
            implementation("io.ktor:ktor-client-core:2.3.12")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
        }

        androidMain.dependencies {
            implementation("androidx.sqlite:sqlite-framework:2.6.1")
            // 1. AJOUTEZ le moteur OkHttp pour Android
            implementation("io.ktor:ktor-client-okhttp:2.3.12")
        }

        val desktopMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

                // 2. AJOUTEZ le moteur Java (ou CIO) pour le Desktop
                implementation("io.ktor:ktor-client-java:2.3.12")
            }
        }
    }
}

android {
    namespace = "com.pisco.stockmanager.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// CORRECTION ICI : Liaison de KSP avec le compilateur de Room multiplateforme
dependencies {
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.8.4")
    add("kspAndroid", "androidx.room:room-compiler:2.8.4")
    add("kspDesktop", "androidx.room:room-compiler:2.8.4")
}

room {
    schemaDirectory("$projectDir/schemas")
}