package com.crisvillamil.platzirecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.crisvillamil.platzirecipes.creation.CreateRecipeScreen
import com.crisvillamil.platzirecipes.creation.CreateRecipeViewModel
import com.crisvillamil.platzirecipes.detail.DetailScreen
import com.crisvillamil.platzirecipes.detail.DetailViewModel
import com.crisvillamil.platzirecipes.favorite.FavoriteScreen
import com.crisvillamil.platzirecipes.favorite.FavoriteViewModel
import com.crisvillamil.platzirecipes.home.HomeScreen
import com.crisvillamil.platzirecipes.home.HomeViewModel
import com.crisvillamil.platzirecipes.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues = paddingValues),
                        navHostController = navController
                    )
                }
            }
        }
    }

    @Composable
    private fun NavHost(modifier: Modifier, navHostController: NavHostController) {
        val homeViewModel = HomeViewModel()
        val createRecipeViewModel = CreateRecipeViewModel()
        val favoriteViewModel = FavoriteViewModel()
        val detailViewModel = DetailViewModel()
        NavHost(
            modifier = modifier,
            navController = navHostController,
            startDestination = NavigationScreens.Home.route,
            builder = {
                composable(NavigationScreens.Home.route) {
                    HomeScreen(
                        navController = navHostController,
                        homeUIState = homeViewModel.state,
                        onHomeUIEvent = {
                            homeViewModel.onEvent(it)
                        })
                }
                composable(route = NavigationScreens.Create.route) {
                    CreateRecipeScreen(
                        navController = navHostController,
                        createRecipeUIState = createRecipeViewModel.state,
                        onCreateRecipeEvent = {
                            createRecipeViewModel.onEvent(it)
                        }
                    )
                }
                composable(route = NavigationScreens.Favorite.route) {
                    FavoriteScreen(
                        navController = navHostController,
                        favoriteUIState = favoriteViewModel.state,
                        onFavoriteUIEvent = {
                            favoriteViewModel.onEvent(it)
                        }
                    )
                }
                composable(
                    route = "${NavigationScreens.Detail.route}/{recipeId}",
                    arguments = listOf(
                        navArgument("recipeId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId", null).orEmpty()
                    detailViewModel.recipeId = recipeId
                    DetailScreen(
                        navController = navHostController,
                        detailUIState = detailViewModel.state,
                        onDetailEvent = {
                            detailViewModel.onEvent(it)
                        }
                    )
                }
            }
        )
    }

    @Composable
    private fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavController) {
        var navigationSelectedItem by remember { mutableIntStateOf(0) }
        NavigationBar(
            modifier = modifier
                .shadow(8.dp),
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            BottomNavigationItem().bottomNavigationItems()
                .forEachIndexed { index, bottomNavigationItem ->
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(bottomNavigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(text = bottomNavigationItem.label) },
                        icon = {
                            Icon(
                                bottomNavigationItem.icon,
                                bottomNavigationItem.label
                            )
                        })
                }
        }
    }
}