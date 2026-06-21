package com.pisco.stockmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val description: String = "",

    val purchasePrice: Double = 0.0,

    val price: Double,

    val quantity: Int,

    val category: String = "",

    val createdAt: Long =
        System.currentTimeMillis(),

    val active: Boolean = true
)