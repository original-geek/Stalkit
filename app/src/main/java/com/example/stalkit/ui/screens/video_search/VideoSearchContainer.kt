package com.example.stalkit.ui.screens.video_search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.stalkit.App
import javax.inject.Inject

@Stable
class VideoSearchContainer {
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
}

@Composable
fun rememberVideoSearchContainer(): VideoSearchContainer {
    val component = (LocalContext.current.applicationContext as App).appComponent
    return remember {
        VideoSearchContainer().also {
            component.getVideoSearchComponent().build().inject(it)
        }
    }
}