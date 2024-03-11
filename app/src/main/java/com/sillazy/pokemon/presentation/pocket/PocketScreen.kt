package com.sillazy.pokemon.presentation.pocket

import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.sillazy.pokemon.R
import com.sillazy.pokemon.domain.model.Pocket
import com.sillazy.pokemon.getImageUrl
import com.sillazy.pokemon.parseTypeToColor
import com.sillazy.pokemon.presentation.component.PokemonBottomBar
import com.sillazy.pokemon.presentation.navgraph.Screen
import com.sillazy.pokemon.setCapitalize
import com.sillazy.pokemon.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PocketScreen(
    onPokemonClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: PocketViewModel = hiltViewModel()
) {
    var pokemonList by remember { mutableStateOf(listOf<Pocket>()) }
    var showDialog by remember { mutableStateOf(false) }
    viewModel.getAllPockets()
    pokemonList = viewModel.pockets.collectAsState().value

    val pageCount = pokemonList.size
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { pageCount }

    val indicatorScrollState = rememberLazyListState()

    LaunchedEffect(key1 = pagerState.currentPage, block = {
        val currentPage = pagerState.currentPage
        val lastVisibleIndex = indicatorScrollState.layoutInfo.visibleItemsInfo.lastIndex
        val size = indicatorScrollState.layoutInfo.visibleItemsInfo.size
        val firstVisibleItemIndex = indicatorScrollState.firstVisibleItemIndex

        if (currentPage > lastVisibleIndex - 1) {
            indicatorScrollState.animateScrollToItem(currentPage - size + 2)
        } else if (currentPage <= firstVisibleItemIndex + 1) {
            indicatorScrollState.animateScrollToItem(Math.max(currentPage - 1, 0))
        }
    })

    val colorMatrix = remember { ColorMatrix() }

    val imageLoader = ImageLoader.Builder(LocalContext.current).components {
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        bottomBar = {
            PokemonBottomBar(
                tabs = Screen.values(),
                currentRoute = Screen.Search.route,
                navigateToRoute = onNavigateToRoute,
                modifier = Modifier.fillMaxWidth()
            )
        },
    ) {
        Surface(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = R.drawable.pokemon_wallpaper3, contentDescription = "null"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                PokemonHorizontalPager(
                    modifier = Modifier.align(Alignment.Center),
                    pagerState = pagerState,
                    imageLoader = imageLoader,
                    showDialog = showDialog,
                    setShowDialog = { showDialog = it },
                    pokemonList = pokemonList,
                    viewModel = viewModel,
                    colorMatrix = colorMatrix
                )
                IndicatorSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    pageCount = pageCount,
                    pagerState = pagerState,
                    indicatorScrollState = indicatorScrollState,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonHorizontalPager(
    modifier: Modifier,
    pagerState: PagerState,
    imageLoader: ImageLoader,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    pokemonList: List<Pocket>,
    viewModel: PocketViewModel,
    colorMatrix: ColorMatrix,
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState
    ) { index ->
        val pageOffset =
            (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
        val imageSize by animateFloatAsState(
            targetValue =
            if (pageOffset != 0.0f) 0.75f else 1f,
            animationSpec = tween(durationMillis = 300),
            label = "",
        )
        val pokemonObject = pokemonList[index]

        if (showDialog) {
            CustomDialog(
                pokemon = pokemonObject,
                setShowDialog = { setShowDialog(it) },
                viewModel = viewModel
            )
        }

        LaunchedEffect(key1 = imageSize) {
            if (pageOffset != 0.0f) {
                colorMatrix.setToSaturation(0f)
            } else {
                colorMatrix.setToSaturation(1f)
            }
        }
        Box(modifier = Modifier.fillMaxHeight()) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Spacer(modifier = Modifier.height(120.dp))
                Box(
                    modifier = Modifier
                        .background(
                            Color(pokemonObject.color).copy(1f),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .height(80.dp)
                        .padding(horizontal = 30.dp),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = setCapitalize(pokemonObject.name),
                        style = Typography.bodyMedium,
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(50.dp),
                onClick = {
                    setShowDialog(true)
                }) {
                Icon(
                    painterResource(id = R.drawable.ic_close),
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 10.dp, end = 10.dp),
                    contentDescription = "release"
                )
            }
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = imageSize
                        scaleY = imageSize
                    }
                    .clip(RoundedCornerShape(16.dp))
                    .padding(vertical = 50.dp, horizontal = 20.dp)
                    .align(Alignment.Center),
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .align(Alignment.Center),
                    model = ImageRequest.Builder(LocalContext.current).data(
                        getImageUrl(pokemonObject.id)
                    ).build(),
                    imageLoader = imageLoader,
                    colorFilter = ColorFilter.colorMatrix(colorMatrix = colorMatrix),
                    contentDescription = null,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 100.dp)
                    .align(Alignment.BottomCenter)
            ) {
                val cuts = pokemonObject.types.split(" ")
                val types = arrayListOf<String>()
                for (t in cuts) {
                    if (t.contains("name")) {
                        types.add(t.substring(16).dropLast(1))
                    }
                }
                for (type in types) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .background(parseTypeToColor(type))
                            .height(40.dp)
                    ) {
                        Text(
                            text = setCapitalize(type),
                            color = Color.White,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndicatorSection(
    modifier: Modifier,
    pageCount: Int,
    pagerState: PagerState,
    indicatorScrollState: LazyListState,
) {
    LazyRow(
        state = indicatorScrollState,
        modifier = modifier
            .height(50.dp)
            .width(200.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val alpha =
                if (pagerState.currentPage == iteration) 1f else 0.5f
            item(key = "item$iteration") {
                val currentPage = pagerState.currentPage
                val firstVisibleIndex by remember { derivedStateOf { indicatorScrollState.firstVisibleItemIndex } }
                val lastVisibleIndex =
                    indicatorScrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        ?: 0
                val size by animateDpAsState(
                    targetValue =
                    when (iteration) {
                        currentPage -> {
                            15.dp
                        }

                        in firstVisibleIndex + 2..<lastVisibleIndex - 1 -> {
                            15.dp
                        }

                        firstVisibleIndex + 1 -> {
                            12.5.dp
                        }

                        lastVisibleIndex - 1 -> {
                            12.5.dp
                        }

                        else -> {
                            10.dp
                        }
                    }, label = ""
                )

                Image(
                    modifier = Modifier
                        .alpha(alpha)
                        .padding(8.dp)
                        .size(size),
                    painter = painterResource(id = R.drawable.pokeball),
                    contentDescription = "Indicator Icon"
                )
            }
        }
    }
}
@Composable
fun CustomDialog(pokemon: Pocket, setShowDialog: (Boolean) -> Unit, viewModel: PocketViewModel) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    setShowDialog(false)
                    viewModel.releasePocket(pokemon)
                    Toast.makeText(
                        context,
                        "${setCapitalize(pokemon.name)} is free !!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Text("Release")
            }
        },
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(text = "Do you want to release ${setCapitalize(pokemon.name)}?")
        },
        text = {
            Text(text = "${setCapitalize(pokemon.name)} is removed at your pocket")
        },
        dismissButton = {
            TextButton(
                onClick = {
                    setShowDialog(false)
                }
            ) {
                Text("Cancel")
            }
        },
    )
}
