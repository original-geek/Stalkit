package com.example.stalkit.ui

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.exoplayer.ExoPlayer

object VideoPlayerFactory {



    fun exoPlayer(context: Context, url: String): ExoPlayer {
        val mediaItem = MediaItem.Builder().setUri(url).build()
        return ExoPlayer.Builder(context).build()
            .also { exoPlayer ->
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = false
                exoPlayer.repeatMode = REPEAT_MODE_OFF
            }
    }

}