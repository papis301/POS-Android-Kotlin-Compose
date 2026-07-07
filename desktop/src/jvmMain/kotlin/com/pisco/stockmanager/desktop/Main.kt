package com.pisco.stockmanager.desktop

import androidx.compose.ui.input.key.Key.Companion.Window
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
            YombenaDesktopApp()
        }
    }
}