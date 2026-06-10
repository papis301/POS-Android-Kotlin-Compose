package com.pisco.stockmanager.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pisco.stockmanager.presentation.viewmodel.ClientViewModel

@Composable
fun ClientScreen(
    viewModel: ClientViewModel = hiltViewModel()
) {

    val clients by viewModel.clients.collectAsState()

    var name by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
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
                Text("Nom client")
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            label = {
                Text("Telephone")
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = address,
            onValueChange = {
                address = it
            },
            label = {
                Text("Adresse")
            }
        )

        Button(
            onClick = {

                viewModel.addClient(
                    name,
                    phone,
                    address,
                )

                name = ""
                phone = ""
            }
        ) {

            Text("Ajouter")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn {

            items(clients) { client ->

                Text(
                    text = client.name,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        viewModel.deleteClient(client)
                    }
                ) {
                    Text("Supprimer")
                }
            }
        }
    }
}
