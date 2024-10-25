package com.example.stalkit.di

import android.content.Context
import com.example.stalkit.data.login.UserData
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
@ActivityScope
interface UserComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserComponent
    }

    fun userData(): UserData

}