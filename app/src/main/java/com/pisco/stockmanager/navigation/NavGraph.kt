package com.pisco.stockmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pisco.stockmanager.presentation.screen.ClientScreen
import com.pisco.stockmanager.presentation.screen.DashboardScreen
import com.pisco.stockmanager.presentation.screen.ProductScreen
import com.pisco.stockmanager.presentation.screen.Sale2Screen
import com.pisco.stockmanager.presentation.screen.SaleScreen

@Composable
fun AppNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {

        composable(
            Screen.Dashboard.route
        ) {
            DashboardScreen(navController)
        }

        composable(
            Screen.Products.route
        ) {
            ProductScreen()
        }

        composable(
            Screen.Clients.route
        ) {
            ClientScreen()
        }

        composable(
            Screen.Sales.route
        ) {
            Sale2Screen()
        }
    }
}