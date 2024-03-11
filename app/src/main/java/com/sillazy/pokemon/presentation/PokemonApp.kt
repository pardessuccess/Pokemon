package com.sillazy.pokemon.presentation

import androidx.compose.runtime.Composable
import com.sillazy.pokemon.presentation.navgraph.rememberPokemonNavController
import com.sillazy.pokemon.presentation.navigation.PokemonNavGraph
import com.sillazy.pokemon.ui.theme.PokemonTheme

@Composable
fun PokemonApp() {
    PokemonTheme {
        val pokemonNavController = rememberPokemonNavController()
        PokemonNavGraph(pokemonNavController = pokemonNavController)
    }
}