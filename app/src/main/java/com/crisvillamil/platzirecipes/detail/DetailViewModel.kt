package com.crisvillamil.platzirecipes.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.model.FakeDataProvider
import com.crisvillamil.platzirecipes.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    var state by mutableStateOf(DetailUIState())
        private set

    lateinit var recipeId: String
    private lateinit var recipe: Recipe
    private val fakeLocalPreferences = FakeDataProvider.fakeLocalPreferences

    fun onEvent(detailEvent: DetailEvent) {
        when (detailEvent) {
            is DetailEvent.OnRateSelected -> onRateSelected(detailEvent.rate)
            DetailEvent.OnFetchRecipeDetail -> getRecipeDetails()
            DetailEvent.OnFavoriteClicked -> onFavoriteClicked()
        }
    }

    private fun onFavoriteClicked() {
        if (fakeLocalPreferences.userFavoriteRecipe.contains(recipe)) {
            fakeLocalPreferences.userFavoriteRecipe.remove(recipe)
        } else {
            fakeLocalPreferences.userFavoriteRecipe.add(recipe)
        }
        state = state.copy(
            recipeDetailState = state.recipeDetailState.copy(
                isFavorite = !state.recipeDetailState.isFavorite
            )
        )
    }

    private fun onRateSelected(rate: Int) {
        //TODO: send rate to post rate service, and retrieve the average rate
        fakeLocalPreferences.userRatedRecipes[recipeId.toInt()] = rate
        state = state.copy(
            recipeDetailState = state.recipeDetailState.copy(
                ratingBarValue = rate
            )
        )
    }

    private fun getRecipeDetails() {
        viewModelScope.launch {
            showLoadingState()
            delay(1000L)
            recipe = FakeDataProvider.fakeRemoteData.first { it.recipeId == recipeId.toInt() }
            state = state.copy(
                isLoading = false,
                recipeDetailState = RecipeDetailState(
                    imageUrl = recipe.imageUrl.orEmpty(),
                    name = recipe.name,
                    description = recipe.description,
                    rate = recipe.rating.toString(),
                    isFavorite = fakeLocalPreferences.userFavoriteRecipe.contains(recipe),
                    ingredients = recipe.ingredients,
                    cookingSteps = recipe.cookingSteps,
                    ratingBarValue = fakeLocalPreferences.userRatedRecipes[recipeId.toInt()] ?: 0,
                    cookingTime = recipe.cookingTime,
                    difficulty = recipe.difficulty,
                    authorName = recipe.authorName,
                    authorImageUrl = recipe.authorImageUrl,
                )
            )
        }
    }

    private fun showLoadingState() {
        state = state.copy(
            isLoading = true
        )
    }
}