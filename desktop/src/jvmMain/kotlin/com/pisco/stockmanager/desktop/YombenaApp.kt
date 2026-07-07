package com.pisco.stockmanager.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

private enum class AppSection {
    CAISSE,
    PRODUITS
}

@Composable
fun YombenaApp() {

    var section by remember { mutableStateOf(AppSection.CAISSE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = section == AppSection.CAISSE,
                    onClick = { section = AppSection.CAISSE },
                    icon = { Icon(Icons.Default.PointOfSale, contentDescription = "Caisse") },
                    label = { Text("Caisse") }
                )

                NavigationBarItem(
                    selected = section == AppSection.PRODUITS,
                    onClick = { section = AppSection.PRODUITS },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "Produits") },
                    label = { Text("Produits") }
                )
            }
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            when (section) {
                AppSection.CAISSE -> CaisseScreen()
                AppSection.PRODUITS -> ProductScreen()
            }
        }
    }
}