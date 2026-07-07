package com.pisco.stockmanager.shared.domain

import com.pisco.stockmanager.shared.data.SaleDao
import com.pisco.stockmanager.shared.data.SaleEntity
import kotlinx.coroutines.flow.Flow

class SaleRepository(
    private val saleDao: SaleDao
) {

    fun getSales(): Flow<List<SaleEntity>> =
        saleDao.getAllSales()

    suspend fun insertSale(sale: SaleEntity): Long =
        saleDao.insert(sale)

    suspend fun deleteSale(sale: SaleEntity) {
        saleDao.delete(sale)
    }
}