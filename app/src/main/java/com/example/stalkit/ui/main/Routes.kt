package com.example.stalkit.ui.main

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    companion object {
        const val VIDEO_ARG = "video"
        const val PROFILE_ARG = "profile"
    }

    @Serializable
    object VideoView: Routes()

    @Serializable
    object Videos: Routes()

    @Serializable
    object Login: Routes()
}