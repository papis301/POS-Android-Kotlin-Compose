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
    private val repository: ProductRepository
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

            repository.getProducts()
                .collect {

                    _products.value = it
                }
        }
    }

    fun deleteProduct(
        product: ProductEntity
    ) {

        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun updateProduct(
        product: ProductEntity
    ) {

        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun addProduct(
        name: String,
        price: Double,
        quantity: Int
    ) {
        viewModelScope.launch {

            repository.insertProduct(
                ProductEntity(
                    name = name,
                    description = "",
                    price = price,
                    quantity = quantity,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}