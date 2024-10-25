package com.example.stalkit.data.repositories

import com.example.stalkit.data.login.Profile
import com.example.stalkit.data.network.RemoteApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoRepository @Inject constructor(val apiService: RemoteApiService) {


    suspend fun fetchUserInfo(id: String, token: String): Profile? {
        val user = apiService.fetchUserInfo(id, token)
        return user.body()?.response?.firstOrNull()
    }

}