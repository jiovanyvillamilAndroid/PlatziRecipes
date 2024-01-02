package com.crisvillamil.platzirecipes.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.crisvillamil.platzirecipes.NavigationScreens
import com.crisvillamil.platzirecipes.R
import com.crisvillamil.platzirecipes.ui.theme.light_gray_color
import com.crisvillamil.platzirecipes.ui.theme.star_color

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    favoriteUIState: FavoriteUIState,
    onFavoriteUIEvent: (FavoriteEvent) -> Unit,
) {
    LaunchedEffect(key1 = true, block = {
        onFavoriteUIEvent(FavoriteEvent.OnFetchFavoriteRecipes)
    })
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = "Favoritos",
            style = MaterialTheme.typography.titleLarge
        )
        LazyVerticalGrid(
            contentPadding = PaddingValues(8.dp),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favoriteUIState.favorites) { favoriteItem ->
                FavoriteItem(favoriteItemUIState = favoriteItem,
                    onFavoriteUIEvent = onFavoriteUIEvent,
                    onItemClicked = {
                        navController.navigate("${NavigationScreens.Detail.route}/${favoriteItem.recipeId}") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }
}

@Composable
fun FavoriteItem(
    favoriteItemUIState: FavoriteItemUIState,
    onFavoriteUIEvent: (FavoriteEvent) -> Unit,
    onItemClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier.clickable {
            onItemClicked(favoriteItemUIState.recipeId)
        },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                model = favoriteItemUIState.imageURL,
                contentDescription = "item image"
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = favoriteItemUIState.name,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favorites",
                        tint = star_color
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "(${favoriteItemUIState.rate})",
                        style = MaterialTheme.typography.bodySmall,
                        color = light_gray_color
                    )
                }
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.time_circle),
                        contentDescription = "cooking Time", tint = Color.Gray
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = favoriteItemUIState.cookingTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = light_gray_color,
                    )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.show),
                        contentDescription = "Visits",
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = favoriteItemUIState.views,
                        style = MaterialTheme.typography.bodySmall,
                        color = light_gray_color
                    )
                }
                Icon(
                    modifier = Modifier
                        .clickable {
                            onFavoriteUIEvent(
                                FavoriteEvent.OnRemoveFromFavorites(
                                    favoriteItemUIState.recipeId
                                )
                            )
                        },
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}