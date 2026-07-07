package com.pisco.stockmanager.shared.domain

import com.pisco.stockmanager.shared.data.SaleItemDao
import com.pisco.stockmanager.shared.data.SaleItemEntity

class SaleItemRepository(
    private val saleItemDao: SaleItemDao
) {

    suspend fun insert(saleItem: SaleItemEntity) {
        saleItemDao.insert(saleItem)
    }

    suspend fun getItemsForSale(saleId: Int): List<SaleItemEntity> =
        saleItemDao.getItemsForSale(saleId)
}