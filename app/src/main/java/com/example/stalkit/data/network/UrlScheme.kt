package com.example.stalkit.data.network

import com.example.stalkit.BuildConfig

object UrlScheme {

    const val BASE_URL = "https://api.vk.com/method/"

    const val API_KEY = BuildConfig.APP_SERVICE_KEY

    const val AUTH_URL = "https://oauth.vk.com/authorize?client_id=51478987&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=video&response_type=token&v=5.131"

}