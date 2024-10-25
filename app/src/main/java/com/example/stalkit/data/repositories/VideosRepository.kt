package com.example.stalkit.data.repositories

import com.example.stalkit.Anal
import com.example.stalkit.data.login.Profile
import com.example.stalkit.data.network.RemoteApiService
import com.example.stalkit.data.entities.Video
import com.example.stalkit.data.entities.asVideo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosRepository @Inject constructor(val apiService: RemoteApiService) {


    suspend fun searchVideos(kw: String, offset: Int, token: String): List<Video>? {
        Anal.print("kw $kw offset $offset token $token")
        val response = apiService.searchVideos(kw, offset, token)
        return response.body()?.response?.items?.map { it.asVideo() }
    }

}