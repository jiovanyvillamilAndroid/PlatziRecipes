package com.crisvillamil.platzirecipes.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.domain.GetFavoriteRecipesUseCase
import com.crisvillamil.platzirecipes.domain.RemoveFromFavoriteRecipeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(
    private val getFavoriteRecipeUseCase: GetFavoriteRecipesUseCase,
    private val removeFromFavoriteRecipeUseCase: RemoveFromFavoriteRecipeUseCase
) : ViewModel() {

    var state by mutableStateOf(FavoriteUIState())
        private set

    fun onEvent(favoriteEvent: FavoriteEvent) {
        when (favoriteEvent) {
            FavoriteEvent.OnFetchFavoriteRecipes -> fetchFavoriteRecipes()
            is FavoriteEvent.OnRemoveFromFavorites -> removeFromFavorite(favoriteEvent.recipeId)
        }
    }

    private fun removeFromFavorite(recipeId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                removeFromFavoriteRecipeUseCase(recipeId)
            }
            val favorites = withContext(Dispatchers.IO) {
                getFavoriteRecipeUseCase().map {
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
            }
            state = state.copy(
                isLoading = false,
                favorites = favorites
            )
        }
    }

    private fun fetchFavoriteRecipes() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            val favorites = withContext(Dispatchers.IO) {
                getFavoriteRecipeUseCase().map {
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
            }
            state = state.copy(
                isLoading = false,
                favorites = favorites
            )
        }
    }
}