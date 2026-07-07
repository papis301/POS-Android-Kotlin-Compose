package com.pisco.stockmanager.shared.di

import android.content.Context
import com.pisco.stockmanager.shared.data.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * À utiliser uniquement si/quand l'app Android migre de Hilt vers Koin
 * pour consommer :shared (voir étape 4 du plan de migration KMP).
 * Tant que l'app Android garde Hilt, ce module n'est pas chargé.
 */
fun androidPlatformModule(context: Context): Module = module {
    single { DatabaseFactory(context.applicationContext) }
}