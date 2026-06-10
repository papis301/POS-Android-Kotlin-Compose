package com.pisco.stockmanager.navigation

sealed class Screen(
    val route: String
) {

    object Dashboard :
        Screen("dashboard")

    object Products :
        Screen("products")

    object Clients :
        Screen("clients")

    object Sales :
        Screen("sales")
}