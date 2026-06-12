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
import androidx.navigation.NavController
import com.pisco.stockmanager.data.local.SaleEntity
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel
import com.pisco.stockmanager.utils.formatCfa
import com.pisco.stockmanager.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SaleHistoryScreen(
    navController: NavController,
    viewModel: SaleViewModel = hiltViewModel()
) {

    val sales by viewModel.sales.collectAsState()
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Historique des ventes",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn {

            items(sales.reversed()) { sale ->

                SaleCard(
                    sale = sale,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun SaleCard(
    sale: SaleEntity,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = "Facture #${sale.id}",
                style =
                    MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text =
                    "Client ID : ${sale.clientId}"
            )

            Text(
                text =
                    "Montant : ${
                        formatCfa(sale.total)
                    } CFA"
            )

            Text(
                text =
                    "Date : ${
                        formatDate(
                            sale.createdAt
                        )
                    }"
            )
            Button(
                onClick = {

                    navController.navigate(
                        "sale_detail/${sale.id}"
                    )
                }
            ) {

                Text("Voir détail")
            }
        }
    }
}