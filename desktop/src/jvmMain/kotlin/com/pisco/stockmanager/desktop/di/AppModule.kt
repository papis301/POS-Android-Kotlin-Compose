// desktop/src/main/kotlin/com/pisco/stockmanager/desktop/di/AppModule.kt
package com.pisco.stockmanager.desktop.di

import com.pisco.stockmanager.desktop.repository.SettingsRepositoryImpl
import com.pisco.stockmanager.shared.domain.SettingsRepository
import org.koin.dsl.module

val appModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl() }
}

