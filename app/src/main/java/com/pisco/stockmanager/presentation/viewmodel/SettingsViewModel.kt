package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pisco.stockmanager.data.preferences.AppSettings
import com.pisco.stockmanager.data.preferences.AppThemeMode
import com.pisco.stockmanager.data.preferences.Currency
import com.pisco.stockmanager.data.preferences.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> =
        settingsRepository.settings.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppSettings()
        )

    fun saveStoreInfo(
        name: String,
        address: String,
        phone: String
    ) {

        viewModelScope.launch {

            settingsRepository.updateStoreInfo(
                name = name,
                address = address,
                phone = phone
            )
        }
    }

    fun setCurrency(
        currency: Currency
    ) {

        viewModelScope.launch {

            settingsRepository.updateCurrency(
                currency
            )
        }
    }

    fun setThemeMode(
        mode: AppThemeMode
    ) {

        viewModelScope.launch {

            settingsRepository.updateThemeMode(
                mode
            )
        }
    }
}