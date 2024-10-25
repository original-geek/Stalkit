package com.example.stalkit.data.network

import com.example.stalkit.data.login.UserInfoResponse
import com.example.stalkit.data.network.entities.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteApiService {

    @GET("video.search?v=5.131")
    suspend fun searchVideos(@Query("q") kw: String,
                             @Query("offset") offset: Int,
                             @Query("access_token") token: String): Response<VideoResponse>


    @GET("users.get?fields=photo_400_orig&v=5.131")
    suspend fun fetchUserInfo(@Query("user_ids") user_id: String,
                              @Query("access_token") token: String): Response<UserInfoResponse>

}