package com.pisco.stockmanager.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module_licenses")
data class ModuleLicenseEntity(
    @PrimaryKey val moduleId: String,       // Ex: "SEARCH_PRO", "STOCK_ALERTS"
    val isActivated: Boolean,               // true si actif
    val activationDate: Long,               // Timestamp (ms)
    val expirationDate: Long,               // Timestamp (ms) d'expiration
    val lastCheckedTimestamp: Long          // Dernier timestamp système enregistré (anti-triche)
)