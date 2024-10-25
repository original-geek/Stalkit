package com.example.stalkit.data.network.entities


data class VideoResponse(val response: VideosResponseBody)


data class VideosResponseBody(val count: Int, val items: List<RemoteVideo>)