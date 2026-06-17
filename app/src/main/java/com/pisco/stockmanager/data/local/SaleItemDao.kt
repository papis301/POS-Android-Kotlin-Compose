package com.pisco.stockmanager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(
        saleItem: SaleItemEntity
    )

    @Query(
        "SELECT * FROM sale_items WHERE saleId = :saleId"
    )
    fun getItemsBySale(
        saleId: Int
    ): Flow<List<SaleItemEntity>>

    @Query("SELECT * FROM sale_items")
    suspend fun getAllSaleItemsOnce():
            List<SaleItemEntity>
    @Query("DELETE FROM sale_items")
    suspend fun deleteAll()
}