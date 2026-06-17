package com.pisco.stockmanager.domain.model

import com.pisco.stockmanager.data.local.ClientEntity
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.data.local.SaleEntity
import com.pisco.stockmanager.data.local.SaleItemEntity

data class BackupData(

    val backupDate: Long =
        System.currentTimeMillis(),
    val products: List<ProductEntity>,

    val clients: List<ClientEntity>,

    val sales: List<SaleEntity>,

    val saleItems: List<SaleItemEntity>
)