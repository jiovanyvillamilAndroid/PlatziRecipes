package com.crisvillamil.platzirecipes.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.model.FakeDataProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

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
            delay(300L)
            state = state.copy(
                isLoading = false,
                recipes = FakeDataProvider.fakeRemoteData.map {
                    RecipeItemState(
                        recipeId = it.recipeId,
                        isFavorite = FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.contains(
                            it
                        ),
                        title = it.name,
                        isLoading = false,
                        authorName = it.authorName.orEmpty(),
                        authorImageUrl = it.authorImageUrl.orEmpty(),
                        imageUrl = it.imageUrl.orEmpty(),
                        rating = it.rating.toString(),
                    )
                },
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
            delay(1000L)
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
            val recipeInFavoriteList =
                FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.firstOrNull { it.recipeId == recipeId }
            val recipe = FakeDataProvider.fakeRemoteData.first { it.recipeId == recipeId }
            if (recipeInFavoriteList == null) {
                FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.add(recipe)
            } else {
                FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.remove(recipeInFavoriteList)
            }
        }
    }

    private fun onSearch(text: String) {
        state = state.copy(
            recipes = FakeDataProvider.fakeRemoteData.map {
                RecipeItemState(
                    recipeId = it.recipeId,
                    isFavorite = FakeDataProvider.fakeLocalPreferences.userFavoriteRecipe.contains(
                        it
                    ),
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
        )
    }

    private fun showLoadingState() {
        state = state.copy(
            isLoading = true
        )
    }

}