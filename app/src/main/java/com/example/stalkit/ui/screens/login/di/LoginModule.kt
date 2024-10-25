package com.example.stalkit.ui.screens.login.di

import androidx.lifecycle.ViewModel
import com.example.stalkit.di.ViewModelKey
import com.example.stalkit.ui.screens.login.LoginVM
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap

@Module
abstract class LoginModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginVM::class)
    abstract fun bindViewModel(viewmodel: LoginVM): ViewModel
}