// shared/src/commonMain/kotlin/com/pisco/stockmanager/shared/data/preferences/Settings.kt
package com.pisco.stockmanager.shared.data.preferences

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val storeName: String = "",
    val storeAddress: String = "",
    val storePhone: String = "",
    val currency: Currency = Currency.XOF,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM
)

enum class AppThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class Currency(
    val code: String,
    val symbol: String
) {
    XOF("XOF", "CFA"),
    USD("USD", "$"),
    EUR("EUR", "€"),
    GBP("GBP", "£"),
    JPY("JPY", "¥"),
    CNY("CNY", "¥")
}