package com.pisco.stockmanager.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SaleScreen(
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel()
) {

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    if (isTablet) {

        SaleLandscapeScreen(
            navController = navController,
            viewModel = viewModel
        )

    } else {

        SalePortraitScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}