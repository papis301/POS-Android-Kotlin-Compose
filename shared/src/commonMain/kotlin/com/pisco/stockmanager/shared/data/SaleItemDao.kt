package com.pisco.stockmanager.shared.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaleItemDao {

    @Insert
    suspend fun insert(saleItem: SaleItemEntity)

    @Query("SELECT * FROM sale_items WHERE saleId = :saleId")
    suspend fun getItemsForSale(saleId: Int): List<SaleItemEntity>
}