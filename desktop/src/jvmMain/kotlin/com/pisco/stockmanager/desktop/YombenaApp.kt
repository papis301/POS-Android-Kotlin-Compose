// desktop/src/jvmMain/kotlin/com/pisco/stockmanager/desktop/YombenaApp.kt
package com.pisco.stockmanager.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.pisco.stockmanager.desktop.di.appModule
import org.koin.compose.KoinApplication
import org.koin.core.context.startKoin
import java.awt.Dimension
import java.awt.Frame
import java.awt.Toolkit

private enum class AppSection {
    CAISSE,
    PRODUITS,
    SETTINGS
}
fun main() = application {
    startKoin {
        modules(appModule)
    }

    val windowState = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Yombena - Back-office",
        undecorated = true,
        resizable = true
    ) {
        // Force le plein écran APRÈS que la fenêtre native soit créée
        LaunchedEffect(Unit) {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            window.setBounds(0, 0, screenSize.width, screenSize.height)
            window.extendedState = Frame.MAXIMIZED_BOTH
        }

        KoinApplication(application = { modules(appModule) }) {
            YombenaTheme(darkTheme = false) {
                YombenaApp()
            }
        }
    }
}


//fun main() {
//    // Initialiser Koin
//    startKoin {
//        modules(appModule)
//    }
//
//    val window = ComposeWindow().apply {
//        title = "Yombena - Back-office"
//       // minimumSize = Dimension(2024, 768)
//        isResizable = true
//
//        // Force la fenêtre à s'ouvrir maximisée (X et Y)
//       // extendedState = JFrame.MAXIMIZED_BOTH
//    }
//
//    // Utiliser la méthode setContent au lieu de compose
//    window.setContent {
//        KoinApplication(application = {
//            modules(appModule)
//        }) {
//            YombenaTheme(darkTheme = false) {
//                YombenaApp()
//            }
//        }
//    }
//
//    window.isVisible = true
//
//    window.extendedState = JFrame.MAXIMIZED_BOTH
//}

@Composable
fun YombenaApp() {
    var section by remember { mutableStateOf(AppSection.CAISSE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                NavigationBarItem(
                    selected = section == AppSection.CAISSE,
                    onClick = { section = AppSection.CAISSE },
                    icon = { Icon(Icons.Default.PointOfSale, contentDescription = "Caisse") },
                    label = { Text("Caisse") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = section == AppSection.PRODUITS,
                    onClick = { section = AppSection.PRODUITS },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "Produits") },
                    label = { Text("Produits") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                NavigationBarItem(
                    selected = section == AppSection.SETTINGS,
                    onClick = { section = AppSection.SETTINGS },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Paramètres") },
                    label = { Text("Paramètres") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (section) {
                AppSection.CAISSE -> CaisseScreen()
                AppSection.PRODUITS -> ProductScreen()
                AppSection.SETTINGS -> SettingsScreen(
                    onBack = { /* Pas de retour sur desktop */ }
                )
            }
        }
    }
}