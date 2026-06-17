package com.pisco.stockmanager.data.repository

import com.pisco.stockmanager.data.local.ClientDao
import com.pisco.stockmanager.data.local.ClientEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ClientRepository @Inject constructor(
    private val clientDao: ClientDao
) {

    fun getClients(): Flow<List<ClientEntity>> {
        return clientDao.getAllClients()
    }

    suspend fun insertClient(
        client: ClientEntity
    ) {
        clientDao.insert(client)
    }

    suspend fun updateClient(
        client: ClientEntity
    ) {
        clientDao.update(client)
    }

    suspend fun deleteClient(
        client: ClientEntity
    ) {
        clientDao.delete(client)
    }

    fun getClientCount() =
        clientDao.getClientCount()

    suspend fun getClientsOnce():
            List<ClientEntity> {

        return clientDao
            .getAllClientsOnce()
    }
    suspend fun deleteAll() {

        clientDao.deleteAll()
    }
}