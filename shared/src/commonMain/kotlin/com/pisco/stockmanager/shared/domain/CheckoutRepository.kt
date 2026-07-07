package com.pisco.stockmanager.shared.domain

import com.pisco.stockmanager.shared.data.SaleEntity
import com.pisco.stockmanager.shared.data.SaleItemEntity

sealed class CheckoutResult {
    data object Success : CheckoutResult()
    data class InsufficientStock(val productNames: List<String>) : CheckoutResult()
    data object EmptyCart : CheckoutResult()
}

/**
 * Orchestre la validation d'une vente.
 *
 * Contrairement à la version Android actuelle (SaleViewModel.validateSale),
 * ici TOUT le panier est vérifié AVANT d'écrire quoi que ce soit en base.
 * Ça évite le cas où une vente partiellement invalide reste enregistrée
 * avec un total qui ne correspond plus aux lignes réellement insérées.
 */
class CheckoutRepository(
    private val saleRepository: SaleRepository,
    private val saleItemRepository: SaleItemRepository,
    private val productRepository: ProductRepository
) {

    suspend fun validateSale(
        cart: List<CartItem>,
        clientId: Int
    ): CheckoutResult {

        if (cart.isEmpty()) {
            return CheckoutResult.EmptyCart
        }

        // 1) Pré-validation : on vérifie TOUT le panier avant d'écrire quoi que ce soit
        val insufficientItems = cart.filter { it.quantity > it.product.quantity }

        if (insufficientItems.isNotEmpty()) {
            return CheckoutResult.InsufficientStock(insufficientItems.map { it.product.name })
        }

        // 2) Tout est valide : on peut écrire en toute sécurité
        val totalAmount = cart.sumOf { it.product.price * it.quantity }

        val saleId = saleRepository.insertSale(
            SaleEntity(
                clientId = clientId,
                total = totalAmount
            )
        )

        cart.forEach { item ->

            saleItemRepository.insert(
                SaleItemEntity(
                    saleId = saleId.toInt(),
                    productId = item.product.id,
                    productName = item.product.name,
                    quantity = item.quantity,
                    unitPrice = item.product.price
                )
            )

            productRepository.updateProduct(
                item.product.copy(
                    quantity = item.product.quantity - item.quantity
                )
            )
        }

        return CheckoutResult.Success
    }
}