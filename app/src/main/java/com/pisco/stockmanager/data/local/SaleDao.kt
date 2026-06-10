package com.pisco.stockmanager.data.local

import androidx.room.Dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert
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
}