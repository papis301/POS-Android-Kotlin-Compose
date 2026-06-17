package com.pisco.stockmanager.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {


    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insert(
        client: ClientEntity
    )

    @Update
    suspend fun update(client: ClientEntity)

    @Delete
    suspend fun delete(client: ClientEntity)

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("SELECT COUNT(*) FROM clients")
    fun getClientCount(): Flow<Int>

    @Query("SELECT * FROM clients")
    suspend fun getAllClientsOnce():
            List<ClientEntity>
    @Query("DELETE FROM clients")
    suspend fun deleteAll()
}