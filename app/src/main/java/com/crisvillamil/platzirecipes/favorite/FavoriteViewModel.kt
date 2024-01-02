package com.crisvillamil.platzirecipes.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.model.FakeDataProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    var state by mutableStateOf(FavoriteUIState())
        private set

    fun onEvent(favoriteEvent: FavoriteEvent) {
        when (favoriteEvent) {
            FavoriteEvent.OnFetchFavoriteRecipes -> fetchFavoriteRecipes()
            is FavoriteEvent.OnRemoveFromFavorites -> removeFromFavorite(favoriteEvent.recipeId)
        }
    }

    private fun removeFromFavorite(recipeId: Int) {
            val recipe = FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.first { it.recipeId == recipeId }
            FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.remove(recipe)
            state = state.copy(
                isLoading = false,
                favorites = FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.map {
                    with(it) {
                        FavoriteItemUIState(
                            recipeId = it.recipeId,
                            imageURL = imageUrl.orEmpty(),
                            name = name,
                            cookingTime = cookingTime,
                            rate = rating ?: 0F,
                            views = it.viewsCount
                        )
                    }
                }
            )
    }

    private fun fetchFavoriteRecipes() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            delay(1000L)
            state = state.copy(
                isLoading = false,
                favorites = FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.map {
                    with(it) {
                        FavoriteItemUIState(
                            recipeId = recipeId,
                            imageURL = imageUrl.orEmpty(),
                            name = name,
                            cookingTime = cookingTime,
                            rate = rating ?: 0F,
                            views = it.viewsCount
                        )
                    }
                }
            )
        }
    }
}