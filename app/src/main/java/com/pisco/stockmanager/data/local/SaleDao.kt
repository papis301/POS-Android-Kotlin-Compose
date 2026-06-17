package com.pisco.stockmanager.data.local

import androidx.room.Dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(
        sale: SaleEntity
    ): Long

    @Delete
    suspend fun delete(
        sale: SaleEntity
    )

    @Query("SELECT * FROM sales")
    fun getAllSales():
            Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales")
    suspend fun getAllSalesOnce():
            List<SaleEntity>
    @Query("DELETE FROM sales")
    suspend fun deleteAll()
}