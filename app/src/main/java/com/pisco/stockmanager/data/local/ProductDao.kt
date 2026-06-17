package com.pisco.stockmanager.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Exécute les requêtes SQL
@Dao
interface ProductDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(
        product: ProductEntity
    )

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    fun getProductCount(): Flow<Int>

    @Query("SELECT SUM(price * quantity) FROM products")
    fun getStockValue(): Flow<Double?>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(
        id: Int
    ): ProductEntity?

    @Query("SELECT * FROM products")
    suspend fun getAllProductsOnce():
            List<ProductEntity>

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}