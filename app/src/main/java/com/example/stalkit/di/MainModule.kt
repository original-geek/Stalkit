package com.example.stalkit.di

import androidx.lifecycle.ViewModel
import com.example.stalkit.ui.main.MainVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainVM::class)
    abstract fun bindViewModel(viewmodel: MainVM): ViewModel

}