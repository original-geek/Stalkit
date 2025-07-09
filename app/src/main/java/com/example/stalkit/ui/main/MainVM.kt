package com.example.stalkit.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stalkit.Anal
import com.example.stalkit.data.db.LoginEntity
import com.example.stalkit.data.login.LoginHelper
import com.example.stalkit.data.login.LoginStatus
import com.example.stalkit.data.login.Profile
import com.example.stalkit.data.login.UserData
import com.example.stalkit.data.repositories.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed interface CurrentUserProfileState {
    object Idle: CurrentUserProfileState
    object NotLoggedIn: CurrentUserProfileState
    data class InfoLoaded(val entity: LoginEntity, val profile: Profile): CurrentUserProfileState
}

sealed interface CurrentUserProfileIntent {
    object Login: CurrentUserProfileIntent
    data class LoginCheck(val url: String?): CurrentUserProfileIntent
    object Logout: CurrentUserProfileIntent
}

@Singleton
class MainVM @Inject constructor(
    private val repository: UserInfoRepository,
    private val userData: UserData
) : ViewModel() {

    private val _profileState = MutableStateFlow<CurrentUserProfileState>(CurrentUserProfileState.Idle)
    val profileState: StateFlow<CurrentUserProfileState> = _profileState.asStateFlow()

    private val entityFlow = userData.loginFlow

    private var entity: LoginEntity? = null


    init {
        Anal.print("MainVM init")
        viewModelScope.launch {
            entityFlow.collect { entity ->
                loadUserInfo(entity)
            }
        }
    }

    private fun updateState(state: CurrentUserProfileState) {
        viewModelScope.launch {
            _profileState.emit(state)
        }
    }

    private fun logout() {
        viewModelScope.launch {
            entity?.let {
                userData.logout(it)
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            userData.closeAllSessions()
        }
    }

    private fun loginCheck(url: String?) {
        url?.let {
            val status = LoginHelper.checkAuthUrl(url)
            if (status is LoginStatus.Succeed) {
                viewModelScope.launch {
                    userData.setLoggedIn(status.token)
                }
            }
        }
    }

    fun sendIntent(intent: CurrentUserProfileIntent) {
        when (intent) {
            is CurrentUserProfileIntent.Login -> {
                login()
            }
            is CurrentUserProfileIntent.LoginCheck -> {
                loginCheck(intent.url)
            }
            is CurrentUserProfileIntent.Logout -> {
                logout()
            }
        }
    }

    private suspend fun loadUserInfo(entity: LoginEntity?) {
        var newState: CurrentUserProfileState? = null
        this.entity = entity
        if (entity != null) {
            val profile = repository.fetchUserInfo(entity.userId, entity.token)
            if (profile != null) {
                newState = CurrentUserProfileState.InfoLoaded(entity, profile)
            }
        }
        updateState(newState ?: CurrentUserProfileState.NotLoggedIn)
    }

}