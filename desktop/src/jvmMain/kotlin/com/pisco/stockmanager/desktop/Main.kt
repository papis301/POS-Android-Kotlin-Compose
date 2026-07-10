package com.pisco.stockmanager.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.pisco.stockmanager.desktop.di.appModule // 1. Import your desktop appModule
import com.pisco.stockmanager.shared.di.desktopPlatformModule
import com.pisco.stockmanager.shared.di.sharedModule
import org.koin.core.context.startKoin

fun main() {

    startKoin {
        // 2. Include appModule alongside your shared modules
        modules(desktopPlatformModule, sharedModule, appModule)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Yombena — Back-office"
        ) {
            YombenaTheme {
                YombenaApp()
            }
        }
    }
}