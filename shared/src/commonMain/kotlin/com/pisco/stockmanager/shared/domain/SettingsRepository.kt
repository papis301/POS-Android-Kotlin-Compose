// shared/src/commonMain/kotlin/com/pisco/stockmanager/shared/domain/SettingsRepository.kt
package com.pisco.stockmanager.shared.domain

import com.pisco.stockmanager.shared.data.preferences.AppSettings
import com.pisco.stockmanager.shared.data.preferences.AppThemeMode
import com.pisco.stockmanager.shared.data.preferences.Currency

interface SettingsRepository {
    fun getSettings(): AppSettings
    fun saveStoreInfo(name: String, address: String, phone: String)
    fun setCurrency(currency: Currency)
    fun setThemeMode(themeMode: AppThemeMode)
}