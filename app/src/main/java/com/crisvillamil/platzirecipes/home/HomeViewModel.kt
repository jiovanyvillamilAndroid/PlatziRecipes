package com.crisvillamil.platzirecipes.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.domain.AddToFavoriteRecipeUseCase
import com.crisvillamil.platzirecipes.domain.GetIsFavoriteRecipeUseCase
import com.crisvillamil.platzirecipes.domain.GetRecipesUseCase
import com.crisvillamil.platzirecipes.domain.RemoveFromFavoriteRecipeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getIsFavoriteRecipeUseCase: GetIsFavoriteRecipeUseCase,
    private val removeFromFavoriteRecipeUseCase: RemoveFromFavoriteRecipeUseCase,
    private val addToFavoriteRecipeUseCase: AddToFavoriteRecipeUseCase,
) : ViewModel() {

    var state by mutableStateOf(HomeUIState())
        private set

    fun onEvent(homeEvent: HomeEvent) {
        when (homeEvent) {
            is HomeEvent.OnSearch -> onSearch(homeEvent.text)
            HomeEvent.OnFetchRecipes -> fetchRecipes()
            is HomeEvent.OnFavoriteClick -> onFavoriteClick(homeEvent.recipeId)
        }
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            showLoadingState()
            val recipes = withContext(Dispatchers.IO) {
                getRecipesUseCase().map {
                    RecipeItemState(
                        recipeId = it.recipeId,
                        isFavorite = getIsFavoriteRecipeUseCase(it.recipeId),
                        title = it.name,
                        isLoading = false,
                        authorName = it.authorName.orEmpty(),
                        authorImageUrl = it.authorImageUrl.orEmpty(),
                        imageUrl = it.imageUrl.orEmpty(),
                        rating = it.rating.toString(),
                    )
                }
            }
            state = state.copy(
                isLoading = false,
                recipes = recipes,
            )
        }
    }

    private fun onFavoriteClick(recipeId: Int) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = false,
                recipes = state.recipes.toMutableList().also { list ->
                    val index = state.recipes.indexOfFirst { it.recipeId == recipeId }
                    val item = list[index]
                    list[index] = item.copy(
                        isLoading = true
                    )
                }
            )
            withContext(Dispatchers.IO) {
                if (getIsFavoriteRecipeUseCase(recipeId)) {
                    removeFromFavoriteRecipeUseCase(recipeId)
                } else {
                    addToFavoriteRecipeUseCase(recipeId)
                }
            }
            state = state.copy(
                isLoading = false,
                recipes = state.recipes.toMutableList().also { list ->
                    val index = state.recipes.indexOfFirst { it.recipeId == recipeId }
                    val item = list[index]
                    list[index] = item.copy(
                        isLoading = false,
                        isFavorite = !item.isFavorite
                    )
                }
            )

        }
    }

    private fun onSearch(text: String) {
        viewModelScope.launch {
            val recipes = withContext(Dispatchers.IO) {
                getRecipesUseCase().map {
                    RecipeItemState(
                        recipeId = it.recipeId,
                        isFavorite = getIsFavoriteRecipeUseCase(it.recipeId),
                        title = it.name,
                        isLoading = false,
                        authorName = it.authorName.orEmpty(),
                        authorImageUrl = it.authorImageUrl.orEmpty(),
                        imageUrl = it.imageUrl.orEmpty(),
                        rating = it.rating.toString(),
                    )
                }.filter {
                    it.title.contains(
                        text,
                        ignoreCase = true
                    )
                }
            }
            state = state.copy(
                recipes = recipes
            )
        }
    }

    private fun showLoadingState() {
        state = state.copy(
            isLoading = true
        )
    }

}