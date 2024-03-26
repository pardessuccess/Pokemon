package com.sillazy.pokemon.presentation.fight

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sillazy.pokemon.R
import com.sillazy.pokemon.getGifUrl
import com.sillazy.pokemon.getImageUrl
import com.sillazy.pokemon.presentation.detail.DetailViewModel
import com.sillazy.pokemon.setCapitalize
import com.sillazy.pokemon.toastText
import com.sillazy.pokemon.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FightScreen(
    viewModel: DetailViewModel,
    upPress: () -> Unit
) {
    val context = LocalContext.current
    var first by remember { mutableStateOf(false) }

    var level by remember {
        mutableStateOf(10)
    }

    if (!first) {
        level = Random.nextInt(5, 50)
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            var hpStatus by remember { mutableIntStateOf(1) }
            val name = viewModel.pokemon.value?.name?.let { setCapitalize(it) } ?: ""
            hpStatus = viewModel.pokemon.value!!.stats[0].base_stat

            var numberSkill by remember { mutableIntStateOf(0) }
            first = true

            FieldSection(
                level = { level },
                name = name,
                context = context,
                viewModel = viewModel,
                hpStatus = { hpStatus },
                numberSkill = { numberSkill },
                numberState = { numberSkill = it },
                upPress = { upPress() }
            )

            Spacer(modifier = Modifier.height(20.dp))
            SkillSection(
                thunderPunchOnClicked = {
                    if (numberSkill == 0) {
                        numberSkill = 1
                        hpStatus -= 10
                    }
                },
                thunderboltOnClicked = {
                    if (numberSkill == 0) {
                        numberSkill = 2
                        hpStatus -= 15
                    }
                },
                quickAttackOnClicked = {
                    if (numberSkill == 0) {
                        numberSkill = 3
                        hpStatus -= 5
                    }
                },
                provokeOnClicked = {
                    if (numberSkill == 0) {
                        numberSkill = 4
                    }
                },
            )
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .weight(1f),
                onClick = {
                    if (hpStatus <= viewModel.hp.value!! / 2f) {
                        viewModel.catchPokemon()
                        toastText(
                            context,
                            "Catch a ${name}!",
                        )
                        upPress()
                    } else {
                        toastText(
                            context, "$name is not exhausted yet!"
                        )
                    }
                }
            ) {
                Text(text = "Catch !!!")
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
        LoadingSection(
            context = context,
            viewModel = viewModel
        )
    }
}

@Composable
fun LoadingSection(
    context: Context,
    viewModel: DetailViewModel
) {
    var introVisibility by remember { mutableStateOf(true) }

    val loadingPokemonImage = getImageUrl(viewModel.number.value!!.toInt())

    LaunchedEffect(Unit) {
        delay(1000L)
        introVisibility = false
    }

    AnimatedVisibility(
        visible = introVisibility,
        enter = fadeIn(),
        exit = slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            ),
            targetOffsetX = { -1000 }
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    AsyncImage(
                        modifier = Modifier
                            .size(200.dp)
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium)
                            .weight(1f),
                        model = ImageRequest.Builder(context).data(
                            loadingPokemonImage
                        ).crossfade(true).build(),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                AsyncImage(
                    modifier = Modifier.size(200.dp),
                    model = R.drawable.pikachu_front2,
                    contentDescription = ""
                )
            }
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Gray.copy(0.5f),
                    start = Offset.Zero,
                    end = Offset(size.width * 1.5f, size.height * 1.5f),
                    strokeWidth = 450f,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun operateSkill(
    skill: (Boolean) -> Unit,
    vibrator: Vibrator,
    setNumber: (Int) -> Unit
) {
    skill(true)
    vibrator.vibrate(
        VibrationEffect.createOneShot(
            500,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
    )
    delay(500L)
    skill(false)
    setNumber(0)
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun FieldSection(
    level: () -> Int,
    name: String,
    context: Context,
    viewModel: DetailViewModel,
    hpStatus: () -> Int,
    numberSkill: () -> Int,
    numberState: (Int) -> Unit,
    upPress: () -> Unit,
) {
    val config = LocalConfiguration.current
    val vibrator = context.getSystemService(Vibrator::class.java)

    val imageLoader = ImageLoader.Builder(context).components {
        if (Build.VERSION.SDK_INT >= 28) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }
    }.build()

    var pokemonGif by remember {
        mutableStateOf(
            getGifUrl(viewModel.number.value.toString())
        )
    }

    val shake by remember { mutableStateOf(Animatable(0f)) }
    var offsetTarget by remember { mutableStateOf(Offset.Zero) }

    val quickAttackOffset by animateOffsetAsState(
        animationSpec = tween(500, easing = EaseInBack),
        targetValue = offsetTarget,
        label = ""
    )

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dizzy))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    var skillThunderPunch by remember { mutableStateOf(false) }
    var skillThunderbolt by remember { mutableStateOf(false) }
    var skillQuickAttack by remember { mutableStateOf(false) }
    var skillProvoke by remember { mutableStateOf(false) }

    if (hpStatus() <= 0) {
        toastText(context, "You kill $name")
        upPress()
    }
    LaunchedEffect(numberSkill()) {
        when (numberSkill()) {
            1 -> {
                operateSkill({ skillThunderPunch = it }, vibrator, numberState)
            }

            2 -> {
                operateSkill({ skillThunderbolt = it }, vibrator, numberState)
            }

            3 -> {
                offsetTarget =
                    Offset(
                        config.screenWidthDp.toFloat() / 2f,
                        -config.screenWidthDp.toFloat() / 2f
                    )

                delay(500L)
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                delay(200L)
                offsetTarget = Offset(0f, 0f)
                numberState(0)
            }

            4 -> {
                skillProvoke = true
                for (i in 0..10) {
                    when (i % 2) {
                        0 -> shake.animateTo(5f, spring(stiffness = 100_000f))
                        else -> shake.animateTo(-5f, spring(stiffness = 100_000f))
                    }
                }
                shake.animateTo(0f)
                skillProvoke = false
                numberState(0)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Color.Blue.copy(0.1f))
    ) {
        Row(
            modifier = Modifier.padding(top = 85.dp, start = 20.dp)
        ) {
            HpStats(
                name,
                viewModel.hp.value!!,
                { level() },
                { hpStatus() })
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 40.dp, top = 50.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(130.dp)
                    .align(Alignment.Center)
                    .onGloballyPositioned { coordinates ->
//                        targetPosition(coordinates.positionInWindow())
                    },
                model = ImageRequest.Builder(context)
                    .data(pokemonGif)
                    .decoderFactory(ImageDecoderDecoder.Factory())
                    .build(),
                onError = {
                    pokemonGif =
                        getImageUrl(viewModel.number.value!!.toInt())
                },
                contentDescription = null
            )
            if (skillThunderPunch) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    model = R.drawable.skill_punch,
                    contentDescription = "",
                )
            }
            if (skillThunderbolt) {
                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    model = R.drawable.skill_thunderbolt,
                    contentDescription = ""

                )
            }
            if (skillProvoke) {
                LottieAnimation(
                    modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.TopCenter)
                        .alpha(0.8f),
                    composition = composition, progress = { progress })
            }
        }
        Box(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
//                    setPikachuPosition(coordinates.positionInWindow())
                }
                .offset(
                    when (numberSkill()) {
                        3 -> quickAttackOffset.x.dp
                        4 -> shake.value.roundToInt().dp
                        else -> 0.dp
                    },
                    when (numberSkill()) {
                        3 -> quickAttackOffset.y.dp
                        else -> {
                            0.dp
                        }
                    }
                )
                .align(Alignment.BottomStart)
                .padding(start = 15.dp, bottom = 20.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
                    .onGloballyPositioned { coordinates ->
//                        targetPosition(coordinates.positionInWindow())
                    },
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(R.drawable.pikachu_back_animation)
                        .build(),
                    imageLoader
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Skill(
    modifier: Modifier = Modifier,
    skillName: String, skill: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(15.dp))
            .background(
                Color.Yellow.copy(0.5f)
            )
            .clickable {
                skill()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = skillName, style = Typography.bodyMedium,
            fontSize = 20.sp
        )
    }
}

@Composable
fun SkillSection(
    thunderPunchOnClicked: () -> Unit,
    thunderboltOnClicked: () -> Unit,
    quickAttackOnClicked: () -> Unit,
    provokeOnClicked: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 15.dp)
    ) {
        Skill(
            skillName = "Thunder Punch",
            modifier = Modifier.weight(1f),
            skill = thunderPunchOnClicked
        )
        Spacer(modifier = Modifier.width(15.dp))
        Skill(
            skillName = "Thunderbolt",
            modifier = Modifier.weight(1f),
            skill = thunderboltOnClicked
        )
    }
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 15.dp)
    ) {
        Skill(
            skillName = "Quick Attack",
            modifier = Modifier.weight(1f),
            skill = quickAttackOnClicked
        )
        Spacer(modifier = Modifier.width(15.dp))
        Skill(
            skillName = "Provoke",
            modifier = Modifier.weight(1f),
            skill = provokeOnClicked
        )
    }
}

@Composable
fun HpStats(name: String, maxHp: Int, level: () -> Int, hpStatus: () -> Int) {
    Column(
        modifier = Modifier
            .width(180.dp)
            .height(65.dp)
            .background(
                color = Color(0xFFEFF0C6),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 5.dp,
                    topEnd = 5.dp
                )
            )
            .border(
                width = 2.dp,
                color = Color(0xFF3F3F3F).copy(0.6f),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 5.dp,
                    topEnd = 5.dp
                )
            ),
    ) {
        var animationPlayed by remember { mutableStateOf(false) }
        val curPercent = animateFloatAsState(
            targetValue = if (animationPlayed) hpStatus() / maxHp.toFloat() else 0f,
            animationSpec = tween(
                1000,
            ), label = ""
        )
        LaunchedEffect(key1 = true) {
            animationPlayed = true
        }
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 5.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name, style = Typography.bodySmall,
                fontSize = 16.sp,
                modifier = Modifier.width(80.dp),
                maxLines = 1
            )
            Spacer(
                modifier = Modifier.weight(1f).padding(end = 3.dp)
            )
            Image(
                painter =
                if (level() % 2 == 0) {
                    painterResource(id = R.drawable.boy)
                } else {
                    painterResource(id = R.drawable.girl)
                },
                contentDescription = "gender icon",
                modifier = Modifier
                    .size(15.dp)
                    .padding(start = 3.dp)
            )
            Text(text = "Lv${level()}", fontWeight = FontWeight.SemiBold)
        }
        Row(modifier = Modifier.padding(end = 5.dp, top = 10.dp)) {
            Spacer(modifier = Modifier.width(30.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .wrapContentHeight()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(
                        color = Color(0xFF636363),
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(20.dp)
                ) {
                    Text(
                        text = "HP",
                        color = Color(0xFFFF9800),
                        modifier = Modifier.padding(horizontal = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(curPercent.value)
                            .padding(1.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(
                                border = BorderStroke(1.dp, Color.White),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                color = Color(0xFF46B517),
                            )
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = hpStatus().toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
