package com.example.stalkit.ui.screens.video_search.di

import com.example.stalkit.ui.screens.video_search.VideoSearchContainer
import dagger.Subcomponent

@Subcomponent(modules = [VideoSearchModule::class])
interface VideoSearchComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): VideoSearchComponent
    }

    fun inject(container: VideoSearchContainer)
}