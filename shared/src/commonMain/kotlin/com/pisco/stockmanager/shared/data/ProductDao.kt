package com.pisco.stockmanager.shared.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Update
    suspend fun update(product: ProductEntity)

    @Delete
    suspend fun delete(product: ProductEntity)

    @Query(
        """
        SELECT *
        FROM products
        WHERE active = 1
        ORDER BY id DESC
        """
    )
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: Int): ProductEntity?

    @Query("SELECT COUNT(*) FROM products")
    fun getProductCount(): Flow<Int>

    @Query("SELECT SUM(price * quantity) FROM products")
    fun getStockValue(): Flow<Double?>

    @Query("SELECT * FROM products")
    suspend fun getAllProductsOnce(): List<ProductEntity>

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query(
        """
        UPDATE products
        SET active = 0
        WHERE id = :id
        """
    )
    suspend fun deactivateProduct(id: Int)
}