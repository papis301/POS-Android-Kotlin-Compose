package com.pisco.stockmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// 1. AJOUTEZ CES DEUX IMPORTS pour Room KMP :
import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [
        ProductEntity::class,
        ClientEntity::class,
        SaleEntity::class,
        SaleItemEntity::class,
        ModuleLicenseEntity::class
    ],
    version = 8,
    exportSchema = false
)
// 2. AJOUTEZ l'annotation @ConstructedBy qui pointe vers le constructeur
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun clientDao(): ClientDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun moduleLicenseDao(): ModuleLicenseDao
}

// 3. AJOUTEZ cet objet compagnon que Room va implémenter automatiquement :
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}