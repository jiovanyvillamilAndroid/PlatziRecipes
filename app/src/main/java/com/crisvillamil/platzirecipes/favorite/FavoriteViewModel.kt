package com.crisvillamil.platzirecipes.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.domain.GetFavoriteRecipesUseCase
import com.crisvillamil.platzirecipes.domain.RemoveFromFavoriteRecipeUseCase
import com.crisvillamil.platzirecipes.model.FakeDataProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    var state by mutableStateOf(FavoriteUIState())
        private set

    private val getFavoriteRecipeUseCase = GetFavoriteRecipesUseCase()
    private val removeFromFavoriteRecipeUseCase = RemoveFromFavoriteRecipeUseCase()

    fun onEvent(favoriteEvent: FavoriteEvent) {
        when (favoriteEvent) {
            FavoriteEvent.OnFetchFavoriteRecipes -> fetchFavoriteRecipes()
            is FavoriteEvent.OnRemoveFromFavorites -> removeFromFavorite(favoriteEvent.recipeId)
        }
    }

    private fun removeFromFavorite(recipeId: Int) {
        viewModelScope.launch {
            removeFromFavoriteRecipeUseCase(recipeId)
            state = state.copy(
                isLoading = false,
                favorites = getFavoriteRecipeUseCase().map {
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
    }

    private fun fetchFavoriteRecipes() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            state = state.copy(
                isLoading = false,
                favorites = getFavoriteRecipeUseCase().map {
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