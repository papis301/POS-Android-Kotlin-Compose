package com.pisco.stockmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        ProductEntity::class,
        ClientEntity::class,
        SaleEntity::class,
        SaleItemEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun clientDao(): ClientDao
    abstract fun saleDao(): SaleDao

    abstract fun saleItemDao(): SaleItemDao
}

val MIGRATION_6_7 =
    object : Migration(
        6,
        7
    ) {

        override fun migrate(
            db:
            SupportSQLiteDatabase
        ) {

            db.execSQL(
                """
            ALTER TABLE products
            ADD COLUMN active
            INTEGER NOT NULL
            DEFAULT 1
            """
            )
        }
    }