package com.example.stalkit.ui.screens.video_search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.stalkit.Anal
import com.example.stalkit.App
import com.example.stalkit.R
import com.example.stalkit.data.db.LoginEntity
import com.example.stalkit.data.entities.Video
import com.example.stalkit.ui.main.CurrentUserProfileState
import com.example.stalkit.ui.main.MainVM
import com.example.stalkit.ui.main.RouteArgs
import com.example.stalkit.ui.main.Routes
import com.example.stalkit.ui.menu.TopBarMain
import com.example.stalkit.ui.screens.VideoScreen
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


@Composable
fun VideosScreen(
    userInfo: CurrentUserProfileState.InfoLoaded) {
    Anal.print("VideosScreen CurrentUserProfileState.InfoLoaded")
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Videos.name) {
        composable(route = Routes.Videos.name) {
            Videos(userInfo, onItemClicked = { video ->
                navController.currentBackStackEntry?.savedStateHandle?.set(RouteArgs.VIDEO_ARG, video)
                navController.navigate(Routes.Video.name)
            })
        }
        composable(route = Routes.Video.name) {
            val video = navController.previousBackStackEntry?.savedStateHandle?.get<Video>(RouteArgs.VIDEO_ARG)
            video?.let {
                VideoScreen(video)
            }
        }
    }
}

@Composable
fun Videos(userInfo: CurrentUserProfileState.InfoLoaded,
           videoSearchContainer: VideoSearchContainer = rememberVideoSearchContainer(),
           viewModel: MediaSearchVM = viewModel(factory = videoSearchContainer.vmFactory),
           onItemClicked: (video: Video) -> Unit) {
    val state = viewModel.mediaSearchState.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        SearchField(viewModel = viewModel, profile = userInfo.entity)
        ItemsGrid(state = state,
            onItemClicked = onItemClicked,
            viewModel = viewModel,
            profile = userInfo.entity)
    }
}

@Composable
fun SearchPlaceHolder() {
    val c = LocalContext.current
    Row() {
        Icon(
            painter = painterResource(id = R.drawable.search_24px),
            modifier = Modifier
                //.padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
                .aspectRatio(1f)
                .size(20.dp),
            tint = Color.LightGray,
            contentDescription = null)
        Text(c.getString(R.string.search_placeholder),
            style = MaterialTheme.typography.labelMedium,
            color = Color.LightGray,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 5.dp)
            )
    }
}

@Composable
fun SearchField(viewModel: MediaSearchVM, profile: LoginEntity) {
    val searchText = viewModel.stateKeyword
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 20.dp)
    ) {
        OutlinedTextField(
            placeholder = { SearchPlaceHolder() },
            value = searchText.value,
            onValueChange = { newValue ->
                searchText.value = newValue
                viewModel.sendIntent(
                    MediaSearchIntent.Search(token = profile.token)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        )
    }
}

@Composable
fun ItemsGrid(state: State<MediaSearchState>,
                 onItemClicked: (video: Video) -> Unit,
                 viewModel: MediaSearchVM,
                 profile: LoginEntity) {
    val c = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        when (state.value) {
            is MediaSearchState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is MediaSearchState.ResultUpdated -> {
                ShowVideoItems(
                    items = (state.value as MediaSearchState.ResultUpdated).items,
                    onItemClicked = onItemClicked,
                    viewModel = viewModel,
                    profile = profile
                )
            }
            is MediaSearchState.Error -> {
                Text(c.getString(R.string.error_occurred),
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center))
            }
            is MediaSearchState.Idle -> {
            }
        }
    }
}

@Composable
fun ShowVideoItems(items: List<Video>,
                   onItemClicked: (video: Video) -> Unit,
                   viewModel: MediaSearchVM,
                   profile: LoginEntity) {
    val state = rememberLazyGridState()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 200.dp),
        state = state
    ) {
        itemsIndexed(items, key = { _, item -> item.player }) { ind, item ->
            MediaItem(item.thumb, onItemClicked = { onItemClicked(item) })
        }
    }
    LaunchedEffect(state, items) {
        snapshotFlow { state.layoutInfo }
            .map { it -> it.isScrolledToEnd()  }
            .distinctUntilChanged()
            .filter { it == true }
            .collect {
                viewModel.sendIntent(
                    MediaSearchIntent.LoadMore(currentCnt = items.size, token = profile.token)
                )
            }
    }
}

fun LazyGridLayoutInfo.isScrolledToEnd(): Boolean {
    val lastVisibleIndex = visibleItemsInfo.lastOrNull()?.index ?: 0
    return lastVisibleIndex == totalItemsCount - 1
}

@Composable
fun MediaItem(url: String?, onItemClicked: () -> Unit) {
    Box(modifier = Modifier.clickable { onItemClicked() }) {
        AsyncImage(
            model = url,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(4.dp)
                .background(Color(0x000f0f0f))
        )
        Icon(painter = painterResource(id = R.drawable.videocam_24px),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(0.dp, 5.dp, 5.dp, 0.dp),
            contentDescription = null)
    }
}