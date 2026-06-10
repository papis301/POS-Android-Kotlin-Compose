package com.pisco.stockmanager.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pisco.stockmanager.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val productCount by
    viewModel.productCount.collectAsState()

    val clientCount by
    viewModel.clientCount.collectAsState()

    val stockValue by
    viewModel.stockValue.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Produits : $productCount"
        )

        Text(
            text = "Clients : $clientCount"
        )

        Text(
            text = "Valeur Stock : $stockValue"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                navController.navigate("products")
            }
        ) {
            Text("Produits")
        }

        Button(
            onClick = {
                navController.navigate("clients")
            }
        ) {
            Text("Clients")
        }

        Button(
            onClick = {
                navController.navigate("sales")
            }
        ) {
            Text("Ventes")
        }

    }
}