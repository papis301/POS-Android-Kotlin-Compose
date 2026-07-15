package com.pisco.stockmanager.shared.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

@SuppressLint("HardwareIds")
actual fun getDeviceUUID(context: Any?): String {
    val appContext = context as? Context ?: return "UNKNOWN_ANDROID"
    // ANDROID_ID est un ID de 64-bits unique à chaque combinaison d'application, appareil et utilisateur.
    return Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID) ?: "UNKNOWN_ANDROID"
}