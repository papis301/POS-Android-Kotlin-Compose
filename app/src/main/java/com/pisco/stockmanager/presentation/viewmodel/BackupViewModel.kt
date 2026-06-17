package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pisco.stockmanager.data.repository.ClientRepository
import com.pisco.stockmanager.data.repository.ProductRepository
import com.pisco.stockmanager.data.repository.SaleItemRepository
import com.pisco.stockmanager.data.repository.SaleRepository
import com.pisco.stockmanager.domain.model.BackupData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(

    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository,
    private val saleRepository: SaleRepository,
    private val saleItemRepository: SaleItemRepository

) : ViewModel() {

    suspend fun createBackup(): String {

        val backup = BackupData(

            products =
                productRepository
                    .getProductsOnce(),

            clients =
                clientRepository
                    .getClientsOnce(),

            sales =
                saleRepository
                    .getSalesOnce(),

            saleItems =
                saleItemRepository
                    .getSaleItemsOnce()
        )

        return Gson().toJson(backup)
    }

    suspend fun restoreBackup(
        backup: BackupData
    ) {

        saleItemRepository.deleteAll()

        saleRepository.deleteAll()

        clientRepository.deleteAll()

        productRepository.deleteAll()

        backup.products.forEach {

            productRepository.insertProduct(
                it
            )
        }

        backup.clients.forEach {

            clientRepository.insertClient(
                it
            )
        }

        backup.sales.forEach {

            saleRepository.insertSale(
                it
            )
        }

        backup.saleItems.forEach {

            saleItemRepository.insert(
                it
            )
        }
    }
}