package com.example.stalkit.ui.screens.login

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stalkit.Anal
import com.example.stalkit.data.db.LoginEntity
import com.example.stalkit.data.login.LoginHelper
import com.example.stalkit.data.login.LoginStatus
import com.example.stalkit.data.network.UrlScheme
import com.example.stalkit.ui.main.CurrentUserProfileIntent
import com.example.stalkit.ui.main.CurrentUserProfileState
import com.example.stalkit.ui.main.MainVM

@Composable
fun LoginScreen(loginContainer: LoginContainer = rememberLoginContainer(),
                loginVM: LoginVM = viewModel(factory = loginContainer.vmFactory),
                onLoggedIn: (token: LoginEntity) -> Unit) {
    Anal.print("Login Screen")
    val state = loginVM.loginState.collectAsState()
    when (state.value) {
        is LoginState.Idle -> {
            LaunchedEffect(state.value) {
                loginVM.sendIntent(LoginIntent.Login)
            }
        }
        is LoginState.LoginFlowStarted -> {
            MyWebView(url = UrlScheme.AUTH_URL) { url ->
                loginVM.sendIntent(LoginIntent.LoginCheck(url))
            }
        }
        is LoginState.LoggedIn -> {
            LaunchedEffect(state.value) {
                onLoggedIn((state.value as LoginState.LoggedIn).token)
            }
        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebView(url: String,
              onWebPageFinished: (url: String?) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            WebView(it).apply {
                settings.javaScriptEnabled = true
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onWebPageFinished(url)
                    }
                }
                loadUrl(url)
            }
        }, update = {
        })
    }
}