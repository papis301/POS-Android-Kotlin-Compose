// desktop/src/main/kotlin/com/pisco/stockmanager/desktop/screen/SettingsScreen.kt
package com.pisco.stockmanager.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pisco.stockmanager.desktop.theme.BluePrimary
import com.pisco.stockmanager.shared.data.preferences.AppThemeMode
import com.pisco.stockmanager.shared.data.preferences.Currency
import com.pisco.stockmanager.shared.domain.SettingsRepository
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {}
) {
    val settingsRepository = koinInject<SettingsRepository>()
    val scope = rememberCoroutineScope()

    var settings by remember { mutableStateOf(settingsRepository.getSettings()) }

    var storeName by remember(settings.storeName) {
        mutableStateOf(settings.storeName)
    }

    var storeAddress by remember(settings.storeAddress) {
        mutableStateOf(settings.storeAddress)
    }

    var storePhone by remember(settings.storePhone) {
        mutableStateOf(settings.storePhone)
    }

    var currencyMenuExpanded by remember {
        mutableStateOf(false)
    }

    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            snackbarMessage = null
        }
    }

    // Fonction pour mettre à jour le thème
    fun updateTheme(themeMode: AppThemeMode) {
        settingsRepository.setThemeMode(themeMode)
        settings = settings.copy(themeMode = themeMode)
        snackbarMessage = "Thème mis à jour"
    }

    // Fonction pour mettre à jour la devise
    fun updateCurrency(currency: Currency) {
        settingsRepository.setCurrency(currency)
        settings = settings.copy(currency = currency)
        snackbarMessage = "Devise mise à jour"
    }

    // Fonction pour sauvegarder les infos du magasin
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
        containerColor = BluePrimary
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // En-tête avec fond bleu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BluePrimary)
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
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
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

            // Contenu avec fond arrondi
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
                        focusedBorderColor = BluePrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = storeAddress,
                    onValueChange = { storeAddress = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Adresse") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = storePhone,
                    onValueChange = { storePhone = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Téléphone") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { saveStoreInfo() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BluePrimary
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
                            focusedBorderColor = BluePrimary
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
                        // Navigation vers l'écran de contact
                        snackbarMessage = "Contact & Support - Fonctionnalité à venir"
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = BluePrimary
                    )
                ) {
                    Text("Contact & Support")
                }

                Spacer(modifier = Modifier.height(24.dp))
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
                selectedColor = BluePrimary
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