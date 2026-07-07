package com.pisco.stockmanager.shared.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Insert
    suspend fun insert(sale: SaleEntity): Long

    @Delete
    suspend fun delete(sale: SaleEntity)

    @Query("SELECT * FROM sales ORDER BY id DESC")
    fun getAllSales(): Flow<List<SaleEntity>>
}