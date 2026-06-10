package com.pisco.stockmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pisco.stockmanager.data.local.ClientEntity
import com.pisco.stockmanager.data.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val repository: ClientRepository
) : ViewModel() {

    private val _clients =
        MutableStateFlow<List<ClientEntity>>(emptyList())

    val clients: StateFlow<List<ClientEntity>>
            = _clients.asStateFlow()

    init {
        loadClients()
    }

    private fun loadClients() {

        viewModelScope.launch {

            repository.getClients()
                .collect {
                    _clients.value = it
                }
        }
    }

    fun deleteClient(
        client: ClientEntity
    ) {

        viewModelScope.launch {
            repository.deleteClient(client)
        }
    }

    fun updateClient(
        client: ClientEntity
    ) {

        viewModelScope.launch {
            repository.updateClient(client)
        }
    }

    fun addClient(
        name: String,
        phone: String,
        address: String
    ) {
        viewModelScope.launch {

            repository.insertClient(
                ClientEntity(
                    name = name,
                    phone = phone,
                    address = address
                )
            )
        }
    }
}