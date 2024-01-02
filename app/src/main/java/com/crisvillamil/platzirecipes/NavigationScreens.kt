package com.crisvillamil.platzirecipes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationScreens(val route: String) {
    data object Home : NavigationScreens("home_route")
    data object Create : NavigationScreens("create_route")
    data object Detail : NavigationScreens("detail_route")
    data object Favorite : NavigationScreens("favorite_route")
}

//initializing the data class with default parameters
data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = NavigationScreens.Home.route
            ),
            BottomNavigationItem(
                label = "Create",
                icon = Icons.Filled.Add,
                route = NavigationScreens.Create.route
            ),
            BottomNavigationItem(
                label = "Favorite",
                icon = Icons.Filled.Favorite,
                route = NavigationScreens.Favorite.route
            ),
        )
    }
}