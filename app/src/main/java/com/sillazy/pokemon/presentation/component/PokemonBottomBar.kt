package com.sillazy.pokemon.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.sillazy.pokemon.presentation.navgraph.Screen
import com.sillazy.pokemon.ui.theme.TypeWater

// 바텀 네비게이션 바

@Composable
fun PokemonBottomBar(
    tabs: Array<Screen>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    var size by remember { mutableStateOf(Size.Zero) }
    val routes = remember { tabs.map { it.route } }
    val currentSection = tabs.first { it.route == currentRoute }
    Row(
        modifier = modifier
            .background(TypeWater)
            .onGloballyPositioned {
                size = it.size.toSize()
            }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEach { section ->
            val selected = section == currentSection
            PokemonBottomNavigationItem(
                icon = section.icon,
                text = section.title,
                selected = selected,
                onSelected = { navigateToRoute(section.route) },
                size = configuration.screenWidthDp.dp
            )
        }
    }
}

// 바텀 네비게이션 요소

@Composable
fun PokemonBottomNavigationItem(
    icon: ImageVector,
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
    size: Dp
) {
    BottomNavItemLayout(
        icon = icon,
        text = text,
        modifier = Modifier
            .selectable(selected = selected, onClick = onSelected),
        size = size / 3
    )
}

// 바텀 네비게이션 레이아웃

@Composable
fun BottomNavItemLayout(
    icon: ImageVector,
    text: String,
    modifier: Modifier,
    size: Dp,
) {
    println(size)
    Column(
        modifier = modifier
            .width(size)
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Text(text = text)
    }
}