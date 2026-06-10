package com.pisco.stockmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val clientId: Int,

    val total: Double,

    val createdAt: Long = System.currentTimeMillis()
)