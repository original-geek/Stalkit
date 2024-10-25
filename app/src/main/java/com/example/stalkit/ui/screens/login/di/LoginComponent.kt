package com.example.stalkit.ui.screens.login.di


import com.example.stalkit.ui.screens.login.LoginContainer
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): LoginComponent
    }

    fun inject(container: LoginContainer)
}