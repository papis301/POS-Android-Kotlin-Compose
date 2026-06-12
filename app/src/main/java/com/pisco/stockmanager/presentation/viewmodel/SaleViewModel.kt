package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pisco.stockmanager.data.local.ClientEntity
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.data.local.SaleEntity
import com.pisco.stockmanager.data.local.SaleItemEntity
import com.pisco.stockmanager.data.repository.*
import com.pisco.stockmanager.domain.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(

    private val saleRepository: SaleRepository,

    private val saleItemRepository: SaleItemRepository,

    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository

) : ViewModel() {

    private val _sales =
        MutableStateFlow<List<SaleEntity>>(emptyList())

    val sales: StateFlow<List<SaleEntity>>
            = _sales.asStateFlow()

    private val _products =
        MutableStateFlow<List<ProductEntity>>(
            emptyList()
        )

    val products =
        _products.asStateFlow()

    private val _clients =
        MutableStateFlow<List<ClientEntity>>(emptyList())

    val clients =
        _clients.asStateFlow()

    private val _paymentMode =
        MutableStateFlow("CASH")

    val paymentMode =
        _paymentMode.asStateFlow()

    fun setPaymentMode(
        mode: String
    ) {
        _paymentMode.value = mode
    }

    private val _cart =
        MutableStateFlow<List<CartItem>>(
            emptyList()
        )

    val cart =
        _cart.asStateFlow()

    private fun loadClients() {

        viewModelScope.launch {

            clientRepository
                .getClients()
                .collect {

                    _clients.value = it
                }
        }
    }

    fun addToCart(
        product: ProductEntity,
        quantity: Int
    ) {

        val currentCart = _cart.value.toMutableList()

        val existingItem =
            currentCart.find {
                it.product.id == product.id
            }

        if (existingItem != null) {

            currentCart.remove(existingItem)

            currentCart.add(
                existingItem.copy(
                    quantity =
                        existingItem.quantity + quantity
                )
            )

        } else {

            currentCart.add(
                CartItem(
                    product = product,
                    quantity = quantity
                )
            )
        }

        _cart.value = currentCart
    }

    fun removeFromCart(
        productId: Int
    ) {

        _cart.value =
            _cart.value.filter {

                it.product.id != productId
            }
    }

    val total: StateFlow<Double> =
        _cart
            .map { cartItems ->

                cartItems.sumOf {

                    it.product.price *
                            it.quantity
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0.0
            )

    init {
        loadSales()
        loadProducts()
        loadClients()
    }

    private fun loadProducts() {

        viewModelScope.launch {

            productRepository
                .getProducts()
                .collect {

                    _products.value = it
                }
        }
    }

    fun clearCart() {

        _cart.value = emptyList()
    }

    private fun loadSales() {

        viewModelScope.launch {

            saleRepository.getSales()
                .collect {

                    _sales.value = it
                }
        }
    }

    fun addSale(
        clientId: Int,
        total: Double
    ) {

        viewModelScope.launch {

            saleRepository.insertSale(
                SaleEntity(
                    clientId = clientId,
                    total = total
                )
            )
        }
    }

    fun deleteSale(
        sale: SaleEntity
    ) {

        viewModelScope.launch {

            saleRepository.deleteSale(
                sale
            )
        }
    }
    fun validateSale(
        clientId: Int
    ) {

        viewModelScope.launch {

            if (_cart.value.isEmpty()) {
                return@launch
            }



            val totalAmount =
                _cart.value.sumOf {

                    it.product.price *
                            it.quantity
                }

            val saleId =
                saleRepository.insertSale(
                    SaleEntity(
                        clientId = clientId,
                        total = totalAmount
                    )
                )

            _cart.value.forEach { item ->

                if (
                    item.quantity >
                    item.product.quantity
                ) {
                    return@launch
                }

                saleItemRepository.insert(

                    SaleItemEntity(

                        saleId = saleId.toInt(),

                        productId = item.product.id,

                        productName = item.product.name,

                        quantity = item.quantity,

                        unitPrice = item.product.price
                    )
                )

                val updatedProduct =
                    item.product.copy(

                        quantity =
                            item.product.quantity -
                                    item.quantity
                    )

                productRepository.updateProduct(
                    updatedProduct
                )
            }

            clearCart()
        }
    }

    fun increaseQuantity(
        productId: Int
    ) {

        _cart.value =
            _cart.value.map { item ->

                if (item.product.id == productId) {

                    item.copy(
                        quantity = item.quantity + 1
                    )

                } else {

                    item
                }
            }
    }

    fun decreaseQuantity(
        productId: Int
    ) {

        _cart.value =
            _cart.value.mapNotNull { item ->

                if (item.product.id == productId) {

                    val newQuantity =
                        item.quantity - 1

                    if (newQuantity <= 0) {

                        null

                    } else {

                        item.copy(
                            quantity = newQuantity
                        )
                    }

                } else {

                    item
                }
            }
    }

}