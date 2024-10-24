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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    object Logout: CurrentUserProfileIntent
}

@Singleton
class MainVM @Inject constructor(
    private val repository: UserInfoRepository,
    private val userData: UserData
) : ViewModel() {

    private val _profileState = MutableStateFlow<CurrentUserProfileState>(CurrentUserProfileState.Idle)
    val profileState: StateFlow<CurrentUserProfileState> = _profileState

    private val entityFlow = userData.loginFlow

    private var entity: LoginEntity? = null


    init {
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

    fun logout() {
        viewModelScope.launch {
            entity?.let {
                userData.logout(it)
            }
        }
    }

    fun sendIntent(intent: CurrentUserProfileIntent) {
        when (intent) {
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