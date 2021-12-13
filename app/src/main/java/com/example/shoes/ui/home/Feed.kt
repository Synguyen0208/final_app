/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.shoes.ui.home

import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoes.R
import com.example.shoes.model.*
import com.example.shoes.ui.components.*
import com.example.shoes.ui.rememberShoesAppState
import com.example.shoes.utils.Resource
import com.example.shoes.viewmodel.ShoesViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield


@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun Feed(
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    FeedScreen(
        onShoesClick,
        modifier
    )
}


@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun FeedScreen(
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val appState = rememberShoesAppState()
    ShoesSurface(modifier = modifier.fillMaxSize()) {
        Box(
        ) {
            SnackCollectionList(onShoesClick)
            DestinationBar(appState::navigateToBottomBarRoute)
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalPagerApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SnackCollectionList(
    onShoesClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShoesViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(
        pageCount = 3,
        initialOffscreenLimit = 2
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val getAllUserData: List<ShoesResponse> by viewModel
        .getShoesData
        .observeAsState(listOf())
    Spacer(Modifier.statusBarsHeight(additional = 40.dp))
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                animationSpec = tween(600)
            )
        }
    }


    scope.launch {
        val result = viewModel.getShoesData()

        if (result is Resource.Success) {
            Toast.makeText(context, "Fetching data success!", Toast.LENGTH_SHORT).show()
        } else if (result is Resource.Error) {
            Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    if (!viewModel.isLoading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
    val scroll = rememberScrollState(0)

    Column(

    ) {
        Box {
            Column(
                modifier = Modifier.verticalScroll(scroll)
            ){
                Column(modifier = Modifier.height(300.dp)) {
                    Spacer(Modifier.statusBarsHeight(additional = 10.dp))
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .weight(1f)
                            .height(300.dp)
                    ) { page ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp, 0.dp, 5.dp, 0.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.White)
                                    .align(Alignment.Center)
                            ) {
                                Image(
                                    painter = painterResource(
                                        id = when (page) {
                                            1 -> R.drawable.banner_2
                                            2 -> R.drawable.banner3
                                            3 -> R.drawable.banner1
                                            else -> R.drawable.banner1
                                        }
                                    ),
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                    }
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                }
                if (viewModel.isLoading.value) {
                    if (viewModel.getShoesData.value!!.isNotEmpty()) {
                        ContentLayout(shoes = getAllUserData, onShoesClick = onShoesClick)
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun ContentLayout(
    shoes: List<ShoesResponse>,
    onShoesClick: (Long) -> Unit,
) {
    Column(
    ) {
        Box {
            FlowRow(
                mainAxisAlignment = MainAxisAlignment.Center,
                mainAxisSize = SizeMode.Expand,
                crossAxisSpacing = 8.dp,
                mainAxisSpacing = 8.dp
            ) {
                shoes.forEach { shoe ->
                    FoodItem(
                        shoesResponse = shoe,
                        onShoesClick = onShoesClick
                    )
                }
            }
        }
    }

}
