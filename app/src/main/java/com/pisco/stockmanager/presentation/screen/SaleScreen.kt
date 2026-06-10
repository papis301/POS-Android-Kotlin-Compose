package com.pisco.stockmanager.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pisco.stockmanager.data.local.ClientEntity
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreen(
    viewModel: SaleViewModel = hiltViewModel()
) {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Caisse",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // CLIENT

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = selectedClient?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Client")
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                clients.forEach { client ->

                    DropdownMenuItem(
                        text = {
                            Text(client.name)
                        },
                        onClick = {

                            selectedClient = client

                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            "Produits",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(products) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
                            onClick = {

                                selectedProduct = product

                                showDialog = true
                            }
                        ) {

                            Text("Ajouter")
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            "Panier",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier.height(180.dp)
        ) {

            items(cart) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            "${item.product.name} x${item.quantity}"
                        )

                        Text(
                            "${item.product.price * item.quantity} CFA"
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Total : $total CFA",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row {

            RadioButton(
                selected = paymentMode == "CASH",
                onClick = {
                    viewModel.setPaymentMode("CASH")
                }
            )

            Text("Cash")

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            RadioButton(
                selected = paymentMode == "CREDIT",
                onClick = {
                    viewModel.setPaymentMode("CREDIT")
                }
            )

            Text("Crédit")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                selectedClient?.let {

                    viewModel.validateSale(
                        it.id
                    )
                }
            }
        ) {

            Text("Valider Facture")
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
                        quantity = it
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
                                quantity.toIntOrNull() ?: 1
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