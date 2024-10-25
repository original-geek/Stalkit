package com.example.stalkit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface LoginDao {

    @Query("SELECT * FROM LoginInfo")
    fun queryTokens(): Flow<List<LoginEntity>>

    @Delete
    suspend fun deleteToken(token: LoginEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: LoginEntity)

    @Query("DELETE FROM LoginInfo")
    suspend fun deleteAll()
}