package com.example.stalkit.ui.screens

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.stalkit.data.entities.Video

@Composable
fun VideoScreen(video: Video) {

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webChromeClient = object : WebChromeClient() {

                }
                loadUrl(video.player)
            }
        }, update = {
        })
    }
}

@Composable
fun VideoScreenExoPlayer(video: String) {
    val context = LocalContext.current

    val url = video

    val exoPlayer = remember(context, url) {
        val mediaItem = MediaItem.Builder().setUri(url).build()
        ExoPlayer.Builder(context).build()
            .also { exoPlayer ->
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = false
                exoPlayer.repeatMode = REPEAT_MODE_OFF
            }
    }

    AndroidView(factory = {
        PlayerView(context).apply {
            player = exoPlayer
        }
    })

    DisposableEffect(url) {
        onDispose { exoPlayer.release() }
    }
}