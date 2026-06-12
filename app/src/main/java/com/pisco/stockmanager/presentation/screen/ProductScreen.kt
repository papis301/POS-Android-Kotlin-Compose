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
import com.pisco.stockmanager.presentation.viewmodel.ProductViewModel

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {

    val products by viewModel.products.collectAsState()

    var name by remember {
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
                        viewModel.deleteProduct(product)
                    }
                ) {
                    Text("Supprimer")
                }
            }
        }
    }
}