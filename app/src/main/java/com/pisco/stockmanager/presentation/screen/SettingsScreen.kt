package com.pisco.stockmanager.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pisco.stockmanager.data.preferences.AppThemeMode
import com.pisco.stockmanager.data.preferences.Currency
import com.pisco.stockmanager.presentation.viewmodel.SettingsViewModel
import com.pisco.stockmanager.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val settings by viewModel.settings.collectAsState()

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

    Scaffold(

        containerColor = BluePrimary,

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Paramètres",
                        color = Color.White
                    )
                },

                navigationIcon = {

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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Informations du magasin",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = storeName,
                onValueChange = {
                    storeName = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Nom du magasin")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = storeAddress,
                onValueChange = {
                    storeAddress = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Adresse")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = storePhone,
                onValueChange = {
                    storePhone = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Téléphone")
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    viewModel.saveStoreInfo(
                        name = storeName.trim(),
                        address = storeAddress.trim(),
                        phone = storePhone.trim()
                    )
                }
            ) {

                Text("Enregistrer les informations")
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Devise",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = currencyMenuExpanded,
                onExpandedChange = {
                    currencyMenuExpanded = it
                }
            ) {

                OutlinedTextField(
                    value = "${settings.currency.code} (${settings.currency.symbol})",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = {
                        Text("Devise utilisée")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = currencyMenuExpanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = currencyMenuExpanded,
                    onDismissRequest = {
                        currencyMenuExpanded = false
                    }
                ) {

                    Currency.entries.forEach { currency ->

                        DropdownMenuItem(
                            text = {
                                Text("${currency.code} (${currency.symbol})")
                            },
                            onClick = {

                                viewModel.setCurrency(currency)

                                currencyMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Apparence",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column {

                ThemeOption(
                    label = "Clair",
                    selected = settings.themeMode == AppThemeMode.LIGHT,
                    onClick = {
                        viewModel.setThemeMode(AppThemeMode.LIGHT)
                    }
                )

                ThemeOption(
                    label = "Sombre",
                    selected = settings.themeMode == AppThemeMode.DARK,
                    onClick = {
                        viewModel.setThemeMode(AppThemeMode.DARK)
                    }
                )

                ThemeOption(
                    label = "Système (par défaut)",
                    selected = settings.themeMode == AppThemeMode.SYSTEM,
                    onClick = {
                        viewModel.setThemeMode(AppThemeMode.SYSTEM)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Assistance",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("contact")
                }
            ) {

                Text("Contact & Support")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}