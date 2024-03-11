package com.sillazy.pokemon.presentation.book

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sillazy.pokemon.R
import com.sillazy.pokemon.calcDominantColor
import com.sillazy.pokemon.data.model.Result
import com.sillazy.pokemon.getImageUrl
import com.sillazy.pokemon.presentation.component.PokemonBottomBar
import com.sillazy.pokemon.presentation.navgraph.Screen
import com.sillazy.pokemon.setCapitalize
import com.sillazy.pokemon.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun BookScreen(
    onPokemonClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: BookViewModel = hiltViewModel(),
) {

    val lazyGridState = rememberLazyGridState()
    val pokemonList = viewModel.pokemonList.collectAsLazyPagingItems()
    var expand by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxWidth(),
        topBar = { BookTopBar() },
        bottomBar = {
            PokemonBottomBar(
                tabs = Screen.values(),
                currentRoute = Screen.Search.route,
                navigateToRoute = onNavigateToRoute,
                modifier = Modifier.fillMaxWidth()
            )
        },
    ) {

        Surface(
            modifier = Modifier
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Blue.copy(0.3f)),
            ) {
                BookSection(
                    lazyGridState = lazyGridState,
                    pokemonList = pokemonList,
                    onPokemonClick = { onPokemonClick(it) },
                )
                FloatingButtonSection(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    lazyGridState = lazyGridState,
                    expand = { expand },
                    setExpand = { expand = it }
                )
            }
        }
    }
}

@Composable
fun BookSection(
    lazyGridState: LazyGridState,
    pokemonList: LazyPagingItems<Result>,
    onPokemonClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = lazyGridState
    ) {
        items(pokemonList.itemCount) { count ->
            pokemonList[count]?.let {
                PokemonComponent(result = it, onPokemonClick)
            }
        }
    }
}

@Composable
fun FloatingButtonSection(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    expand: () -> Boolean,
    setExpand: (Boolean) -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = expand()) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (lazyGridState.firstVisibleItemIndex > 6) {
                            lazyGridState.animateScrollToItem(
                                lazyGridState.firstVisibleItemIndex - 6, 0,
                            )
                        } else {
                            lazyGridState.animateScrollToItem(0, 0)
                        }
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.1f))
                    .size(60.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_up),
                    contentDescription = "scroll up",
                    Modifier.size(50.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(
            onClick = {
                setExpand(!expand())
            },
            modifier = Modifier
                .background(Color.Black.copy(0.0f)),
            shape = CircleShape,
        ) {
            if (!expand()) {
                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = painterResource(id = R.drawable.pokeball),
                    contentDescription = ""
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Color.White.copy(0f),
                            CircleShape
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "icon close"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        AnimatedVisibility(visible = expand()) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (lazyGridState.firstVisibleItemIndex < 1000) {
                            lazyGridState.animateScrollToItem(
                                lazyGridState.firstVisibleItemIndex + 6, 0,
                            )
                        } else {
                            lazyGridState.animateScrollToItem(
                                1000, 0
                            )
                        }
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(0.1f))
                    .size(60.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "scroll down",
                    Modifier.size(50.dp)
                )
            }
        }
    }
}


@Composable
fun BookTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue.copy(0.3f)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .height(100.dp)
                .padding(top = 10.dp),
            model = R.drawable.pokemon_logo,
            contentDescription = "Pokemon Logo"
        )
    }
}


@Composable
fun PokemonComponent(
    result: Result,
    onPokemonClick: (String) -> Unit
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.radialGradient(
                    listOf(
                        dominantColor.copy(0.1f),
                        dominantColor.copy(0.2f),
                        dominantColor.copy(1f),
                    )
                )
            )
            .clickable { onPokemonClick(result.name) }

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val name = result.name
            val number = if (result.url.endsWith("/")) {
                result.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                result.url.takeLastWhile { it.isDigit() }
            }

            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                model = ImageRequest.Builder(LocalContext.current).data(
                    getImageUrl(number.toInt())
                ).crossfade(true).build(),
                contentDescription = name,
                onSuccess = { success ->
                    calcDominantColor(success.result.drawable) { color ->
                        dominantColor = color
                    }
                }
            )
            Text(
                text = setCapitalize(result.name),
                style = Typography.bodySmall,
                fontSize = 20.sp,
            )
        }
    }
}

