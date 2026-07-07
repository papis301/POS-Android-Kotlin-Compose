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
    entities = [ProductEntity::class, SaleEntity::class, SaleItemEntity::class],
    version = 2,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun saleDao(): SaleDao
    abstract fun saleItemDao(): SaleItemDao
}

/**
 * Room génère l'implémentation "actual" de cet objet à la compilation
 * (obligatoire depuis Kotlin 2.0 pour les bases de données KMP).
 * Ne rien mettre dedans, ne pas le supprimer.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

/**
 * Chaque plateforme fournit son propre Builder (chemin de fichier différent).
 * Voir DatabaseFactory.android.kt et DatabaseFactory.desktop.kt
 */
expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<AppDatabase>
}

const val DATABASE_NAME = "yombena.db"

/**
 * Version 1 -> 2 : ajout des tables Ventes (sales, sale_items).
 * Ne touche PAS à la table "products" existante — les produits déjà
 * enregistrés (Android comme Desktop) sont conservés intacts.
 */
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
        .addMigrations(MIGRATION_1_2)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .build()
}