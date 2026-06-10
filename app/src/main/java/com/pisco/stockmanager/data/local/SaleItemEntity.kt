package com.pisco.stockmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_items")
data class SaleItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val saleId: Int,

    val productId: Int,

    val productName: String,

    val quantity: Int,

    val unitPrice: Double
)