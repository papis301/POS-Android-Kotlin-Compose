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
import com.pisco.stockmanager.data.local.SaleItemEntity
import com.pisco.stockmanager.utils.formatCfa

@Composable
fun SaleDetailScreen(
    saleId: Int,
    items: List<SaleItemEntity>
) {

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
            text = "Facture #$saleId",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

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
                        "${item.productName} x${item.quantity}"
                    )

                    Text(
                        formatCfa(
                            item.quantity *
                                    item.unitPrice
                        ) + " CFA"
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
                MaterialTheme.typography.titleLarge
        )
    }
}