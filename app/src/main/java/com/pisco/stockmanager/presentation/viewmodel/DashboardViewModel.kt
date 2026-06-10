package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pisco.stockmanager.data.repository.ClientRepository
import com.pisco.stockmanager.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository
) : ViewModel() {

    private val _productCount =
        MutableStateFlow(0)

    val productCount =
        _productCount.asStateFlow()

    private val _clientCount =
        MutableStateFlow(0)

    val clientCount =
        _clientCount.asStateFlow()

    private val _stockValue =
        MutableStateFlow(0.0)

    val stockValue =
        _stockValue.asStateFlow()

    init {

        viewModelScope.launch {

            productRepository
                .getProductCount()
                .collect {

                    _productCount.value = it
                }
        }

        viewModelScope.launch {

            clientRepository
                .getClientCount()
                .collect {

                    _clientCount.value = it
                }
        }

        viewModelScope.launch {

            productRepository
                .getStockValue()
                .collect {

                    _stockValue.value = it ?: 0.0
                }
        }
    }
}