package com.example.stalkit.data.login

import android.content.Context
import com.example.stalkit.Anal
import com.example.stalkit.data.db.LoginDao
import com.example.stalkit.data.db.LoginEntity
import com.example.stalkit.di.ActivityScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserData @Inject constructor(val dao: LoginDao) {


    val loginFlow: Flow<LoginEntity?> = dao.queryTokens()
        .transform { entities ->
            Anal.print("loginFlow transform cnt = ${entities.size}")
            var bEmittedAtLeastOne = false
            entities.forEach {
                Anal.print("entities.forEach ${it.userId} expiration = ${it.expiration}" +
                        " dataLoggedIn = ${it.dateLoggedIn.time} now = ${Date().time}")
                if (!it.isSessionExpired()) {
                    emit(it)
                    bEmittedAtLeastOne = true
                }
            }
            Anal.print("bEmittedAtLeastOne $bEmittedAtLeastOne")
            if (!bEmittedAtLeastOne) emit(null)
        }


    suspend fun logout(token: LoginEntity) {
        dao.deleteToken(token)
    }

    suspend fun setLoggedIn(token: LoginEntity) {
        Anal.print("insert token")
        dao.insertToken(token)
    }

    suspend fun closeAllSessions() {
        dao.deleteAll()
    }

}