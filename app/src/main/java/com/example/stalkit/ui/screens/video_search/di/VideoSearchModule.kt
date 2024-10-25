package com.example.stalkit.ui.screens.video_search.di

import androidx.lifecycle.ViewModel
import com.example.stalkit.di.ViewModelKey
import com.example.stalkit.ui.screens.video_search.MediaSearchVM
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Module
abstract class VideoSearchModule {
    @Binds
    @IntoMap
    @ViewModelKey(MediaSearchVM::class)
    abstract fun bindViewModel(viewmodel: MediaSearchVM): ViewModel
}