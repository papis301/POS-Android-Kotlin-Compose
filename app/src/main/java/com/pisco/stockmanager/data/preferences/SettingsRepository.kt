package com.pisco.stockmanager.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(
    name = "settings"
)

/**
 * Devises disponibles dans l'application.
 * `symbol` est affiché après le montant (ex: "12 500 CFA").
 */
enum class Currency(
    val code: String,
    val symbol: String
) {
    CFA("CFA", "CFA"),
    EUR("EUR", "€"),
    USD("USD", "$"),
    GBP("GBP", "£")
}

enum class AppThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

data class AppSettings(
    val storeName: String = "StockManager",
    val storeAddress: String = "",
    val storePhone: String = "",
    val currency: Currency = Currency.CFA,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM
)

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {

        val STORE_NAME =
            stringPreferencesKey("store_name")

        val STORE_ADDRESS =
            stringPreferencesKey("store_address")

        val STORE_PHONE =
            stringPreferencesKey("store_phone")

        val CURRENCY =
            stringPreferencesKey("currency")

        val THEME_MODE =
            stringPreferencesKey("theme_mode")
    }

    val settings: Flow<AppSettings> =
        context.settingsDataStore.data.map { prefs ->

            AppSettings(

                storeName =
                    prefs[Keys.STORE_NAME]
                        ?: "StockManager",

                storeAddress =
                    prefs[Keys.STORE_ADDRESS] ?: "",

                storePhone =
                    prefs[Keys.STORE_PHONE] ?: "",

                currency =
                    prefs[Keys.CURRENCY]?.let { code ->

                        Currency.entries.find {
                            it.code == code
                        }

                    } ?: Currency.CFA,

                themeMode =
                    prefs[Keys.THEME_MODE]?.let { mode ->

                        AppThemeMode.entries.find {
                            it.name == mode
                        }

                    } ?: AppThemeMode.SYSTEM
            )
        }

    suspend fun updateStoreInfo(
        name: String,
        address: String,
        phone: String
    ) {

        context.settingsDataStore.edit { prefs ->

            prefs[Keys.STORE_NAME] = name
            prefs[Keys.STORE_ADDRESS] = address
            prefs[Keys.STORE_PHONE] = phone
        }
    }

    suspend fun updateCurrency(
        currency: Currency
    ) {

        context.settingsDataStore.edit { prefs ->

            prefs[Keys.CURRENCY] = currency.code
        }
    }

    suspend fun updateThemeMode(
        mode: AppThemeMode
    ) {

        context.settingsDataStore.edit { prefs ->

            prefs[Keys.THEME_MODE] = mode.name
        }
    }
}