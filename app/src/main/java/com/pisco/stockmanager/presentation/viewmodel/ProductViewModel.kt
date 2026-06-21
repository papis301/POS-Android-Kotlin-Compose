package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//Le ViewModel gère la logique de l'écran. les fonctionnalites
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products =
        MutableStateFlow<List<ProductEntity>>(emptyList())

    val products: StateFlow<List<ProductEntity>>
            = _products.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {

        viewModelScope.launch {

            productRepository.getProducts()
                .collect {

                    _products.value = it
                }
        }
    }

    fun deleteProduct(
        product: ProductEntity
    ) {

        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun updateProduct(
        product: ProductEntity
    ) {

        viewModelScope.launch {

            productRepository.updateProduct(
                product
            )
        }
    }

    fun addProduct(
        name: String,
        description: String,
        purchasePrice: Double,
        price: Double,
        quantity: Int,
        category: String
    ) {

        viewModelScope.launch {

            productRepository.insertProduct(

                ProductEntity(

                    name = name,

                    description = description,

                    purchasePrice = purchasePrice,

                    price = price,

                    quantity = quantity,

                    category = category
                )
            )
        }
    }

    fun restockProduct(
        product: ProductEntity,
        quantity: Int
    ) {

        if (quantity <= 0) return

        viewModelScope.launch {

            productRepository.updateProduct(

                product.copy(

                    quantity =
                        product.quantity + quantity
                )
            )
        }
    }
    fun deactivateProduct(
        product: ProductEntity
    ) {

        viewModelScope.launch {
            productRepository.deactivateProduct(product)
        }
    }
}