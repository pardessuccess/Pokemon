package com.sillazy.pokemon.presentation.search

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sillazy.pokemon.R
import com.sillazy.pokemon.Resource
import com.sillazy.pokemon.data.model.PokemonListDto
import com.sillazy.pokemon.data.model.Result
import com.sillazy.pokemon.presentation.component.PokemonBottomBar
import com.sillazy.pokemon.presentation.navgraph.Screen
import com.sillazy.pokemon.setCapitalize
import com.sillazy.pokemon.toastText
import com.sillazy.pokemon.ui.theme.Typography

@Composable
fun SearchScreen(
    onPokemonClick: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val preferencesManager = PreferencesManager(context)

    var textState by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(preferencesManager.getData()) }
    var pokemonAll by remember { mutableStateOf(listOf<Result>()) }

    var first by remember { mutableStateOf(false) }

    val pokemonAllList = produceState<Resource<PokemonListDto>>(initialValue = Resource.Loading()) {
        value = viewModel.pokemonAll.value
    }.value

    LaunchedEffect(textState) {
        if (textState.isEmpty() and first) {
            pokemonAll = viewModel.pokemonList.value!!
        }
    }

    LaunchedEffect(active) {
        history = preferencesManager.getData()
        println(viewModel.getAllPokemon())
    }

    Scaffold(
        bottomBar = {
            PokemonBottomBar(
                tabs = Screen.values(),
                currentRoute = Screen.Book.route,
                navigateToRoute = onNavigateToRoute
            )
        },
    ) {
        Surface(
            modifier = Modifier
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                model = R.drawable.pokemon_wallpaper3, contentDescription = "null"
            )
            Column {
                SearchSection(
                    context = context,
                    active = { active },
                    setActive = { active = it },
                    preferencesManager = preferencesManager,
                    textState = { textState },
                    setTextState = { textState = it },
                    pokemonAll = { pokemonAll },
                    setPokemonAll = { pokemonAll = it },
                    viewModel = viewModel,
                    history = { history!! },
                    setHistory = { history = it }
                )

                if (pokemonAllList is Resource.Success) {
                    if (!first) {
                        pokemonAll = viewModel.pokemonList.value!!
                    }
                    first = true
                    ResultSection(
                        preferencesManager = preferencesManager,
                        pokemonAll = { pokemonAll },
                        onPokemonClick = { onPokemonClick(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultSection(
    preferencesManager: PreferencesManager,
    pokemonAll: () -> List<Result>,
    onPokemonClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        items(pokemonAll().size, key = { it }) { index ->
            val id = pokemonAll()[index].url.split("/").dropLast(1).last()
            Row(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
                    .background(
                        Color(0xFF6390F0).copy(0.9f),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .animateItemPlacement(
                        animationSpec = tween(600)
                    )
                    .clickable {
                        onPokemonClick(id)
                        preferencesManager.saveData(
                            System
                                .currentTimeMillis()
                                .toString(),
                            pokemonAll()[index].name
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 10.dp)
                        .animateItemPlacement(),
                    text = "#${id}",
                    fontSize = 20.sp,
                    style = Typography.bodyMedium,
                )
                Divider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(vertical = 2.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = setCapitalize(pokemonAll()[index].name),
                    fontSize = 25.sp,
                    style = Typography.bodyMedium,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    context: Context,
    viewModel: SearchViewModel,
    preferencesManager: PreferencesManager,
    active: () -> Boolean,
    setActive: (Boolean) -> Unit,
    textState: () -> String,
    setTextState: (String) -> Unit,
    pokemonAll: () -> List<Result>,
    setPokemonAll: (List<Result>) -> Unit,
    history: () -> MutableMap<String, *>,
    setHistory: (MutableMap<String, *>) -> Unit
) {
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(if (active()) 0.dp else 10.dp),
        query = textState(),
        onQueryChange = { setTextState(it) },
        onSearch = { text ->
            setActive(false)
            preferencesManager.saveData(System.currentTimeMillis().toString(), text)
            setPokemonAll(pokemonAll().filter { result ->
                result.name.contains(text.lowercase())
            })
            if (pokemonAll().isEmpty()) {
                toastText(context, "It's not here. T.T")
                setTextState("")
                setPokemonAll(viewModel.pokemonList.value!!)
            }
        },
        active = active(),
        onActiveChange = { setActive(it) },
        placeholder = {
            Text(text = "Search...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (active()) {
                Icon(
                    modifier = Modifier.clickable {
                        if (textState().isNotEmpty()) {
                            setTextState("")
                        } else {
                            setActive(false)
                        }
                    },
                    imageVector = Icons.Default.Close, contentDescription = "Close Icon"
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            if (textState().isEmpty()) {
                                setPokemonAll(pokemonAll().shuffled())
                                viewModel.setPokemonListShuffled(pokemonAll())
                            }
                        },
                    painter = painterResource(id = R.drawable.exchange),
                    tint = Color.Black.copy(0.8f),
                    contentDescription = "Close Icon"
                )
            }
        },

        ) {
        val scrollable = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollable)) {
            history().forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, top = 14.dp, end = 14.dp)
                        .clickable {
                            setTextState(it.value.toString())
                            setActive(false)
                            setPokemonAll(pokemonAll().filter { result ->
                                result.name.contains(textState().lowercase())
                            })
                            if (pokemonAll().isEmpty()) {
                                toastText(
                                    context,
                                    "It's not here. T.T"
                                )
                                setPokemonAll(viewModel.pokemonList.value!!)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.History,
                        contentDescription = "History Icon"
                    )
                    Text(
                        text = it.value.toString(),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        preferencesManager.deleteData(it.key)
                        preferencesManager.getData()?.let { history -> setHistory(history) }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "delete history"
                        )
                    }
                }
            }
        }
    }
}

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    fun saveData(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(): MutableMap<String, *>? {
        return sharedPreferences.all
    }

    fun deleteData(key: String) {
        editor.remove(key)
        editor.commit()
    }
}