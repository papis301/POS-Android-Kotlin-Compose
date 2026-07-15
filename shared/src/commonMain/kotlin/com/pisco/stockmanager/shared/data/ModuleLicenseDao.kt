package com.pisco.stockmanager.shared.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ModuleLicenseDao {

    @Query("SELECT * FROM module_licenses WHERE moduleId = :moduleId LIMIT 1")
    suspend fun getLicense(moduleId: String): ModuleLicenseEntity?

    @Query("SELECT * FROM module_licenses")
    suspend fun getAllLicenses(): List<ModuleLicenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(license: ModuleLicenseEntity)
}