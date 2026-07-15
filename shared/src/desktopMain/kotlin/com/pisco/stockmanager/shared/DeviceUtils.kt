package com.pisco.stockmanager.shared.utils

import java.io.File
import java.util.UUID

actual fun getDeviceUUID(context: Any?): String {
    // Sous Desktop, on génère un UUID persistant qu'on sauvegarde localement dans un fichier caché
    val userHome = System.getProperty("user.home")
    val uuidFile = File(userHome, ".stockmanager_device_id")

    return if (uuidFile.exists()) {
        uuidFile.readText().trim()
    } else {
        val newUUID = UUID.randomUUID().toString()
        try {
            uuidFile.writeText(newUUID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        newUUID
    }
}