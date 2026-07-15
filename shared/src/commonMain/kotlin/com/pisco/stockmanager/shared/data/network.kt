package com.pisco.stockmanager.shared.data


import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SubscriptionPayload(
    val deviceId: String,
    val subscribedAt: Long,
    val status: String = "ACTIVE"
)

class FirebaseService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun registerDeviceToFirebase(deviceId: String): Boolean {
        return try {
            val payload = SubscriptionPayload(
                deviceId = deviceId,
                subscribedAt = System.currentTimeMillis()
            )

            // Remplacez par l'URL de votre Firebase Realtime Database
            val firebaseUrl = "https://votre-projet-firebase-default-rtdb.firebaseio.com/subscriptions/$deviceId.json"

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
}