package com.pisco.stockmanager.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.shared.data.ProductEntity
import com.pisco.stockmanager.shared.domain.ProductRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

// Palette Yombena (même valeurs que app/.../ui/theme/Color.kt)
val GreenPrimary = Color(0xFF1B824F)
val StockLowOrange = Color(0xFFFF9800)
private val BackgroundLight = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen() {

    val repository = koinInject<ProductRepository>()
    val scope = rememberCoroutineScope()

    val products by repository.getProducts()
        .collectAsState(initial = emptyList())

    var search by remember { mutableStateOf("") }

    val filteredProducts = products.filter {
        it.name.contains(search, ignoreCase = true)
    }

    var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }

    // --- Dialogue Ajout ---
    var showAddDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }
    var salePrice by remember { mutableStateOf("") }
    var purchasePrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var stockError by remember { mutableStateOf(false) }

    // --- Dialogue Modification ---
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }
    var editPurchasePrice by remember { mutableStateOf("") }
    var editSalePrice by remember { mutableStateOf("") }
    var editStock by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }

    // --- Dialogue Réapprovisionnement ---
    var showRestockDialog by remember { mutableStateOf(false) }
    var restockQty by remember { mutableStateOf("") }

    fun resetAddFields() {
        productName = ""
        salePrice = ""
        purchasePrice = ""
        stock = ""
        category = ""
        description = ""
        nameError = false
        priceError = false
        stockError = false
    }

    Scaffold(

        containerColor = GreenPrimary,

        topBar = {
            TopAppBar(
                title = { Text("Produits", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary,
                    titleContentColor = Color.White
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = GreenPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    color = BackgroundLight,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                label = { Text("Rechercher un produit") }
            )

            if (products.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Aucun produit pour l'instant. Ajoute-en un avec le bouton +.")
                }

            } else if (filteredProducts.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("Aucun produit ne correspond à « $search ».")
                }

            } else {

                LazyColumn {

                    items(filteredProducts) { product ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {

                            Column(modifier = Modifier.padding(12.dp)) {

                                Text(product.name, style = MaterialTheme.typography.titleMedium)

                                Text("Prix : ${product.price} CFA")

                                Text("Stock : ${product.quantity}")

                                when {
                                    product.quantity == 0 -> {
                                        Text(
                                            "🚫 Rupture",
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }

                                    product.quantity < 5 -> {
                                        Text(
                                            "⚠ Stock faible",
                                            color = StockLowOrange
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row {

                                    OutlinedButton(
                                        onClick = {
                                            selectedProduct = product
                                            restockQty = ""
                                            showRestockDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddBox,
                                            contentDescription = "Réapprovisionner",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    OutlinedButton(
                                        onClick = {
                                            selectedProduct = product
                                            editName = product.name
                                            editDescription = product.description
                                            editPurchasePrice = product.purchasePrice.toString()
                                            editSalePrice = product.price.toString()
                                            editStock = product.quantity.toString()
                                            editCategory = product.category
                                            showEditDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Modifier",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    OutlinedButton(
                                        onClick = {
                                            scope.launch {
                                                repository.deleteProduct(product)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Supprimer",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ================= Dialogue Ajout =================
    if (showAddDialog) {

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Nouveau produit") },
            text = {
                Column {

                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it; nameError = false },
                        isError = nameError,
                        label = { Text("Nom produit *") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = purchasePrice,
                        onValueChange = {
                            purchasePrice = it.filter { c -> c.isDigit() || c == '.' }
                        },
                        label = { Text("Prix d'achat") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = salePrice,
                        onValueChange = {
                            salePrice = it.filter { c -> c.isDigit() || c == '.' }
                            priceError = false
                        },
                        isError = priceError,
                        label = { Text("Prix de vente *") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = stock,
                        onValueChange = {
                            stock = it.filter { c -> c.isDigit() }
                            stockError = false
                        },
                        isError = stockError,
                        label = { Text("Stock initial *") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Catégorie") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") }
                    )

                    if (nameError) {
                        Text("Le nom est obligatoire", color = MaterialTheme.colorScheme.error)
                    }
                    if (priceError) {
                        Text("Prix invalide", color = MaterialTheme.colorScheme.error)
                    }
                    if (stockError) {
                        Text(
                            "Le stock initial doit être supérieur à 0",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        nameError = productName.isBlank()
                        priceError = salePrice.toDoubleOrNull() == null || salePrice.toDouble() <= 0
                        stockError = stock.toIntOrNull() == null || stock.toInt() <= 0

                        if (!nameError && !priceError && !stockError) {

                            // On résout toutes les valeurs AVANT de lancer la coroutine,
                            // pour ne pas risquer de lire un champ déjà réinitialisé
                            // par resetAddFields() pendant que la coroutine est en attente.
                            val newProduct = ProductEntity(
                                name = productName.trim(),
                                description = description.trim(),
                                purchasePrice = purchasePrice.toDoubleOrNull() ?: 0.0,
                                price = salePrice.toDouble(),
                                quantity = stock.toIntOrNull() ?: 0,
                                category = category.trim()
                            )

                            scope.launch {
                                repository.insertProduct(newProduct)
                            }

                            resetAddFields()
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false; resetAddFields() }) {
                    Text("Annuler")
                }
            }
        )
    }

    // ================= Dialogue Modification =================
    if (showEditDialog) {

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Modifier produit") },
            text = {
                Column {

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nom") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        label = { Text("Description") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editPurchasePrice,
                        onValueChange = {
                            editPurchasePrice = it.filter { c -> c.isDigit() || c == '.' }
                        },
                        label = { Text("Prix d'achat") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editSalePrice,
                        onValueChange = {
                            editSalePrice = it.filter { c -> c.isDigit() || c == '.' }
                        },
                        label = { Text("Prix de vente") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editStock,
                        onValueChange = {
                            editStock = it.filter { c -> c.isDigit() }
                        },
                        label = { Text("Stock") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editCategory,
                        onValueChange = { editCategory = it },
                        label = { Text("Catégorie") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedProduct?.let { current ->

                            // Même précaution : on résout la valeur avant de lancer
                            // la coroutine, pour éviter de lire un champ modifié
                            // entre-temps (ex: si l'utilisateur rouvre un autre produit).
                            val updated = current.copy(
                                name = editName,
                                description = editDescription,
                                purchasePrice = editPurchasePrice.toDoubleOrNull() ?: 0.0,
                                price = editSalePrice.toDoubleOrNull() ?: 0.0,
                                quantity = editStock.toIntOrNull() ?: 0,
                                category = editCategory
                            )

                            scope.launch {
                                repository.updateProduct(updated)
                            }
                        }
                        showEditDialog = false
                    }
                ) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // ================= Dialogue Réapprovisionnement =================
    if (showRestockDialog) {

        AlertDialog(
            onDismissRequest = { showRestockDialog = false },
            title = { Text("Réapprovisionnement") },
            text = {
                Column {

                    Text(selectedProduct?.name ?: "")

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = restockQty,
                        onValueChange = {
                            restockQty = it.filter { c -> c.isDigit() }
                        },
                        label = { Text("Quantité à ajouter") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val qty = restockQty.toIntOrNull() ?: 0

                        if (qty > 0) {
                            selectedProduct?.let { current ->
                                scope.launch {
                                    repository.updateProduct(
                                        current.copy(quantity = current.quantity + qty)
                                    )
                                }
                            }
                        }

                        showRestockDialog = false
                    }
                ) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestockDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}