package com.pisco.stockmanager.di

import android.content.Context
import androidx.room.Room
import com.pisco.stockmanager.data.local.AppDatabase
import com.pisco.stockmanager.data.local.ClientDao
import com.pisco.stockmanager.data.local.MIGRATION_6_7
import com.pisco.stockmanager.data.local.ProductDao
import com.pisco.stockmanager.data.local.SaleDao
import com.pisco.stockmanager.data.local.SaleItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "stock_manager_db"
        )
            .addMigrations(
                MIGRATION_6_7
            )
            .build()
    }

    @Provides
    fun provideProductDao(
        database: AppDatabase
    ): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideClientDao(
        database: AppDatabase
    ): ClientDao {
        return database.clientDao()
    }

    @Provides
    fun provideSaleDao(
        database: AppDatabase
    ): SaleDao {
        return database.saleDao()
    }

    @Provides
    fun provideSaleItemDao(
        database: AppDatabase
    ): SaleItemDao {

        return database.saleItemDao()
    }
}