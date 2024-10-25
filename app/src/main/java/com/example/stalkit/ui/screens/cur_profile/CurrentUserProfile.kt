package com.example.stalkit.ui.screens.cur_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.stalkit.App
import com.example.stalkit.R
import com.example.stalkit.data.login.Profile
import com.example.stalkit.ui.main.CurrentUserProfileIntent
import com.example.stalkit.ui.main.CurrentUserProfileState
import com.example.stalkit.ui.main.MainVM
import com.example.stalkit.ui.screens.video_search.MediaSearchVM
import com.example.stalkit.ui.screens.video_search.VideoSearchContainer
import com.example.stalkit.ui.screens.video_search.rememberVideoSearchContainer


@Composable
fun CurrentUserProfile(mainVM: MainVM, login: () -> Unit) {
    val state = mainVM.profileState.collectAsState()
    var profile: Profile? = null
    when (state.value) {
        CurrentUserProfileState.Idle -> {
        }
        CurrentUserProfileState.NotLoggedIn -> {
            profile = null
        }
        is CurrentUserProfileState.InfoLoaded -> {
            profile = (state.value as CurrentUserProfileState.InfoLoaded).profile
        }
    }
    CurrentUserProfile(profile = profile, setLoggedIn = { b ->
        if (!b)
            mainVM.sendIntent(CurrentUserProfileIntent.Logout)
        else
            login()
    })
}

@Composable
fun CurrentUserProfile(profile: Profile?, setLoggedIn: (b: Boolean) -> Unit) {
    val isLoggedIn = profile != null
    val loginButtTitleRes = if (isLoggedIn) R.string.logout else R.string.login
    val c = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        UserAvatar(
            profile = profile,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        Text(
            text = profile?.getFullname() ?: "",
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 20.dp))
        Button(
            onClick = { setLoggedIn(!isLoggedIn) },
            modifier = Modifier.padding(top = 20.dp)) {
            Text(c.getString(loginButtTitleRes))
        }
    }
}

@Composable
fun UserAvatar(profile: Profile?, modifier: Modifier = Modifier) {

    if (profile == null) {
        Image(
            painter = painterResource(id = R.drawable.account_circle_24px),
            contentDescription = null,
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = profile.photo_400_orig,
            placeholder = painterResource(id = R.drawable.account_circle_24px),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier)
    }
}