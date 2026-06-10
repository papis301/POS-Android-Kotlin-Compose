package com.pisco.stockmanager.data.repository

import com.pisco.stockmanager.data.local.SaleItemDao
import com.pisco.stockmanager.data.local.SaleItemEntity
import javax.inject.Inject

class SaleItemRepository @Inject constructor(
    private val saleItemDao: SaleItemDao
) {

    suspend fun insert(
        item: SaleItemEntity
    ) {
        saleItemDao.insert(item)
    }

    fun getItemsBySale(
        saleId: Int
    ) = saleItemDao.getItemsBySale(saleId)
}