package com.pisco.stockmanager.presentation.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.presentation.viewmodel.ProductViewModel

@Composable
fun ProductPortraitScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {

    val products by viewModel.products.collectAsState()

    var name by remember {
        mutableStateOf("")
    }
    var showRestockDialog by remember {
        mutableStateOf(false)
    }

    var selectedProduct by remember {
        mutableStateOf<ProductEntity?>(null)
    }

    var restockQty by remember {
        mutableStateOf("")
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var productName by remember {
        mutableStateOf("")
    }

    var salePrice by remember {
        mutableStateOf("")
    }

    var purchasePrice by remember {
        mutableStateOf("")
    }

    var stock by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var editName by remember {
        mutableStateOf("")
    }

    var editPrice by remember {
        mutableStateOf("")
    }

    var editStock by remember {
        mutableStateOf("")
    }


    var editDescription by remember {
        mutableStateOf("")
    }

    var editPurchasePrice by remember {
        mutableStateOf("")
    }

    var editSalePrice by remember {
        mutableStateOf("")
    }

    var editCategory by remember {
        mutableStateOf("")
    }

    Scaffold(

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                }
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter"
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // ton contenu ici





    Column(
        modifier = Modifier.padding(16.dp)
    ) {


        LazyColumn {

            items(products) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text(
                            product.name,
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        Text(
                            "Prix : ${product.price} CFA"
                        )

                        Text(
                            "Stock : ${product.quantity}"
                        )

                        when {

                            product.quantity == 0 -> {

                                Text(
                                    "🚫 Rupture",
                                    color =
                                        MaterialTheme.colorScheme.error
                                )
                            }

                            product.quantity < 5 -> {

                                Text(
                                    "⚠ Stock faible",
                                    color =
                                        androidx.compose.ui.graphics.Color(
                                            0xFFFF9800
                                        )
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Row {

                            OutlinedButton(
                                onClick = {

                                    selectedProduct =
                                        product

                                    showRestockDialog =
                                        true
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.AddBox,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            OutlinedButton(
                                onClick = {

                                    selectedProduct = product

                                    editName = product.name

                                    editDescription =
                                        product.description

                                    editPurchasePrice =
                                        product.purchasePrice.toString()

                                    editSalePrice =
                                        product.price.toString()

                                    editStock =
                                        product.quantity.toString()

                                    editCategory =
                                        product.category

                                    showEditDialog = true
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            OutlinedButton(
                                onClick = {
                                    viewModel.deleteProduct(
                                        product
                                    )
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showRestockDialog) {

        AlertDialog(

            onDismissRequest = {
                showRestockDialog = false
            },

            title = {
                Text("Réapprovisionnement")
            },

            text = {

                Column {

                    Text(
                        selectedProduct?.name ?: ""
                    )

                    OutlinedTextField(
                        value = restockQty,
                        onValueChange = {
                            restockQty = it.filter {
                                    c -> c.isDigit()
                            }
                        },
                        label = {
                            Text("Quantité")
                        }
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        val qty =
                            restockQty.toIntOrNull() ?: 0

                        selectedProduct?.let {

                            viewModel.restockProduct(
                                it,
                                qty
                            )
                        }

                        restockQty = ""

                        showRestockDialog = false
                    }
                ) {

                    Text("Valider")
                }
            }
        )
    }

    if (showAddDialog) {

        AlertDialog(

            onDismissRequest = {
                showAddDialog = false
            },

            title = {
                Text("Nouveau Produit")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = productName,
                        onValueChange = {
                            productName = it
                        },
                        label = {
                            Text("Nom produit")
                        }
                    )

                    OutlinedTextField(
                        value = salePrice,
                        onValueChange = {
                            salePrice =
                                it.filter { c ->
                                    c.isDigit()
                                }
                        },
                        label = {
                            Text("Prix de vente")
                        }
                    )

                    OutlinedTextField(
                        value = purchasePrice,
                        onValueChange = {
                            purchasePrice =
                                it.filter { c ->
                                    c.isDigit()
                                }
                        },
                        label = {
                            Text("Prix d'achat")
                        }
                    )

                    OutlinedTextField(
                        value = stock,
                        onValueChange = {
                            stock =
                                it.filter { c ->
                                    c.isDigit()
                                }
                        },
                        label = {
                            Text("Stock initial")
                        }
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = {
                            category = it
                        },
                        label = {
                            Text("Catégorie")
                        }
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        label = {
                            Text("Description")
                        }
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        viewModel.addProduct(

                            name = productName,

                            description = description,

                            purchasePrice =
                                purchasePrice.toDoubleOrNull()
                                    ?: 0.0,

                            price =
                                salePrice.toDoubleOrNull()
                                    ?: 0.0,

                            quantity =
                                stock.toIntOrNull()
                                    ?: 0,

                            category = category
                        )

                        productName = ""
                        salePrice = ""
                        purchasePrice = ""
                        stock = ""
                        category = ""
                        description = ""

                        showAddDialog = false
                    }
                ) {

                    Text("Enregistrer")
                }
            }
        )
    }
}
    }

    if (showEditDialog) {

        AlertDialog(

            onDismissRequest = {
                showEditDialog = false
            },

            title = {
                Text("Modifier produit")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = editName,
                        onValueChange = {
                            editName = it
                        },
                        label = {
                            Text("Nom")
                        }
                    )

                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = {
                            editDescription = it
                        },
                        label = {
                            Text("Description")
                        }
                    )

                    OutlinedTextField(
                        value = editPurchasePrice,
                        onValueChange = {
                            editPurchasePrice =
                                it.filter { c ->
                                    c.isDigit()
                                }
                        },
                        label = {
                            Text("Prix d'achat")
                        }
                    )

                    OutlinedTextField(
                        value = editSalePrice,
                        onValueChange = {
                            editSalePrice =
                                it.filter { c ->
                                    c.isDigit()
                                }
                        },
                        label = {
                            Text("Prix de vente")
                        }
                    )

                    OutlinedTextField(
                        value = editStock,
                        onValueChange = {
                            editStock =
                                it.filter { c ->
                                    c.isDigit()
                                }
                        },
                        label = {
                            Text("Stock")
                        }
                    )

                    OutlinedTextField(
                        value = editCategory,
                        onValueChange = {
                            editCategory = it
                        },
                        label = {
                            Text("Catégorie")
                        }
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        selectedProduct?.let {

                            viewModel.updateProduct(

                                it.copy(

                                    name = editName,

                                    description =
                                        editDescription,

                                    purchasePrice =
                                        editPurchasePrice
                                            .toDoubleOrNull()
                                            ?: 0.0,

                                    price =
                                        editSalePrice
                                            .toDoubleOrNull()
                                            ?: 0.0,

                                    quantity =
                                        editStock
                                            .toIntOrNull()
                                            ?: 0,

                                    category =
                                        editCategory
                                )
                            )
                        }

                        showEditDialog = false
                    }
                ) {

                    Text("Enregistrer")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showEditDialog = false
                    }
                ) {

                    Text("Annuler")
                }
            }
        )
    }
}