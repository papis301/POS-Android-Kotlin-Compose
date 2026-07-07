package com.pisco.stockmanager.shared.`data`

import androidx.room.RoomDatabaseConstructor

public actual object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
  actual override fun initialize(): AppDatabase = com.pisco.stockmanager.shared.`data`.AppDatabase_Impl()
}
