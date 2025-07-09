package com.example.stalkit.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.stalkit.Anal
import com.example.stalkit.data.entities.Video
import com.example.stalkit.ui.menu.TopBarMain
import com.example.stalkit.ui.screens.login.LoginScreen
import com.example.stalkit.ui.screens.VideoScreen
import com.example.stalkit.ui.screens.cur_profile.CurrentUserProfile
import com.example.stalkit.ui.screens.video_search.MediaSearchVM
import com.example.stalkit.ui.screens.video_search.VideoSearchContainer
import com.example.stalkit.ui.screens.video_search.VideosScreen
import com.example.stalkit.ui.screens.video_search.rememberVideoSearchContainer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StalkitApp(mainContainer: MainContainer = rememberMainContainer(),
               mainVM: MainVM = viewModel(factory = mainContainer.vmFactory)) {
    Anal.print("StalkitApp")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val entry by navController.currentBackStackEntryAsState()
    val isHomeScreen = entry == null || entry?.destination?.route == Routes.Videos.name
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                CurrentUserProfile(mainVM, login = {
                    scope.launch { drawerState.close() }
                })
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBarMain(
                    canGoBack = !isHomeScreen,
                    onBack = { if (!isHomeScreen) navController.popBackStack() },
                    canShowDrawer = isHomeScreen,
                    onDrawerButtClicked = {
                        if (isHomeScreen) {
                            scope.launch {
                                if (drawerState.isOpen) drawerState.close() else drawerState.open()
                            }
                        }
                })
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Home(mainVM, navController)
            }
        }
    }
}

@Composable
fun Home(mainVM: MainVM, navController: NavHostController) {
    Anal.print("Home")
    val profileState = mainVM.profileState.collectAsStateWithLifecycle()
    when (profileState.value) {
        is CurrentUserProfileState.Idle -> {
            Anal.print("Home idle")
        }
        is CurrentUserProfileState.NotLoggedIn -> {
            Anal.print("Home CurrentUserProfileState.NotLoggedIn")
            LoginScreen(mainVM)
        }
        is CurrentUserProfileState.InfoLoaded -> {
            Anal.print("Home CurrentUserProfileState.InfoLoaded")
            val userInfo = (profileState.value as CurrentUserProfileState.InfoLoaded)
            VideosScreen(userInfo, navController)
        }
    }
}