package com.pisco.stockmanager.shared.di

import com.pisco.stockmanager.shared.data.AppDatabase
import com.pisco.stockmanager.shared.data.DatabaseFactory
import com.pisco.stockmanager.shared.data.buildDatabase
import com.pisco.stockmanager.shared.domain.CheckoutRepository
import com.pisco.stockmanager.shared.domain.ProductRepository
import com.pisco.stockmanager.shared.domain.SaleItemRepository
import com.pisco.stockmanager.shared.domain.SaleRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Dépendances communes à toutes les plateformes.
 * Chaque plateforme fournit en plus son propre module (voir
 * AndroidPlatformModule.kt / DesktopPlatformModule.kt) qui déclare
 * comment construire DatabaseFactory.
 */
val sharedModule: Module = module {

    single<AppDatabase> {
        buildDatabase(get<DatabaseFactory>())
    }

    single {
        get<AppDatabase>().productDao()
    }

    single {
        get<AppDatabase>().saleDao()
    }

    single {
        get<AppDatabase>().saleItemDao()
    }

    single {
        ProductRepository(get())
    }

    single {
        SaleRepository(get())
    }

    single {
        SaleItemRepository(get())
    }

    single {
        CheckoutRepository(get(), get(), get())
    }
}