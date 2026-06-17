package com.pisco.stockmanager.data.repository

import com.pisco.stockmanager.data.local.ProductDao
import com.pisco.stockmanager.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Le Repository gère l'accès aux données.
class ProductRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(
        product: ProductEntity
    ) {
        productDao.insert(product)
    }

    suspend fun updateProduct(
        product: ProductEntity
    ) {
        productDao.update(product)
    }

    suspend fun deleteProduct(
        product: ProductEntity
    ) {
        productDao.delete(product)
    }

    fun getProductCount() =
        productDao.getProductCount()

    fun getStockValue() =
        productDao.getStockValue()

    suspend fun getProductById(
        id: Int
    ) = productDao.getById(id)

    suspend fun getProductsOnce():
            List<ProductEntity> {

        return productDao
            .getAllProductsOnce()
    }

    suspend fun deleteAll() {

        productDao.deleteAll()
    }
}