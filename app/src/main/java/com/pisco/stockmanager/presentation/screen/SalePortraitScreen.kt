package com.pisco.stockmanager.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel
import com.pisco.stockmanager.utils.formatCfa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalePortraitScreen(
    viewModel: SaleViewModel = hiltViewModel(),
    navController: NavController
) {

    val products by viewModel.products.collectAsState()
    val total by viewModel.total.collectAsState()

    var search by remember {
        mutableStateOf("")
    }
    var selectedProduct by remember {
        mutableStateOf<ProductEntity?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var quantity by remember {
        mutableStateOf("")
    }

    val cart by viewModel.cart.collectAsState()
    var showCart by remember {
        mutableStateOf(false)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Caisse")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            expanded = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = {
                           // navController.navigate("cart")
                            showCart = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Panier"
                        )
                    }
                }
            )

            DropdownMenu(

                expanded = expanded,

                onDismissRequest = {
                    expanded = false
                }

            ) {

//                DropdownMenuItem(
//
//                    leadingIcon = {
//
//                        Icon(
//                            Icons.Default.Dashboard,
//                            contentDescription = null
//                        )
//                    },
//                    text = {
//                        Text("Dashboard")
//                    },
//
//                    onClick = {
//
//                        expanded = false
//
//                        navController.navigate(
//                            "dashboard"
//                        )
//                    }
//                )

                DropdownMenuItem(

                    leadingIcon = {

                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text("Produits")
                    },

                    onClick = {

                        expanded = false

                        navController.navigate(
                            "products"
                        )
                    }
                )

//                DropdownMenuItem(
//
//                    leadingIcon = {
//
//                        Icon(
//                            Icons.Default.People,
//                            contentDescription = null
//                        )
//                    },
//                    text = {
//                        Text("Clients")
//                    },
//
//                    onClick = {
//
//                        expanded = false
//
//                        navController.navigate(
//                            "clients"
//                        )
//                    }
//                )

                DropdownMenuItem(

                    leadingIcon = {

                        Icon(
                            Icons.Default.History,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text("Historique")
                    },

                    onClick = {

                        expanded = false

                        navController.navigate(
                            "history"
                        )
                    }
                )

                DropdownMenuItem(

                    leadingIcon = {

                        Icon(
                            Icons.Default.Save,
                            contentDescription = null
                        )
                    },

                    text = {
                        Text("Sauvegarder")
                    },

                    onClick = {
                        expanded = false
                        navController.navigate("backup")
                    }
                )

                DropdownMenuItem(

                    leadingIcon = {

                        Icon(
                            Icons.Default.Restore,
                            contentDescription = null
                        )
                    },

                    text = {
                        Text("Restaurer")
                    },

                    onClick = {
                        expanded = false
                        navController.navigate("restore")
                    }
                )
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Total = "+formatCfa(total)
                    )

//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//
//                        }
//                    ) {
//
//                        Text("PAYER")
//                    }
                }
            }

            OutlinedTextField(
                value = search,
                onValueChange = {
                    search = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true,
                label = {
                    Text("Rechercher")
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(
                    products.filter {

                        it.name.contains(
                            search,
                            true
                        )
                    }
                ) { product ->

                    ProductRow(
                        product = product,
                        onClick = {

                            selectedProduct = product

                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showDialog) {

        AlertDialog(

            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text("Quantité")
            },

            text = {

                OutlinedTextField(

                    value = quantity,

                    onValueChange = {

                        quantity =
                            it.filter { c ->
                                c.isDigit()
                            }
                    },

                    label = {
                        Text("Quantité")
                    }
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        selectedProduct?.let {

                            viewModel.addToCart(

                                it,

                                quantity.toIntOrNull()
                                    ?: 1
                            )
                        }

                        quantity = ""

                        showDialog = false
                    }
                ) {

                    Text("Ajouter")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {

                    Text("Annuler")
                }
            }
        )
    }

    if (showCart) {

        ModalBottomSheet(
            onDismissRequest = {
                showCart = false
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {

                Text(
                    "Panier",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Total : ${formatCfa(total)}",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = cart.isNotEmpty(),
                                onClick = {
                                    viewModel.validateSale(
                                        clientId = 0
                                    )
                                    showCart = false
                            }
                        ) {

                            Text("Valider Facture")
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                if (cart.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text("Panier vide")
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {

                        items(cart) { item ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = item.product.name,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.decreaseQuantity(item.product.id)
                                    }
                                ) {
                                    Text("-")
                                }

                                Text(
                                    text = item.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.increaseQuantity(item.product.id)
                                    }
                                ) {
                                    Text("+")
                                }

                                Text(
                                    text = formatCfa(
                                        item.product.price * item.quantity
                                    ),
                                    modifier = Modifier.width(90.dp)
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.removeFromCart(
                                            item.product.id
                                        )
                                    }
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Supprimer"
                                    )
                                }
                            }
                            HorizontalDivider()

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductRow(
    product: ProductEntity,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
        onClick = {

            if (product.quantity > 0) {
                onClick()
            }
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        randomProductColor(product.id)
                    )
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = product.name,
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Stock : ${product.quantity}"
                )

                if (product.quantity == 0) {

                    Text(
                        text = "🚫 RUPTURE",
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(
                text = formatCfa(product.price),
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

fun randomProductColor(
    id: Int
): Color {

    val colors = listOf(

        Color(0xFFE91E63),
        Color(0xFF2196F3),
        Color(0xFF4CAF50),
        Color(0xFFFF9800),
        Color(0xFF9C27B0),
        Color(0xFFF44336),
        Color(0xFF009688)
    )

    return colors[
        id % colors.size
    ]
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel()
) {

    val cart by viewModel.cart.collectAsState()
    val total by viewModel.total.collectAsState()
    var showCart by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Panier")
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(cart) { item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            "${item.product.name} x${item.quantity}"
                        )

                        Text(
                            formatCfa(
                                item.product.price *
                                        item.quantity
                            )
                        )
                    }
                }
            }

            Text(
                text = "Total : ${formatCfa(total)}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


