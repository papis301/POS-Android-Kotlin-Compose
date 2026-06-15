package com.pisco.stockmanager.presentation.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.presentation.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
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

    val context = LocalContext.current
    val activity = context as Activity
    DisposableEffect(Unit) {

        activity.requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {

            activity.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("Nom produit")
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(
            onClick = {

                viewModel.addProduct(
                    name,
                    1000.0,
                    1
                )

                name = ""
            }
        ) {

            Text("Ajouter")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn {

            items(products) { product ->

                Text(
                    text = product.name,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {

                        selectedProduct = product

                        showRestockDialog = true
                    }
                ) {
                    Text("Stock +")
                }
                Button(
                    onClick = {
                        viewModel.deleteProduct(product)
                    }
                ) {
                    Text("Supprimer")
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
}