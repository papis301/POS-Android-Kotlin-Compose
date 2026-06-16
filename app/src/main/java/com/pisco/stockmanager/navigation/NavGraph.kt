package com.pisco.stockmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pisco.stockmanager.presentation.screen.CartScreen
import com.pisco.stockmanager.presentation.screen.ClientScreen
import com.pisco.stockmanager.presentation.screen.DashboardScreen
import com.pisco.stockmanager.presentation.screen.ProductScreen
import com.pisco.stockmanager.presentation.screen.SaleDetailScreen
import com.pisco.stockmanager.presentation.screen.SaleHistoryScreen
import com.pisco.stockmanager.presentation.screen.SaleScreen
import com.pisco.stockmanager.presentation.viewmodel.SaleItemViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Sales.route
    ) {

        composable(
            Screen.Dashboard.route
        ) {
            DashboardScreen(navController)
        }

        composable(
            Screen.Products.route
        ) {
            ProductScreen(
            )
        }

        composable(
            Screen.Clients.route
        ) {
            ClientScreen()
        }

        composable(
            Screen.Sales.route
        ) {
            SaleScreen(navController)
        }

        composable(
            route = "history"
        ) {

            SaleHistoryScreen(navController = navController)
        }

        composable("cart") {

            CartScreen(
                navController = navController
            )
        }

        composable(
            route = "sale_detail/{saleId}"
        ) { backStackEntry ->

            val saleId =
                backStackEntry
                    .arguments
                    ?.getString("saleId")
                    ?.toInt() ?: 0

            SaleDetailRoute(
                saleId = saleId
            )
        }
    }
}

@Composable
fun SaleDetailRoute(
    saleId: Int,
    viewModel:
    SaleItemViewModel =
        hiltViewModel()
) {

    val items by viewModel
        .getItemsBySale(
            saleId
        )
        .collectAsState(
            initial = emptyList()
        )

    SaleDetailScreen(
        saleId = saleId,
        items = items
    )
}