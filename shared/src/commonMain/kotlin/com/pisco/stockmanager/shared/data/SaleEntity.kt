package com.pisco.stockmanager.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val clientId: Int,

    val total: Double,

    val createdAt: Long = 0L
)