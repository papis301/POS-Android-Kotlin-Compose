// desktop/src/jvmMain/kotlin/com/pisco/stockmanager/desktop/YombenaApp.kt
package com.pisco.stockmanager.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp // AJOUT : Import d'une icône pour les ventes (ou utilisez une autre icône de votre choix)
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
    VENTES, // AJOUT
    SETTINGS
}

fun main() = application {
    // ... Votre code main reste identique ...
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

                // AJOUT : Bouton Ventes
                NavigationBarItem(
                    selected = section == AppSection.VENTES,
                    onClick = { section = AppSection.VENTES },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Ventes") },
                    label = { Text("Ventes") },
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
                AppSection.VENTES -> VentesScreen() // AJOUT : Assurez-vous d'avoir créé le composable VentesScreen()
                AppSection.SETTINGS -> SettingsScreen(
                    onBack = { /* Pas de retour sur desktop */ }
                )
            }
        }
    }
}

