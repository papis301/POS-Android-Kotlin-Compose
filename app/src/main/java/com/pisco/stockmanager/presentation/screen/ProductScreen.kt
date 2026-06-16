package com.pisco.stockmanager.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pisco.stockmanager.presentation.viewmodel.ProductViewModel
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel

@Composable
fun ProductScreen()
{


    val configuration =
        LocalConfiguration.current

    val isTablet =
        configuration.screenWidthDp >= 600

    if (isTablet) {

        ProductLandscapeScreen(
        )

    } else {

        ProductPortraitScreen(

        )
    }
}