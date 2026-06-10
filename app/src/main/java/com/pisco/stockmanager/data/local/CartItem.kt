package com.pisco.stockmanager.domain.model

import com.pisco.stockmanager.data.local.ProductEntity

data class CartItem(

    val product: ProductEntity,

    val quantity: Int
)