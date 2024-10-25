package com.example.stalkit.di

import android.content.Context
import com.example.stalkit.data.db.LoginDao
import com.example.stalkit.ui.main.MainContainer
import com.example.stalkit.ui.screens.login.LoginContainer
import com.example.stalkit.ui.screens.login.di.LoginComponent
import com.example.stalkit.ui.screens.video_search.di.VideoSearchComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [
    MainModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    ViewModelBuilderModule::class,
    SubcomponentsModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun injectMainContainer(container: MainContainer)

    fun getVideoSearchComponent(): VideoSearchComponent.Builder

    fun getLoginComponent(): LoginComponent.Builder

    fun getUserComponent(): UserComponent.Factory
}

@Module(subcomponents = [
    UserComponent::class,
    VideoSearchComponent::class,
    LoginComponent::class
])
object SubcomponentsModule