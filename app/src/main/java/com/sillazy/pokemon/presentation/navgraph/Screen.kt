package com.sillazy.pokemon.presentation.navgraph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

// 바텀 네비게이션 별로 화면을 구성하는 스크린

enum class Screen(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    Book("Book", Icons.Default.Book, "home/book"),
    Search("Search", Icons.Default.Search, "home/search"),
    Pocket("Pocket", Icons.Default.Collections, "home/pocket"),
//    Detail("Detail", Icons.Default.Details, "detail")
}
