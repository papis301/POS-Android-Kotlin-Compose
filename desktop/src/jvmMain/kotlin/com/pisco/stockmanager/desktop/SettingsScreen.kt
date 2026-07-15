package com.pisco.stockmanager.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.desktop.utils.formatDate
import com.pisco.stockmanager.shared.data.FirebaseService
import com.pisco.stockmanager.shared.data.ModuleLicenseEntity
import com.pisco.stockmanager.shared.data.ModuleLicenseDao
import com.pisco.stockmanager.shared.data.preferences.AppThemeMode
import com.pisco.stockmanager.shared.data.preferences.Currency
import com.pisco.stockmanager.shared.domain.SettingsRepository
import com.pisco.stockmanager.shared.utils.getDeviceUUID
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.formatDate
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {}
) {
    val settingsRepository = koinInject<SettingsRepository>()
    val licenseDao = koinInject<ModuleLicenseDao>()
    val firebaseService = koinInject<FirebaseService>() // Injecté via Koin
    val scope = rememberCoroutineScope()

    var settings by remember { mutableStateOf(settingsRepository.getSettings()) }

    var storeName by remember(settings.storeName) { mutableStateOf(settings.storeName) }
    var storeAddress by remember(settings.storeAddress) { mutableStateOf(settings.storeAddress) }
    var storePhone by remember(settings.storePhone) { mutableStateOf(settings.storePhone) }

    var currencyMenuExpanded by remember { mutableStateOf(false) }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    // État d'abonnement / Firebase
    var isSubscribing by remember { mutableStateOf(false) }
    var subscriptionStatusMessage by remember { mutableStateOf<String?>(null) }

    // Liste des licences lues en base de données locale
    var activeLicenses by remember { mutableStateOf<Map<String, ModuleLicenseEntity>>(emptyMap()) }

    // Charger les licences de la base au démarrage
    LaunchedEffect(Unit) {
        activeLicenses = licenseDao.getAllLicenses().associateBy { it.moduleId }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    // Fonction pour activer ou désactiver manuellement un module
    fun toggleModuleActivation(moduleId: String, moduleName: String) {
        scope.launch {
            val existing = activeLicenses[moduleId]
            if (existing != null && existing.isActivated) {
                // Désactivation
                val updated = existing.copy(isActivated = false)
                licenseDao.insertOrUpdate(updated)
                snackbarMessage = "Module désactivé"
            } else {
                // Activation pour une période de 30 jours
                val now = System.currentTimeMillis()
                val thirtyDaysInMs = 30L * 24 * 60 * 60 * 1000
                val newLicense = ModuleLicenseEntity(
                    moduleId = moduleId,
                    isActivated = true,
                    activationDate = now,
                    expirationDate = now + thirtyDaysInMs,
                    lastCheckedTimestamp = now
                )
                licenseDao.insertOrUpdate(newLicense)
                snackbarMessage = "Module activé pour 30 jours !"
            }
            // Rafraîchir l'UI
            activeLicenses = licenseDao.getAllLicenses().associateBy { it.moduleId }
        }
    }

    fun updateTheme(themeMode: AppThemeMode) {
        settingsRepository.setThemeMode(themeMode)
        settings = settings.copy(themeMode = themeMode)
        snackbarMessage = "Thème mis à jour"
    }

    fun updateCurrency(currency: Currency) {
        settingsRepository.setCurrency(currency)
        settings = settings.copy(currency = currency)
        snackbarMessage = "Devise mise à jour"
    }

    fun saveStoreInfo() {
        settingsRepository.saveStoreInfo(
            name = storeName.trim(),
            address = storeAddress.trim(),
            phone = storePhone.trim()
        )
        settings = settings.copy(
            storeName = storeName.trim(),
            storeAddress = storeAddress.trim(),
            storePhone = storePhone.trim()
        )
        snackbarMessage = "Informations enregistrées"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.primary // Utilise le vert du thème de l'application
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // En-tête avec fond dynamique vert
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }

                    Text(
                        text = "Paramètres",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            }

            // Contenu avec fond arrondi blanc/sombre adaptatif
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    )
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                // ================= SECTION : ENREGISTREMENT APPAREIL (ABONNEMENT) =================
                Text(
                    text = "Abonnement Cloud",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Enregistrez cet appareil sur nos serveurs pour activer automatiquement vos licences achetées.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = {
                                isSubscribing = true
                                scope.launch {
                                    val deviceId = getDeviceUUID(null) // Récupération de l'UUID natif (Desktop/Android)
                                    val success = firebaseService.registerDeviceToFirebase(deviceId)
                                    isSubscribing = false
                                    subscriptionStatusMessage = if (success) {
                                        "Appareil enregistré avec succès ! UUID : $deviceId"
                                    } else {
                                        "Erreur lors de l'enregistrement. Vérifiez votre connexion."
                                    }
                                }
                            },
                            enabled = !isSubscribing,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isSubscribing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.CloudUpload, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("S'abonner avec cet appareil")
                            }
                        }

                        subscriptionStatusMessage?.let { message ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                // ================= SECTION : GESTION DES MODULES PAYANTS =================
                Text(
                    text = "Modules & Licences Professionnelles",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Activez ou gérez l'état de vos extensions payantes.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Liste complète avec le module DATE_FILTER inclus !
                val modulesList = listOf(
                    "DATE_FILTER" to "Filtrage des ventes par date (Aujourd'hui, Semaine, Mois, Période)",
                    "SEARCH_PRO" to "Recherche produit avancée (Code-barres, Catégories)",
                    "STOCK_ALERTS" to "Gestion & Alertes de stock bas",
                    "ESC_POS_PRINT" to "Impression thermique directe (ESC/POS)"
                )

                modulesList.forEach { (id, name) ->
                    val license = activeLicenses[id]
                    val isActive = license?.isActivated == true && license.expirationDate > System.currentTimeMillis()

                    ModuleLicenseItem(
                        moduleName = name,
                        isActive = isActive,
                        expirationDate = license?.expirationDate,
                        onToggleActivation = { toggleModuleActivation(id, name) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                // Informations du magasin
                Text(
                    text = "Informations du magasin",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nom du magasin") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = storeAddress,
                    onValueChange = { storeAddress = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Adresse") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = storePhone,
                    onValueChange = { storePhone = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Téléphone") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { saveStoreInfo() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Enregistrer les informations")
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                // Devise
                Text(
                    text = "Devise",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = currencyMenuExpanded,
                    onExpandedChange = { currencyMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = "${settings.currency.code} (${settings.currency.symbol})",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Devise utilisée") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = currencyMenuExpanded
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = currencyMenuExpanded,
                        onDismissRequest = { currencyMenuExpanded = false }
                    ) {
                        Currency.entries.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                    Text("${currency.code} (${currency.symbol})")
                                },
                                onClick = {
                                    updateCurrency(currency)
                                    currencyMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                // Apparence
                Text(
                    text = "Apparence",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    ThemeOptionDesktop(
                        label = "Clair",
                        selected = settings.themeMode == AppThemeMode.LIGHT,
                        onClick = { updateTheme(AppThemeMode.LIGHT) }
                    )

                    ThemeOptionDesktop(
                        label = "Sombre",
                        selected = settings.themeMode == AppThemeMode.DARK,
                        onClick = { updateTheme(AppThemeMode.DARK) }
                    )

                    ThemeOptionDesktop(
                        label = "Système (par défaut)",
                        selected = settings.themeMode == AppThemeMode.SYSTEM,
                        onClick = { updateTheme(AppThemeMode.SYSTEM) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(24.dp))

                // Assistance
                Text(
                    text = "Assistance",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        snackbarMessage = "Contact & Support - Fonctionnalité à venir"
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Contact & Support")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ================= COMPOSABLE : Ligne de licence par module (Theme Friendly) =================
@Composable
fun ModuleLicenseItem(
    moduleName: String,
    isActive: Boolean,
    expirationDate: Long?,
    onToggleActivation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = moduleName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface // S'adapte au mode clair ou sombre
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (isActive && expirationDate != null) {
                    Text(
                        text = "Expire le : ${formatDate(expirationDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary // Vert primaire pour l'activation
                    )
                } else {
                    Text(
                        text = "Désactivé / Expiré",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error // Rouge très lisible
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onToggleActivation,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary // Bouton vert d'activation
                    }
                )
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Désactiver")
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Activer")
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionDesktop(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                else
                    Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = label,
            color = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}