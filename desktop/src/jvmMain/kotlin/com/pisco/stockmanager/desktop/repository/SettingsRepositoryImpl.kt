// desktop/src/main/kotlin/com/pisco/stockmanager/desktop/repository/SettingsRepositoryImpl.kt
package com.pisco.stockmanager.desktop.repository

import androidx.compose.runtime.mutableStateOf
import com.pisco.stockmanager.shared.data.preferences.AppSettings
import com.pisco.stockmanager.shared.data.preferences.AppThemeMode
import com.pisco.stockmanager.shared.data.preferences.Currency
import com.pisco.stockmanager.shared.domain.SettingsRepository
import java.util.prefs.Preferences

class SettingsRepositoryImpl : SettingsRepository {

    private val prefs = Preferences.userNodeForPackage(SettingsRepositoryImpl::class.java)

    private val _settings = mutableStateOf(loadSettings())

    override fun getSettings(): AppSettings = _settings.value

    override fun saveStoreInfo(name: String, address: String, phone: String) {
        prefs.put("storeName", name)
        prefs.put("storeAddress", address)
        prefs.put("storePhone", phone)

        _settings.value = _settings.value.copy(
            storeName = name,
            storeAddress = address,
            storePhone = phone
        )
    }

    override fun setCurrency(currency: Currency) {
        prefs.put("currency", currency.name)

        _settings.value = _settings.value.copy(
            currency = currency
        )
    }

    override fun setThemeMode(themeMode: AppThemeMode) {
        prefs.put("themeMode", themeMode.name)

        _settings.value = _settings.value.copy(
            themeMode = themeMode
        )
    }

    private fun loadSettings(): AppSettings {
        return AppSettings(
            storeName = prefs.get("storeName", ""),
            storeAddress = prefs.get("storeAddress", ""),
            storePhone = prefs.get("storePhone", ""),
            currency = try {
                Currency.valueOf(prefs.get("currency", "XOF"))
            } catch (e: IllegalArgumentException) {
                Currency.XOF
            },
            themeMode = try {
                AppThemeMode.valueOf(prefs.get("themeMode", "SYSTEM"))
            } catch (e: IllegalArgumentException) {
                AppThemeMode.SYSTEM
            }
        )
    }
}