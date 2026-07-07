package com.pisco.stockmanager.presentation.screen

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.presentation.viewmodel.ProductViewModel
import com.pisco.stockmanager.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPortraitScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    navController: NavController
) {

    val products by viewModel.products.collectAsState()

    var search by remember {
        mutableStateOf("")
    }

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

        containerColor = BluePrimary,

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Produits",
                        color = Color.White
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },

        floatingActionButton = {

            FloatingActionButton(

                onClick = {
                    showAddDialog = true
                },

                containerColor = BluePrimary,
                contentColor = Color.White

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
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
        ) {

            // ton contenu ici

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Rechercher un produit")
                    }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                LazyColumn {

                    items(
                        products.filter {

                            it.name.contains(
                                search,
                                true
                            )
                        }
                    ) { product ->

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

                var nameError by remember {
                    mutableStateOf(false)
                }

                var priceError by remember {
                    mutableStateOf(false)
                }

                var stockError by remember {
                    mutableStateOf(false)
                }

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

                                    nameError = false
                                },
                                isError = nameError,
                                label = {
                                    Text("Nom produit *")
                                }
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            OutlinedTextField(
                                value = salePrice,
                                onValueChange = {

                                    salePrice =
                                        it.filter { c ->
                                            c.isDigit()
                                                    || c == '.'
                                        }

                                    priceError = false
                                },
                                isError = priceError,
                                label = {
                                    Text("Prix de vente *")
                                }
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            OutlinedTextField(
                                value = stock,

                                onValueChange = {

                                    stock =
                                        it.filter {
                                                c ->
                                            c.isDigit()
                                        }

                                    stockError = false
                                },

                                isError = stockError,

                                label = {
                                    Text("Stock initial *")
                                }
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
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

                            Spacer(
                                modifier = Modifier.height(8.dp)
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

                            if (nameError) {

                                Text(
                                    text = "Le nom est obligatoire",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            if (priceError) {

                                Text(
                                    text = "Prix invalide",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            if (stockError) {

                                Text(
                                    text =
                                        "Le stock initial doit être supérieur à 0",

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                            }
                        }
                    },

                    confirmButton = {

                        Button(

                            onClick = {

                                nameError =
                                    productName.isBlank()

                                priceError =
                                    salePrice.toDoubleOrNull() == null
                                            ||
                                            salePrice.toDouble() <= 0

                                stockError =
                                    stock.toIntOrNull() == null
                                            ||
                                            stock.toInt() <= 0

                                if (
                                    !nameError &&
                                    !priceError  &&
                                    !stockError
                                ) {

                                    viewModel.addProduct(

                                        name =
                                            productName.trim(),

                                        description =
                                            description.trim(),

                                        purchasePrice =
                                            purchasePrice
                                                .toDoubleOrNull()
                                                ?: 0.0,

                                        price =
                                            salePrice
                                                .toDouble(),

                                        quantity =
                                            stock
                                                .toIntOrNull()
                                                ?: 0,

                                        category =
                                            category.trim()
                                    )

                                    productName = ""
                                    salePrice = ""
                                    purchasePrice = ""
                                    stock = ""
                                    category = ""
                                    description = ""

                                    showAddDialog = false
                                }
                            }

                        ) {

                            Text("Ajouter")
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