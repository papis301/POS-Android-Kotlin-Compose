package com.pisco.stockmanager.presentation.screen

import android.R.attr.padding
import android.app.Activity
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pisco.stockmanager.data.local.ClientEntity
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.domain.model.CartItem
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel
import com.pisco.stockmanager.utils.formatCfa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sale2Screen(
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel()
){

    var search by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val activity = context as Activity

    val message by viewModel.message.collectAsState()
    LaunchedEffect(message) {

        message?.let {

            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearMessage()
        }
    }

    DisposableEffect(Unit) {

        activity.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {

            activity.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val products by viewModel.products.collectAsState()
    val clients by viewModel.clients.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val total by viewModel.total.collectAsState()
    val paymentMode by viewModel.paymentMode.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    var selectedClient by remember {
        mutableStateOf<ClientEntity?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectedProduct by remember {
        mutableStateOf<ProductEntity?>(null)
    }

    var quantity by remember {
        mutableStateOf("")
    }

    val filteredProducts =
        products.filter {

            it.name.contains(
                search,
                ignoreCase = true
            )
        }

    val listState = rememberLazyListState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 36.dp)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp, end = 4.dp),
                horizontalArrangement =
                    Arrangement.spacedBy(2.dp)
            ) {

                Text(
                    "Caisse",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search = it
                    },
                    modifier = Modifier.width(250.dp).height(56.dp),
                    singleLine = true,
                    label = {
                        Text("Rechercher un produit")
                    }
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                    Button(
                        onClick = {
                            expanded = true
                        }
                    ) {
                        Text("Menu")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text("Dashboard")
                            },
                            onClick = {
                                expanded = false
                                navController.navigate(
                                    "dashboard"
                                )
                            }
                        )

                        DropdownMenuItem(
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

                        DropdownMenuItem(
                            text = {
                                Text("Clients")
                            },
                            onClick = {
                                expanded = false
                                navController.navigate(
                                    "clients"
                                )
                            }
                        )


                        DropdownMenuItem(
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
                    }

            }
        }

        Row(
            modifier = Modifier.weight(1f)
        ) {
            // contenu


            Row(
                modifier = Modifier.weight(1f)
            ) {

                ProductPanel(
                    modifier = Modifier.weight(1f),
                    products = filteredProducts,
                    onAddProduct = { product ->

                        selectedProduct = product

                        showDialog = true
                    }
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Box(
                        modifier = Modifier.weight(2f)
                    ) {
                        CartPanel(
                            modifier = Modifier.fillMaxSize(),
                            cart = cart,

                            onIncrease = { productId ->

                                viewModel.increaseQuantity(
                                    productId
                                )
                            },

                            onDecrease = { productId ->

                                viewModel.decreaseQuantity(
                                    productId
                                )
                            },

                            onRemoveItem = { productId ->

                                viewModel.removeFromCart(
                                    productId
                                )
                            }
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        PaymentPanel(
                            modifier = Modifier.fillMaxSize(),
                            total = total,

                            onValidate = {

                                if (cart.isEmpty()) {

                                    Toast.makeText(
                                        context,
                                        "Le panier est vide",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@PaymentPanel
                                }

                                val clientId =
                                    selectedClient?.id ?: 1

                                viewModel.validateSale(
                                    clientId

                                )

                                Toast.makeText(
                                    context,
                                    "Vente enregistrée avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
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

                        if (it.all { char -> char.isDigit() }) {
                            quantity = it
                        }
                    },

                    label = {
                        Text("Quantité")
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            },

            confirmButton = {

                Button(
                    onClick = {

                        val qty = quantity.toIntOrNull()

                        if (
                            qty != null &&
                            selectedProduct != null &&
                            qty <= selectedProduct!!.quantity
                        ) {

                            viewModel.addToCart(
                                selectedProduct!!,
                                qty
                            )

                            showDialog = false

                        } else {

                            Toast.makeText(
                                context,
                                "Stock insuffisant ou quantité invalide",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                ){

                    Text("Ajouter")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {

                    Text("Annuler")
                }
            }
        )
    }
}



@Composable
fun PaymentPanel(
    total: Double,
    onValidate: () -> Unit,
    modifier: Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {


            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onValidate
            ) {

                Text("Total : ${formatCfa(total)} CFA\n"+
                        "Valider Facture")
            }
        }
    }
}

@Composable
fun CartPanel(
    modifier: Modifier = Modifier,
    cart: List<CartItem>,
    onRemoveItem: (Int) -> Unit,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
) {

    Card(
        modifier = modifier.fillMaxHeight()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            Text(
                text = "Panier",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(cart) { item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(
                                item.product.name
                            )

                        }

                        Row {

                            Column {


                                Row {

                                    Button(
                                        onClick = {

                                            onDecrease(
                                                item.product.id
                                            )
                                        }
                                    ) {
                                        Text("-")
                                    }

                                    Text(
                                        item.quantity.toString(),
                                        modifier =
                                            Modifier.padding(8.dp)
                                    )

                                    Button(
                                        onClick = {

                                            onIncrease(
                                                item.product.id
                                            )
                                        }
                                    ) {
                                        Text("+")
                                    }
                                }
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Button(
                                onClick = {
                                    onRemoveItem(
                                        item.product.id
                                    )
                                }
                            ) {
                                Text("❌")
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

//            Spacer(
//                modifier = Modifier.height(8.dp)
//            )
//
//            Text(
//                text =
//                    "Articles : ${
//                        cart.sumOf { it.quantity }
//                    }",
//                style =
//                    MaterialTheme.typography.titleMedium
//            )


        }
    }
}

@Composable
fun ProductPanel(
    modifier: Modifier = Modifier,
    products: List<ProductEntity>,
    onAddProduct: (ProductEntity) -> Unit
) {
    var search by remember {
        mutableStateOf("")
    }
    Card(
        modifier = modifier.fillMaxHeight()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

//            Text(
//                "Produits",
//                style = MaterialTheme.typography.titleMedium
//            )
//            Spacer(
//                modifier = Modifier.height(8.dp)
//            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )

            LazyColumn {

                items(products) { product ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column {

                            Text(product.name)

                            Text(
                                "${product.price} CFA"
                            )

                            Text(
                                "Stock : ${product.quantity}"
                            )
                        }

                        Button(
                            modifier = Modifier.height(40.dp),
                            onClick = {
                                onAddProduct(product)
                            }
                        ) {
                            Text("Ajouter")
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}




