package com.example.stalkit.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.stalkit.App
import javax.inject.Inject

@Stable
class MainContainer {
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
}

@Composable
fun rememberMainContainer(): MainContainer {
    val component = (LocalContext.current.applicationContext as App).appComponent
    return remember {
        MainContainer().also {
            component.injectMainContainer(it)
        }
    }
}