package com.pisco.stockmanager.shared.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.Dispatchers

@Database(
    // AJOUT : ModuleLicenseEntity::class
    entities = [ProductEntity::class, SaleEntity::class, SaleItemEntity::class, ModuleLicenseEntity::class],
    version = 3, // Incrémenté de 2 à 3
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
    abstract fun moduleLicenseDao(): ModuleLicenseDao // Nouveau DAO
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<AppDatabase>
}

const val DATABASE_NAME = "yombena.db"

// Nouvelle migration V2 -> V3 pour Desktop
val MIGRATION_2_3 = object : Migration(startVersion = 2, endVersion = 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `module_licenses` (
                `moduleId` TEXT PRIMARY KEY NOT NULL,
                `isActivated` INTEGER NOT NULL,
                `activationDate` INTEGER NOT NULL,
                `expirationDate` INTEGER NOT NULL,
                `lastCheckedTimestamp` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}

// Mise à jour de la liste des migrations d'origine de l'étape 1
val MIGRATION_1_2 = object : Migration(startVersion = 1, endVersion = 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `sales` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `clientId` INTEGER NOT NULL,
                `total` REAL NOT NULL,
                `createdAt` INTEGER NOT NULL
            )
            """.trimIndent()
        )
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `sale_items` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `saleId` INTEGER NOT NULL,
                `productId` INTEGER NOT NULL,
                `productName` TEXT NOT NULL,
                `quantity` INTEGER NOT NULL,
                `unitPrice` REAL NOT NULL
            )
            """.trimIndent()
        )
    }
}

fun buildDatabase(factory: DatabaseFactory): AppDatabase {
    return factory.create()
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // AJOUT de MIGRATION_2_3
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}