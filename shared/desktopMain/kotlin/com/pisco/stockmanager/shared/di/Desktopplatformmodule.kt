package com.pisco.stockmanager.shared.di

import com.pisco.stockmanager.shared.data.DatabaseFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val desktopPlatformModule: Module = module {
    single { DatabaseFactory() }
}