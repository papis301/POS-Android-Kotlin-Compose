package com.pisco.stockmanager.desktop

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pisco.stockmanager.shared.di.desktopPlatformModule
import com.pisco.stockmanager.shared.di.sharedModule
import org.koin.core.context.startKoin

fun main() {

    startKoin {
        modules(desktopPlatformModule, sharedModule)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Yombena — Back-office"
        ) {
            MaterialTheme(
                colorScheme = lightColorScheme(primary = GreenPrimary)
            ) {
                YombenaApp()
            }
        }
    }
}