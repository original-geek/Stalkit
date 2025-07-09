package com.example.stalkit.ui.screens

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.stalkit.Anal
import com.example.stalkit.data.entities.Video


@Composable
fun VideoScreen(video: Video) {
    Anal.print("video " + video.thumb + " ")
    AsyncImage(
        model = video.thumb,
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(Color(0x000f0f0f))
    )
}

@Composable
fun VideoScreen1(video: Video) {
/*
 video is played in WebView because the app does not have direct access to video file
  and it needs a browser engine to play video by the provided link in video.player
 */
    Anal.print("video " + video.player + " ")
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object: WebViewClient() {

                }
              //  webChromeClient = object : WebClient() {

                //}
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