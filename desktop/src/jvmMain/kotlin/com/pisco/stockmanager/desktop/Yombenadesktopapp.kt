package com.pisco.stockmanager.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.shared.data.ProductEntity
import com.pisco.stockmanager.shared.domain.ProductRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

// Palette Yombena (identique à app/.../ui/theme/Color.kt)
private val GreenPrimary = Color(0xFF1B824F)
private val BackgroundLight = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YombenaDesktopApp() {

    val repository = koinInject<ProductRepository>()
    val scope = rememberCoroutineScope()

    val products by repository.getProducts()
        .collectAsState(initial = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = GreenPrimary,
            background = BackgroundLight
        )
    ) {
        Scaffold(

            topBar = {
                TopAppBar(
                    title = { Text("Yombena — Produits") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = GreenPrimary,
                        titleContentColor = Color.White
                    )
                )
            },

            floatingActionButton = {
                FloatingActionButton(
                    containerColor = GreenPrimary,
                    contentColor = Color.White,
                    onClick = { showAddDialog = true }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Ajouter un produit")
                }
            }

        ) { padding ->

            if (products.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Aucun produit — cette liste vient de la base de données " +
                                "partagée (module :shared), preuve que Desktop et Android " +
                                "peuvent consommer le même code métier.",
                        modifier = Modifier.padding(32.dp)
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { product ->
                        ProductRow(product)
                    }
                }
            }
        }

        if (showAddDialog) {
            AddProductDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, price, quantity ->
                    scope.launch {
                        repository.insertProduct(
                            ProductEntity(
                                name = name,
                                price = price,
                                quantity = quantity
                            )
                        )
                    }
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
private fun ProductRow(product: ProductEntity) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Stock : ${product.quantity}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                "${product.price} FCFA",
                style = MaterialTheme.typography.titleMedium,
                color = GreenPrimary
            )
        }
    }
}

@Composable
private fun AddProductDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, price: Double, quantity: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouveau produit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Prix (FCFA)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantité") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        name,
                        price.toDoubleOrNull() ?: 0.0,
                        quantity.toIntOrNull() ?: 0
                    )
                }
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}