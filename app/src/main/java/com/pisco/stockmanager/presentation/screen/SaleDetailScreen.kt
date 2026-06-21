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
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
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
                                "${item.productName} x${item.quantity}", color = BluePrimary
                            )

                            Text(
                                formatCfa(
                                    item.quantity *
                                            item.unitPrice
                                ) + " CFA", color = BluePrimary
                            )
                        }

                        HorizontalDivider()
                    }
                }

                Text(
                    text =
                        "Total : ${
                            formatCfa(
                                items.sumOf {
                                    it.quantity *
                                            it.unitPrice
                                }
                            )
                        } CFA",
                    style =
                        MaterialTheme.typography.titleLarge,
                    color = BluePrimary
                )
            }
        }
    }
}