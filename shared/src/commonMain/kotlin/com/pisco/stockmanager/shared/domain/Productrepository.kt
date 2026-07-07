package com.pisco.stockmanager.shared.domain

import com.pisco.stockmanager.shared.data.ProductDao
import com.pisco.stockmanager.shared.data.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Identique en logique à app/.../data/repository/ProductRepository.kt,
 * mais sans annotation @Inject (Hilt est Android-only).
 * L'injection se fait ici via Koin — voir SharedModule.kt.
 */
class ProductRepository(
    private val productDao: ProductDao
) {

    fun getProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(product: ProductEntity) {
        productDao.insert(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.update(product)
    }

    suspend fun deleteProduct(product: ProductEntity) {
        productDao.delete(product)
    }

    suspend fun deactivateProduct(product: ProductEntity) {
        productDao.deactivateProduct(product.id)
    }

    fun getProductCount(): Flow<Int> =
        productDao.getProductCount()

    fun getStockValue(): Flow<Double?> =
        productDao.getStockValue()

    suspend fun getProductById(id: Int): ProductEntity? =
        productDao.getById(id)

    suspend fun getProductsOnce(): List<ProductEntity> =
        productDao.getAllProductsOnce()

    suspend fun deleteAll() {
        productDao.deleteAll()
    }
}