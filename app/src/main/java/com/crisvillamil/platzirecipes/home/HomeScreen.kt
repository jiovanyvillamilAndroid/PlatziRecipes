package com.crisvillamil.platzirecipes.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.crisvillamil.platzirecipes.ItemImage
import com.crisvillamil.platzirecipes.NavigationScreens
import com.crisvillamil.platzirecipes.R
import com.crisvillamil.platzirecipes.ui.theme.light_gray_color


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeUIState: HomeUIState,
    onHomeUIEvent: (HomeEvent) -> Unit,
) {
    LaunchedEffect(key1 = true, block = {
        onHomeUIEvent(HomeEvent.OnFetchRecipes)
    })
    HomeContent(
        modifier = modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        navController = navController,
        state = homeUIState,
        onFavoriteClick = { onHomeUIEvent(HomeEvent.OnFavoriteClick(it)) },
        onSearch = { onHomeUIEvent(HomeEvent.OnSearch(it)) },
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier,
    navController: NavController,
    state: HomeUIState,
    onSearch: (String) -> Unit,
    onFavoriteClick: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        SearchBar(onSearch = {
            onSearch(it)
        })
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            ItemsList(
                navController = navController,
                state = state,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(text)
        },
        label = { Text("Encuentra tu receta") },
        leadingIcon = { Icon(painterResource(id = R.drawable.search), contentDescription = null) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch(text)
            keyboardController?.hide()
            focusManager.clearFocus()
        })
    )
}

@Composable
fun ItemsList(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: HomeUIState,
    onFavoriteClick: (Int) -> Unit,
) {
    val recipesList = state.recipes
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        content = {
            items(
                items = recipesList,
                key = { item ->
                    item.recipeId
                }
            ) { recipe ->
                RecipeItem(
                    recipeItemState = recipe,
                    navController = navController,
                    onFavoriteClick = {
                        onFavoriteClick(recipe.recipeId)
                    },
                )
            }
        }
    )
}

@Composable
fun RecipeItem(
    modifier: Modifier = Modifier,
    recipeItemState: RecipeItemState,
    navController: NavController,
    onFavoriteClick: () -> Unit,
) {

    Column(modifier = modifier.clickable {
        navController.navigate("${NavigationScreens.Detail.route}/${recipeItemState.recipeId}") {
            launchSingleTop = true
            restoreState = true
        }
    }) {
        Spacer(modifier = Modifier.height(32.dp))
        ItemImage(
            modifier = Modifier.fillMaxWidth(),
            imageUrl = recipeItemState.imageUrl,
            rating = recipeItemState.rating,
            isFavorite = recipeItemState.isFavorite,
            onFavoriteClick = onFavoriteClick,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = recipeItemState.title,
            style = MaterialTheme.typography.titleLarge,
        )
        AuthorLabel(
            authorName = recipeItemState.authorName,
            authorImageUrl = recipeItemState.authorImageUrl,
        )
    }
}

@Composable
private fun AuthorLabel(modifier: Modifier = Modifier, authorName: String, authorImageUrl: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            model = authorImageUrl,
            contentDescription = "author image"
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = "Por $authorName",
            color = light_gray_color
        )
    }
}


@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen(
        navController = rememberNavController(),
        homeUIState = HomeUIState(),
        onHomeUIEvent = {})
}