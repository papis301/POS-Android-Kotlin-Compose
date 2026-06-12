package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pisco.stockmanager.data.repository.SaleItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaleItemViewModel @Inject constructor(

    private val repository:
    SaleItemRepository

) : ViewModel() {

    fun getItemsBySale(
        saleId: Int
    ) =
        repository.getItemsBySale(
            saleId
        )
}