package com.pisco.stockmanager.shared.domain

import com.pisco.stockmanager.shared.data.ProductEntity

data class CartItem(
    val product: ProductEntity,
    val quantity: Int
)