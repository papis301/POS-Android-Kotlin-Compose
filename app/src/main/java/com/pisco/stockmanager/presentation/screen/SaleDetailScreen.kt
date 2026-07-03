package com.pisco.stockmanager.presentation.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pisco.stockmanager.data.local.SaleItemEntity
import com.pisco.stockmanager.presentation.viewmodel.SaleViewModel
import com.pisco.stockmanager.ui.theme.BluePrimary
import com.pisco.stockmanager.utils.formatCfa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleDetailScreen(
    saleId: Int,
    items: List<SaleItemEntity>,
    navController: NavController? = null
) {

    val total =
        items.sumOf {
            it.quantity * it.unitPrice
        }

    Scaffold(

        containerColor = BluePrimary,

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Facture #$saleId",
                        color = Color.White
                    )
                },

                navigationIcon = {

                    if (navController != null) {

                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Retour",
                                tint = Color.White
                            )
                        }
                    }
                },

                actions = {

                    if (navController != null) {

                        IconButton(
                            onClick = {

                                navController.navigate(
                                    "printer/$saleId"
                                )
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Imprimer",
                                tint = Color.White
                            )
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
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
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(items) { item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "${item.productName} x${item.quantity}",
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = formatCfa(
                                item.quantity *
                                        item.unitPrice
                            ) + " CFA",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    HorizontalDivider()
                }
            }

            Text(
                text =
                    "Total : ${
                        formatCfa(total)
                    } CFA",
                color = MaterialTheme.colorScheme.onSurface,
                style =
                    MaterialTheme.typography.titleLarge
            )
        }
    }
}