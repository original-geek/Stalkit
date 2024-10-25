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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                CurrentUserProfile(mainVM, login = {
                    navController.navigate(Routes.Login)
                })
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBarMain(onMenuButtClicked = {
                    scope.launch {
                        if (drawerState.isOpen) drawerState.close() else drawerState.open()
                    }
                })
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Screens(navController, mainVM)
            }
        }
    }
}

@Composable
fun Screens(navController: NavHostController, mainVM: MainVM) {
    NavHost(navController = navController, startDestination = Routes.Videos) {
        composable<Routes.Login> {
            LoginScreen(onLoggedIn = {
                Anal.print("onLoggedIn")
                navController.popBackStack()
            })
        }
        composable<Routes.Videos> {
            VideosScreen(mainVM, onItemClicked = { video ->
                navController.currentBackStackEntry?.savedStateHandle?.set(Routes.VIDEO_ARG, video)
                navController.navigate(Routes.VideoView)
            }, login = {
                Anal.print("go to login")
                navController.navigate(Routes.Login)
            })
        }
        composable<Routes.VideoView> {
            val video = navController.previousBackStackEntry?.savedStateHandle?.get<Video>(Routes.VIDEO_ARG)
            video?.let {
                VideoScreen(video)
            }
        }
    }
}