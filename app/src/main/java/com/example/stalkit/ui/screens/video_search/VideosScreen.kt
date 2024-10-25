package com.example.stalkit.ui.screens.video_search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.stalkit.Anal
import com.example.stalkit.App
import com.example.stalkit.R
import com.example.stalkit.data.db.LoginEntity
import com.example.stalkit.data.entities.Video
import com.example.stalkit.ui.main.CurrentUserProfileState
import com.example.stalkit.ui.main.MainVM


@Composable
fun VideosScreen(mainVM: MainVM,
                 videoSearchContainer: VideoSearchContainer = rememberVideoSearchContainer(),
                 viewModel: MediaSearchVM = viewModel(factory = videoSearchContainer.vmFactory),
                 onItemClicked: (video: Video) -> Unit, 
                 login: () -> Unit) {
    val profileState = mainVM.profileState.collectAsState()
    when (profileState.value) {
        is CurrentUserProfileState.Idle -> {
        }
        is CurrentUserProfileState.NotLoggedIn -> {
            LaunchedEffect(profileState.value) {
                login()
            }
        }
        is CurrentUserProfileState.InfoLoaded -> {
            val userInfo = (profileState.value as CurrentUserProfileState.InfoLoaded)
            val state = viewModel.mediaSearchState.collectAsState()
            Column(modifier = Modifier.fillMaxSize()) {
                SearchField(viewModel = viewModel, profile = userInfo.entity)
                ItemsContent(state = state,
                    onItemClicked = onItemClicked,
                    viewModel = viewModel,
                    profile = userInfo.entity)
            }
        }
    }
}

@Composable
fun SearchField(viewModel: MediaSearchVM, profile: LoginEntity) {
    val c = LocalContext.current
    val searchText = viewModel.stateKeyword
    OutlinedTextField(
        placeholder = { Text(c.getString(R.string.search_placeholder)) },
        value = searchText.value,
        onValueChange = { newValue ->
            searchText.value = newValue
            viewModel.sendIntent(
                MediaSearchIntent.Search(token = profile.token)
            )
        },
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 20.dp)
    )
}

@Composable
fun ItemsContent(state: State<MediaSearchState>,
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
    if (state.isScrolledToEnd()) {
        LaunchedEffect(items) {
            viewModel.sendIntent(
                MediaSearchIntent.LoadMore(currentCnt = items.size, token = profile.token)
            )
        }
    }
}

fun LazyGridState.isScrolledToEnd(): Boolean {
    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return lastVisibleIndex == layoutInfo.totalItemsCount - 1
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