package com.pisco.stockmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "clients")
data class ClientEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val phone: String,

    val address: String,

    val createdAt: Long = System.currentTimeMillis())