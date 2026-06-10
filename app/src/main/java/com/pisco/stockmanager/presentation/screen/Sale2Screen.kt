package com.pisco.stockmanager.presentation.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pisco.stockmanager.data.local.ClientEntity
import com.pisco.stockmanager.data.local.ProductEntity
import com.pisco.stockmanager.domain.model.CartItem
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sale2Screen(
    viewModel: SaleViewModel = hiltViewModel()
){

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

        Row(
            modifier = Modifier.weight(1f)
        ) {

            ProductPanel(
                modifier = Modifier.weight(1f),
                products = products,
                onAddProduct = { product ->

                    selectedProduct = product

                    showDialog = true
                }
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            CartPanel(
                modifier = Modifier.weight(1f),
                cart = cart
            )
        }

        PaymentPanel(  total = total,
            paymentMode = paymentMode,

            onCashClick = {
                viewModel.setPaymentMode("CASH")
            },

            onCreditClick = {
                viewModel.setPaymentMode("CREDIT")
            },

            onValidate = {

                selectedClient?.let {

                    viewModel.validateSale(
                        it.id
                    )
                }
            })
    }


}

@Composable
fun PaymentPanel(
    total: Double,
    paymentMode: String,
    onCashClick: () -> Unit,
    onCreditClick: () -> Unit,
    onValidate: () -> Unit
) {

    Card {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                "Total : $total CFA",
                style = MaterialTheme.typography.headlineSmall
            )

            Row {

                RadioButton(
                    selected =
                        paymentMode == "CASH",
                    onClick = onCashClick
                )

                Text("Cash")

                RadioButton(
                    selected =
                        paymentMode == "CREDIT",
                    onClick = onCreditClick
                )

                Text("Crédit")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onValidate
            ) {

                Text("Valider Facture")
            }
        }
    }
}

@Composable
fun CartPanel(
    modifier: Modifier = Modifier,
    cart: List<CartItem>
) {

    Card(
        modifier = modifier.fillMaxHeight()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                "Panier",
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn {

                items(cart) { item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            "${item.product.name} x${item.quantity}"
                        )

                        Text(
                            "${item.product.price * item.quantity}"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductPanel(
    modifier: Modifier = Modifier,
    products: List<ProductEntity>,
    onAddProduct: (ProductEntity) -> Unit
) {

    Card(
        modifier = modifier.fillMaxHeight()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                "Produits",
                style = MaterialTheme.typography.titleLarge
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
                }
            }
        }
    }
}