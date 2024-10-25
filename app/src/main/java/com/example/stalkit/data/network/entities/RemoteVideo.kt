package com.example.stalkit.data.network.entities


data class RemoteVideo(val id: Int,
                       val player: String,
                       val image: List<VideoImage>,
                       val owner_id: Int) {

    fun findThumbWithMaxSz(): String? {
        var ret: String? = null
        var maxSz = 0
        for (it in image) {
            if (it.resolution() > maxSz) {
                maxSz = it.resolution()
                ret = it.url
            }
        }
        return ret
    }

}

data class VideoImage(val url: String, val width: Int, val height: Int) {
    fun resolution(): Int = width * height
}

