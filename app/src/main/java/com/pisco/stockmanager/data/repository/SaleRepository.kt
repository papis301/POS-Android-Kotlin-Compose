package com.pisco.stockmanager.data.repository

import com.pisco.stockmanager.data.local.SaleDao
import com.pisco.stockmanager.data.local.SaleEntity
import javax.inject.Inject

class SaleRepository @Inject constructor(
    private val saleDao: SaleDao
) {

    fun getSales() =
        saleDao.getAllSales()

    suspend fun insertSale(
        sale: SaleEntity
    ): Long {

        return saleDao.insert(sale)
    }

    suspend fun deleteSale(
        sale: SaleEntity
    ) {
        saleDao.delete(sale)
    }
}