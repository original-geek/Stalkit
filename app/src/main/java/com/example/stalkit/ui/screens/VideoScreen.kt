package com.example.stalkit.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.stalkit.Anal
import com.example.stalkit.R
import com.example.stalkit.data.entities.Video
import androidx.core.net.toUri


@Composable
fun VideoScreen(video: Video) {
    Anal.print("video " + video.thumb + " ")
    val c = LocalContext.current
    Box(modifier = Modifier) {
        AsyncImage(
            model = video.thumb,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .background(Color(0x000f0f0f))
        )
        IconButton(onClick = {
            openVideoIn(c, video)
        }, modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(0.3f)
            .aspectRatio(1f)
            .clip(CircleShape)
            .border(4.dp, color = Color(0x88FFFFFF), shape = CircleShape)
            .background(Color(0x22FFFFFF)),
            ) {
            Icon(imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = Color(0x88FFFFFF))
        }
    }
}

fun openVideoIn(c: Context, video: Video) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = video.player.toUri()
    }
    c.startActivity(intent)
}