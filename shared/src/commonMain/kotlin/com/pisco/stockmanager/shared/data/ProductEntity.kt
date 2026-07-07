package com.pisco.stockmanager.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Version partagée (KMP) de ProductEntity.
 * Identique à app/.../data/local/ProductEntity.kt — voir la note de migration
 * dans le README pour le plan de convergence des deux versions.
 */
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

    val createdAt: Long = 0L,

    val active: Boolean = true
)