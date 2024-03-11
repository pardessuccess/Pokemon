package com.sillazy.pokemon.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.sillazy.pokemon.presentation.book.BookScreen
import com.sillazy.pokemon.presentation.book.BookViewModel
import com.sillazy.pokemon.presentation.pocket.PocketScreen
import com.sillazy.pokemon.presentation.detail.DetailScreen
import com.sillazy.pokemon.presentation.detail.DetailViewModel
import com.sillazy.pokemon.presentation.fight.FightScreen
import com.sillazy.pokemon.presentation.navgraph.PokemonDestinations
import com.sillazy.pokemon.presentation.navgraph.PokemonNavController
import com.sillazy.pokemon.presentation.navgraph.Screen
import com.sillazy.pokemon.presentation.search.SearchScreen
import com.sillazy.pokemon.presentation.search.SearchViewModel

@Composable
fun PokemonNavGraph(
    pokemonNavController: PokemonNavController
) {
    val bookViewModel: BookViewModel = hiltViewModel()
    val detailViewModel: DetailViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()

    NavHost(
        navController = pokemonNavController.navController,
        startDestination = PokemonDestinations.HOME_ROUTE
    ) {
        pokemonGraph(
            onPokemonSelected = pokemonNavController::navigateToDetail,
            onNavigateToRoute = pokemonNavController::navigateToBottomBarRoute,
            fightPress = pokemonNavController::fightPress,
            upPress = pokemonNavController::upPress,
            viewModel = bookViewModel,
            detailViewModel = detailViewModel,
            searchViewModel = searchViewModel
        )
    }
}

fun NavGraphBuilder.pokemonGraph(
    onPokemonSelected: (String, NavBackStackEntry) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    fightPress: (String) -> Unit,
    upPress: () -> Unit,
    viewModel: BookViewModel,
    detailViewModel: DetailViewModel,
    searchViewModel: SearchViewModel
) {
    navigation(
        route = PokemonDestinations.HOME_ROUTE,
        startDestination = Screen.Book.route,
    ) {
        composable(route = Screen.Book.route) { fromBackStackEntry ->
            BookScreen(
                onPokemonClick = { pokemon -> onPokemonSelected(pokemon, fromBackStackEntry) },
                onNavigateToRoute = onNavigateToRoute,
                viewModel = viewModel
            )
        }
        composable(route = Screen.Search.route) { fromBackStackEntry ->
            SearchScreen(
                onPokemonClick = { pokemon -> onPokemonSelected(pokemon, fromBackStackEntry) },
                onNavigateToRoute = onNavigateToRoute,
                viewModel = searchViewModel
            )
        }
        composable(route = Screen.Pocket.route) { fromBackStackEntry ->
            PocketScreen(
                onPokemonClick = { pokemon -> onPokemonSelected(pokemon, fromBackStackEntry) },
                onNavigateToRoute = onNavigateToRoute
            )
        }
    }
    detailGraph(fightPress = fightPress, upPress = upPress, viewModel = detailViewModel)
}

fun NavGraphBuilder.detailGraph(
    fightPress: (String) -> Unit,
    upPress: () -> Unit,
    viewModel: DetailViewModel
) {
    navigation(
        route = PokemonDestinations.DETAIL_DESTINATION,
        startDestination = PokemonDestinations.DETAIL_ROUTE
    ) {
        composable(
            route = "${PokemonDestinations.DETAIL_DESTINATION}/{${PokemonDestinations.DETAIL_ROUTE}}",
            arguments = listOf(navArgument(PokemonDestinations.DETAIL_ROUTE) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val pokemon = arguments.getString(PokemonDestinations.DETAIL_ROUTE)
            DetailScreen(pokemon!!, fightPress, viewModel)
        }
        composable(
            route = "${PokemonDestinations.DETAIL_DESTINATION}/${PokemonDestinations.DETAIL_ROUTE}/{${PokemonDestinations.FIGHT_ROUTE}}",
        ) { backStackEntry ->
            FightScreen(viewModel, upPress)
        }
    }
}