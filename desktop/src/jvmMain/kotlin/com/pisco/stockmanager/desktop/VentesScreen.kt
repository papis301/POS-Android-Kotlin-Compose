package com.pisco.stockmanager.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.desktop.utils.formatDate
import com.pisco.stockmanager.shared.data.SaleEntity
import com.pisco.stockmanager.shared.data.SaleItemEntity
import com.pisco.stockmanager.shared.domain.SaleRepository
import com.pisco.stockmanager.shared.domain.SaleItemRepository
import com.pisco.stockmanager.shared.data.ModuleLicenseDao // AJOUT
import com.pisco.stockmanager.shared.data.ModuleLicenseEntity // AJOUT
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

enum class DateFilterType {
    ALL, TODAY, WEEK, MONTH, CUSTOM
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentesScreen() {
    val saleRepository = koinInject<SaleRepository>()
    val saleItemRepository = koinInject<SaleItemRepository>()
    val licenseDao = koinInject<ModuleLicenseDao>() // AJOUT : Injection du DAO de licences
    val scope = rememberCoroutineScope()

    val sales by saleRepository.getSales().collectAsState(initial = emptyList())

    var searchByInvoice by remember { mutableStateOf("") }
    var selectedSaleId by remember { mutableStateOf<Int?>(null) }
    var saleItems by remember { mutableStateOf<List<SaleItemEntity>>(emptyList()) }

    var activeFilter by remember { mutableStateOf(DateFilterType.ALL) }
    var customStartDate by remember { mutableStateOf(LocalDate.now()) }
    var customEndDate by remember { mutableStateOf(LocalDate.now()) }
    var showCustomDateDialog by remember { mutableStateOf(false) }

    // --- ÉTAT DE LA LICENCE ---
    var license by remember { mutableStateOf<ModuleLicenseEntity?>(null) }
    var showLicenseBlockedDialog by remember { mutableStateOf(false) }

    // Charger la licence au démarrage
    LaunchedEffect(Unit) {
        license = licenseDao.getLicense("DATE_FILTER")
    }

    // Vérification de la validité de la licence (avec sécurité anti-triche temps)
    val isDateFilterUnlocked = remember(license) {
        val current = System.currentTimeMillis()
        license != null &&
                license!!.isActivated &&
                license!!.expirationDate > current &&
                current >= license!!.lastCheckedTimestamp
    }

    LaunchedEffect(selectedSaleId) {
        selectedSaleId?.let { id ->
            saleItems = saleItemRepository.getItemsForSale(id)
        } ?: run {
            saleItems = emptyList()
        }
    }

    // Filtrage des ventes
    val filteredSales = remember(sales, searchByInvoice, activeFilter, customStartDate, customEndDate) {
        sales.filter { sale ->
            val matchesInvoice = sale.id.toString().contains(searchByInvoice)
            if (!matchesInvoice) return@filter false

            val saleLocalDate = Instant.ofEpochMilli(sale.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val today = LocalDate.now()

            // Si l'utilisateur tente d'utiliser un filtre payant sans licence valide,
            // on le force à rester sur ALL.
            val appliedFilter = if (!isDateFilterUnlocked && activeFilter != DateFilterType.ALL) {
                DateFilterType.ALL
            } else {
                activeFilter
            }

            when (appliedFilter) {
                DateFilterType.ALL -> true
                DateFilterType.TODAY -> saleLocalDate.isEqual(today)
                DateFilterType.WEEK -> {
                    val sevenDaysAgo = today.minusDays(7)
                    !saleLocalDate.isBefore(sevenDaysAgo) && !saleLocalDate.isAfter(today)
                }
                DateFilterType.MONTH -> {
                    saleLocalDate.year == today.year && saleLocalDate.month == today.month
                }
                DateFilterType.CUSTOM -> {
                    !saleLocalDate.isBefore(customStartDate) && !saleLocalDate.isAfter(customEndDate)
                }
            }
        }
    }

    if (selectedSaleId != null) {
        SaleDetailScreen(
            saleId = selectedSaleId!!.toLong(),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchByInvoice,
                        onValueChange = { searchByInvoice = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        label = { Text("Rechercher par N° Facture") }
                    )

                    // On affiche le bouton de date personnalisée uniquement si déverrouillé
                    if (activeFilter == DateFilterType.CUSTOM && isDateFilterUnlocked) {
                        OutlinedButton(
                            onClick = { showCustomDateDialog = true },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Période")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("$customStartDate au $customEndDate")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- SECTION DES CHIPS DE FILTRAGE ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        DateFilterType.ALL to "Toutes",
                        DateFilterType.TODAY to "Aujourd'hui",
                        DateFilterType.WEEK to "Cette semaine",
                        DateFilterType.MONTH to "Ce mois",
                        DateFilterType.CUSTOM to "Période"
                    ).forEach { (filterType, label) ->
                        val isSelected = activeFilter == filterType

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (filterType == DateFilterType.ALL) {
                                    activeFilter = filterType
                                } else {
                                    // Si l'utilisateur clique sur un filtre payant
                                    if (isDateFilterUnlocked) {
                                        activeFilter = filterType
                                        if (filterType == DateFilterType.CUSTOM) {
                                            showCustomDateDialog = true
                                        }
                                    } else {
                                        // Blocage : On affiche la boîte de dialogue d'achat/activation
                                        showLicenseBlockedDialog = true
                                    }
                                }
                            },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(label)
                                    // Petit cadenas visuel sur les filtres verrouillés !
                                    if (filterType != DateFilterType.ALL && !isDateFilterUnlocked) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Verrouillé",
                                            modifier = Modifier.size(12.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (sales.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aucune vente enregistrée pour le moment.")
                    }
                } else if (filteredSales.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aucune facture ne correspond aux critères sélectionnés.")
                    }
                } else {
                    LazyColumn {
                        items(filteredSales.reversed()) { sale ->
                            SaleCard(
                                sale = sale,
                                onSeeDetailsClick = { id ->
                                    selectedSaleId = id.toInt()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // --- DIALOGUE DE BLOCAGE DE LICENCE (DATE FILTER) ---
    if (showLicenseBlockedDialog) {
        AlertDialog(
            onDismissRequest = { showLicenseBlockedDialog = false },
            icon = { Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp)) },
            title = { Text("Module Premium requis") },
            text = {
                Text("Le filtrage avancé des ventes par date (Aujourd'hui, Semaine, Mois, Période personnalisée) est une fonctionnalité du Pack Pro.\n\nVeuillez vous rendre dans les Paramètres de l'application pour activer ce module.")
            },
            confirmButton = {
                Button(
                    onClick = { showLicenseBlockedDialog = false }
                ) {
                    Text("D'accord")
                }
            }
        )
    }

    // --- DIALOGUE PERIODE PERSONNALISEE ---
    if (showCustomDateDialog && isDateFilterUnlocked) {
        var startInput by remember { mutableStateOf(customStartDate.toString()) }
        var endInput by remember { mutableStateOf(customEndDate.toString()) }
        var inputError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showCustomDateDialog = false },
            title = { Text("Sélectionner la période (AAAA-MM-JJ)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startInput,
                        onValueChange = { startInput = it; inputError = false },
                        label = { Text("Date de début") },
                        placeholder = { Text("Ex: 2026-07-14") }
                    )
                    OutlinedTextField(
                        value = endInput,
                        onValueChange = { endInput = it; inputError = false },
                        label = { Text("Date de fin") },
                        placeholder = { Text("Ex: 2026-07-20") }
                    )
                    if (inputError) {
                        Text(
                            text = "Format invalide (AAAA-MM-JJ) ou début après fin",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            val parsedStart = LocalDate.parse(startInput)
                            val parsedEnd = LocalDate.parse(endInput)
                            if (!parsedStart.isAfter(parsedEnd)) {
                                customStartDate = parsedStart
                                customEndDate = parsedEnd
                                showCustomDateDialog = false
                            } else {
                                inputError = true
                            }
                        } catch (e: Exception) {
                            inputError = true
                        }
                    }
                ) {
                    Text("Valider")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDateDialog = false }) {
                    Text("Annuler")
                }
            }
        )
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