package com.sillazy.pokemon.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object PokemonDestinations {
    const val HOME_ROUTE = "Home"
    const val DETAIL_DESTINATION = "Detail"
    const val DETAIL_ROUTE = "Detail_Route"
    const val FIGHT_ROUTE = "Fight_Route"
}

@Composable
fun rememberPokemonNavController(
    navController: NavHostController = rememberNavController(),
): PokemonNavController = remember(navController) {
    PokemonNavController(navController = navController)
}

class PokemonNavController(
    val navController: NavHostController
) {
    val currentRoute: String? = navController.currentDestination?.route

    fun fightPress(pokemon: String) {
        navController.navigate("${PokemonDestinations.DETAIL_DESTINATION}/${PokemonDestinations.DETAIL_ROUTE}/$pokemon")
    }

    fun upPress() {
        navController.navigateUp()
        navController.navigateUp()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToDetail(pokemon: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate("${PokemonDestinations.DETAIL_DESTINATION}/$pokemon")
        }
    }
}

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) {
        findStartDestination(graph.startDestination!!)
    } else {
        graph
    }
}