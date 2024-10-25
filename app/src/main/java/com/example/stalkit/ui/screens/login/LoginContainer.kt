package com.example.stalkit.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.example.stalkit.App
import javax.inject.Inject


@Stable
class LoginContainer {
    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
}

@Composable
fun rememberLoginContainer(): LoginContainer {
    val component = (LocalContext.current.applicationContext as App).appComponent.getLoginComponent()
    return remember {
        LoginContainer().also {
            component.build().inject(it)
        }
    }
}