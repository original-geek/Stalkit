package com.example.stalkit.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stalkit.data.db.LoginEntity
import com.example.stalkit.data.login.LoginHelper
import com.example.stalkit.data.login.LoginStatus
import com.example.stalkit.data.login.Profile
import com.example.stalkit.data.login.UserData
import com.example.stalkit.ui.main.CurrentUserProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed interface LoginState {
    object Idle: LoginState
    object LoginFlowStarted: LoginState
    data class LoggedIn(val token: LoginEntity): LoginState
}

sealed interface LoginIntent {
    object Login: LoginIntent
    data class LoginCheck(val url: String?): LoginIntent
}

@Singleton
class LoginVM @Inject constructor(private val userData: UserData) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState


    private fun login() {
        viewModelScope.launch {
            userData.closeAllSessions()
            _loginState.emit(LoginState.LoginFlowStarted)
        }
    }

    private fun loginCheck(url: String?) {
        url?.let {
            val status = LoginHelper.checkAuthUrl(url)
            if (status is LoginStatus.Succeed) {
                viewModelScope.launch {
                    userData.setLoggedIn(status.token)
                    _loginState.emit(LoginState.LoggedIn(status.token))
                }
            }
        }
    }

    fun sendIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                login()
            }
            is LoginIntent.LoginCheck -> {
                loginCheck(intent.url)
            }
        }
    }

}