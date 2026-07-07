// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false
    id("com.google.dagger.hilt.android") version "2.56" apply false

    // --- Ajouts pour le module partagé KMP + l'app desktop ---
    kotlin("multiplatform") version "2.1.21" apply false
    id("org.jetbrains.compose") version "1.7.3" apply false
    id("com.android.library") version "8.13.2" apply false
    id("androidx.room") version "2.8.4" apply false
}