package com.example.stalkit.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [LoginEntity::class])
@TypeConverters(Converters::class)
abstract class LoginDatabase: RoomDatabase() {
    abstract fun dao(): LoginDao
}