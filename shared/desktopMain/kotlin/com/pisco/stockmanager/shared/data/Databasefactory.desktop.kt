package com.pisco.stockmanager.shared.data

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {

    actual fun create(): RoomDatabase.Builder<AppDatabase> {
        val appDir = File(System.getProperty("user.home"), ".yombena")

        if (!appDir.exists()) {
            appDir.mkdirs()
        }

        val dbFile = File(appDir, DATABASE_NAME)

        return Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath
        )
    }
}