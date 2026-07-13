package com.pisco.stockmanager.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.desktop.utils.formatDate
import com.pisco.stockmanager.shared.data.SaleEntity
import com.pisco.stockmanager.shared.data.SaleItemEntity
import com.pisco.stockmanager.shared.domain.SaleRepository     // Votre repository des ventes globales
import com.pisco.stockmanager.shared.domain.SaleItemRepository // AJOUT : Votre repository d'items transmis
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentesScreen() {

    val saleRepository = koinInject<SaleRepository>()
    val saleItemRepository = koinInject<SaleItemRepository>() // Injection de votre SaleItemRepository
    val scope = rememberCoroutineScope()

    val sales by saleRepository.getSales().collectAsState(initial = emptyList())
    var searchByInvoice by remember { mutableStateOf("") }

    // Suivi de la facture sélectionnée
    var selectedSaleId by remember { mutableStateOf<Int?>(null) }

    // État local contenant la liste des articles de la facture sélectionnée
    var saleItems by remember { mutableStateOf<List<SaleItemEntity>>(emptyList()) }

    // Dès que selectedSaleId change, on charge les items de façon asynchrone
    LaunchedEffect(selectedSaleId) {
        selectedSaleId?.let { id ->
            saleItems = saleItemRepository.getItemsForSale(id)
        } ?: run {
            saleItems = emptyList()
        }
    }

    val filteredSales = sales.filter {
        it.id.toString().contains(searchByInvoice)
    }

    if (selectedSaleId != null) {
        SaleDetailScreen(
            saleId = selectedSaleId!!.toLong(), // Conversion si SaleDetailScreen prend un Long
            items = saleItems,
            onBack = { selectedSaleId = null },
            onPrintClick = { id ->
                scope.launch {
                    // Logique d'impression desktop
                }
            }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            topBar = {
                TopAppBar(
                    title = { Text("Historique des ventes", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
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
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = searchByInvoice,
                    onValueChange = { searchByInvoice = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true,
                    label = { Text("Rechercher par N° Facture") }
                )

                if (sales.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("Aucune vente enregistrée pour le moment.")
                    }
                } else if (filteredSales.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("Aucune facture ne correspond à « $searchByInvoice ».")
                    }
                } else {
                    LazyColumn {
                        items(filteredSales.reversed()) { sale ->
                            SaleCard(
                                sale = sale,
                                onSeeDetailsClick = { id ->
                                    selectedSaleId = id.toInt() // Ajustement du type Long vers Int
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SaleCard(
    sale: SaleEntity,
    onSeeDetailsClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Facture #${sale.id}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatDate(sale.createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Client ID : ${sale.clientId}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Montant total : ${formatCfa(sale.total)} CFA",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onSeeDetailsClick(sale.id.toLong()) }
            ) {
                Text("Voir détail")
            }
        }
    }
}