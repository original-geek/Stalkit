package com.example.stalkit.di

import android.content.Context
import androidx.room.Room
import com.example.stalkit.data.db.LoginDao
import com.example.stalkit.data.db.LoginDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideLoginDB(context: Context): LoginDatabase {
        val db = Room.databaseBuilder(
            context,
            LoginDatabase::class.java, "login-database"
        ).build()
        return db
    }

    @Provides
    @Singleton
    fun provideLoginDao(db: LoginDatabase): LoginDao {
        return db.dao()
    }

}