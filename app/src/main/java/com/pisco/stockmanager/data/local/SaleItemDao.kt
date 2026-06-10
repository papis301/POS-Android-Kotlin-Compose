package com.pisco.stockmanager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao {

    @Insert
    suspend fun insert(
        saleItem: SaleItemEntity
    )

    @Query(
        "SELECT * FROM sale_items WHERE saleId = :saleId"
    )
    fun getItemsBySale(
        saleId: Int
    ): Flow<List<SaleItemEntity>>
}