package com.sillazy.pokemon.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sillazy.pokemon.R
import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.calcDominantColor
import com.sillazy.pokemon.data.model.PokemonDto
import com.sillazy.pokemon.data.model.poke.Move
import com.sillazy.pokemon.getImageUrl
import com.sillazy.pokemon.parseStatToColor
import com.sillazy.pokemon.pokemonType
import com.sillazy.pokemon.setCapitalize
import com.sillazy.pokemon.ui.theme.Typography
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.round

@Composable
fun DetailScreen(
    pokemonName: String,
    fightPress: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resource<PokemonDto>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemon(pokemonName)
    }.value

    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var backgroundColor by remember { mutableStateOf(defaultDominantColor) }

    val state = rememberLazyListState()
    val density = LocalDensity.current

    var loadingDelay by remember { mutableStateOf(true) }

    if (pokemonInfo is Resource.Success) {
        val data = pokemonInfo.data!!
        viewModel.setPokemon(data)
        Box(
            modifier = Modifier
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
                    .border(
                        width = 8.dp,
                        color = Color.Gray.copy(0.5f),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TopSection(data)
                Spacer(modifier = Modifier.height(10.dp))
                ImageSection(data, dominantColor = {
                    backgroundColor = it
                    viewModel.setBackgroundColor(it)
                })
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 30.dp),
                    state = state
                ) {
                    item { PhysicalSection(data) }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item { PokemonStatsSection(data) }
                    item { SkillSection(data, backgroundColor) }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item { AbilitySection(data, backgroundColor) }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = !state.canScrollBackward,
                enter = slideInVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically {
                    with(density) { 40.dp.roundToPx() }
                } + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { fightPress("HI") },
                    modifier = Modifier
                        .background(Color.Black.copy(0.0f))
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    shape = CircleShape,
                ) {
                    Image(
                        modifier = Modifier.size(90.dp),
                        painter = painterResource(id = R.drawable.pokeball), contentDescription = ""
                    )
                }
            }
        }
    }
    LaunchedEffect(true) {
        delay(2000)
        loadingDelay = false
    }
    LoadingAnimation { loadingDelay }
}

@Composable
fun LoadingAnimation(loadingDelay: () -> Boolean) {

    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { 15.dp.toPx() }
    val lastCircle = circleValues.size - 1
    if (loadingDelay()) {
        circles.forEachIndexed { index, animatable ->
            LaunchedEffect(key1 = animatable) {
                delay(index * 200L)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 1200
                            0.0f at 0 with LinearOutSlowInEasing
                            1.0f at 300 with LinearOutSlowInEasing
                            0.0f at 600 with LinearOutSlowInEasing
                            0.0f at 1200 with LinearOutSlowInEasing
                        },
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White.copy(0.5f)
                )
        ) {
            Row(modifier = Modifier.align(Alignment.Center)) {
                circleValues.forEachIndexed { index, value ->
                    Image(
                        painterResource(id = R.drawable.pokeball),
                        modifier = Modifier
                            .size(25.dp)
                            .graphicsLayer {
                                translationY = -value * distance
                            },
                        contentDescription = "loading item"
                    )
                    if (index != lastCircle) {
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TopSection(
    data: PokemonDto
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 15.dp,
                        bottomEnd = 15.dp
                    )
                )
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Gray.copy(0.5f),
                            Color.White,
                            Color.Gray.copy(0.8f)
                        )
                    )
                )
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "No.${data.id}",
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp, end = 10.dp)
            )
        }

        Text(
            text = setCapitalize(data.name),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            style = Typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 10.dp)
                .width(200.dp)
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(40.dp)
                .padding(end = 15.dp, top = 5.dp)
        ) {
            for (type in data.types) {
                AsyncImage(
                    modifier = Modifier,
                    model = pokemonType(type),
                    contentDescription = type.type.name
                )
            }
        }
    }
}

@Composable
fun ImageSection(
    data: PokemonDto,
    dominantColor: (Color) -> Unit
) {
    AsyncImage(
        modifier = Modifier
            .size(250.dp)
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium),
        model = ImageRequest.Builder(LocalContext.current).data(
            getImageUrl(data.id)
        ).crossfade(true).build(),
        onSuccess = { success ->
            calcDominantColor(success.result.drawable) { color ->
                dominantColor(color)
            }
        },
        contentDescription = data.name,
    )
}

@Composable
fun PhysicalSection(
    data: PokemonDto
) {
    Column(
        modifier = Modifier
    ) {
        PokemonDetailDataSection(
            data.weight, data.height
        )
    }
}

@Composable
fun AbilitySection(
    data: PokemonDto,
    backgroundColor: Color
) {
    Text(
        text = "Abilities:",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    LazyRow(
        modifier = Modifier.padding(top = 15.dp)
    ) {
        repeat(data.abilities.size) {
            item {
                Box(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .background(
                            backgroundColor.copy(0.6f),
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(10.dp),
                        text = setCapitalize(data.abilities[it].ability.name),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonStatsSection(
    pokemonInfo: PokemonDto,
) {
    val maxBaseStat = remember {
        pokemonInfo.stats.maxOf {
            it.base_stat
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Base stats:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        for (i in pokemonInfo.stats.indices) {
            val stat = pokemonInfo.stats[i]
            PokemonStatComponent(
                statName = stat.stat.name.capitalize(Locale.ROOT),
                statValue = stat.base_stat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = i * 100
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SkillSection(
    data: PokemonDto,
    backgroundColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Skills:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                expanded = !expanded
            }) {
                if (expanded) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow Down"
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Arrow Up"
                    )
                }
            }
        }
        LazyVerticalGrid(
            modifier = Modifier.height(
                if (expanded) 300.dp else 110.dp
            ),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(data.moves.size) { count ->
                SkillComponent(skill = data.moves[count], backgroundColor = backgroundColor)
            }
        }
    }
}

@Composable
fun SkillComponent(
    skill: Move,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor.copy(0.8f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .align(Alignment.Center),
            text = setCapitalize(skill.move.name),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun PokemonStatComponent(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        ), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                Color.LightGray
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember { round(pokemonWeight * 100f) / 1000f }
    val pokemonHeightInMeters = remember { round(pokemonHeight * 100f) / 1000f }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.weight2),
            modifier = Modifier.weight(1f),
            iconSize = 70
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ceiling),
            modifier = Modifier.weight(1f),
            iconSize = 80
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier,
    iconSize: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            modifier = Modifier.size(iconSize.dp),
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            fontStyle = FontStyle.Normal,
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
