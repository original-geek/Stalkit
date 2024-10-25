package com.example.stalkit.ui.screens.video_search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stalkit.data.entities.Video
import com.example.stalkit.data.repositories.VideosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed interface MediaSearchState {
    object Idle: MediaSearchState
    object Loading: MediaSearchState
    object Error: MediaSearchState
    data class ResultUpdated(val items: List<Video>, val kw: String): MediaSearchState
}

interface MediaSearchIntent {
    data class Search(val token: String): MediaSearchIntent

    data class LoadMore(val currentCnt: Int, val token: String): MediaSearchIntent
}

@Singleton
class MediaSearchVM @Inject constructor(private val repository: VideosRepository) : ViewModel() {

    private val _mediaSearchState = MutableStateFlow<MediaSearchState>(MediaSearchState.Idle)
    val mediaSearchState: StateFlow<MediaSearchState> = _mediaSearchState

    val stateKeyword = mutableStateOf("")



    fun sendIntent(intent: MediaSearchIntent) {
        when (intent) {
            is MediaSearchIntent.Search -> {
                search(intent.token)
            }
            is MediaSearchIntent.LoadMore -> {
                loadMore(intent.currentCnt, intent.token)
            }
        }
    }


    private fun loadMore(currentCnt: Int, token: String) {
        viewModelScope.launch {
            loadVideos(currentCnt, token)
        }
    }

    private suspend fun loadVideos(currentCnt: Int, token: String) {
        val kw = stateKeyword.value
        val videos = repository.searchVideos(kw, currentCnt, token)
        if (videos == null) {
            _mediaSearchState.emit(MediaSearchState.Error)
        } else {
            val stateValue = _mediaSearchState.value
            if (stateValue is MediaSearchState.ResultUpdated && stateValue.kw == kw) {
                _mediaSearchState.emit(
                    stateValue.copy(items = stateValue.items + videos)
                )
            } else {
                _mediaSearchState.emit(MediaSearchState.ResultUpdated(videos, kw))
            }
        }
    }

    private fun search(token: String) {
        viewModelScope.launch {
            _mediaSearchState.emit(MediaSearchState.Loading)
            loadVideos(0, token)
        }
    }

}