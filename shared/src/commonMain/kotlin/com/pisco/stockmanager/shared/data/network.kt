package com.pisco.stockmanager.shared.data.network

import com.pisco.stockmanager.shared.data.ModuleLicenseDao
import com.pisco.stockmanager.shared.data.ModuleLicenseEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FirebaseModuleStatus(
    val isActivated: Boolean = false,
    val expirationDate: Long = 0L
)

@Serializable
data class SubscriptionPayload(
    val deviceId: String,
    val subscribedAt: Long,
    val status: String = "ACTIVE",
    val modules: Map<String, FirebaseModuleStatus> = emptyMap()
)

class FirebaseService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
    }

    // URL de votre base de données Firebase (À remplacer par votre URL réelle)
    private val databaseUrl = "https://its2025-default-rtdb.asia-southeast1.firebasedatabase.app"

    /**
     * Enregistre l'appareil sur Firebase s'il n'existe pas encore.
     */
    suspend fun registerDeviceToFirebase(deviceId: String): Boolean {
        return try {
            val payload = SubscriptionPayload(
                deviceId = deviceId,
                subscribedAt = System.currentTimeMillis()
            )

            val firebaseUrl = "$databaseUrl/subscriptions/$deviceId.json"

            val response = client.put(firebaseUrl) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }

            response.status.isSuccess()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Synchronise les licences depuis Firebase et les enregistre dans la base locale Room.
     */
    suspend fun syncLicensesFromFirebase(deviceId: String, licenseDao: ModuleLicenseDao): Boolean {
        return try {
            val firebaseUrl = "$databaseUrl/subscriptions/$deviceId.json"

            val response = client.get(firebaseUrl)

            if (response.status.isSuccess()) {
                val payload = response.body<SubscriptionPayload?>()

                if (payload != null && payload.modules.isNotEmpty()) {
                    val now = System.currentTimeMillis()

                    // On parcourt les modules reçus de Firebase et on met à jour Room
                    payload.modules.forEach { (moduleId, firebaseModule) ->
                        val localLicense = ModuleLicenseEntity(
                            moduleId = moduleId,
                            isActivated = firebaseModule.isActivated,
                            activationDate = now,
                            expirationDate = firebaseModule.expirationDate,
                            lastCheckedTimestamp = now
                        )
                        licenseDao.insertOrUpdate(localLicense)
                    }
                    return true
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}