package com.crisvillamil.platzirecipes.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.domain.AddToFavoriteRecipeUseCase
import com.crisvillamil.platzirecipes.domain.GetIsFavoriteRecipeUseCase
import com.crisvillamil.platzirecipes.domain.GetRecipeDetailUseCase
import com.crisvillamil.platzirecipes.domain.GetUserRatedRecipesUseCase
import com.crisvillamil.platzirecipes.domain.PutUserRatedRecipeUseCase
import com.crisvillamil.platzirecipes.domain.RemoveFromFavoriteRecipeUseCase
import com.crisvillamil.platzirecipes.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    var state by mutableStateOf(DetailUIState())
        private set

    var recipeId: Int? = null
    private lateinit var recipe: Recipe
    private val getIsFavoriteRecipeUseCase = GetIsFavoriteRecipeUseCase()
    private val addToFavoriteRecipeUseCase = AddToFavoriteRecipeUseCase()
    private val removeFromFavoriteRecipeUseCase = RemoveFromFavoriteRecipeUseCase()
    private val getRecipeDetailUseCase = GetRecipeDetailUseCase()
    private val getUserRatedRecipesUseCase = GetUserRatedRecipesUseCase()
    private val putUserRatedRecipesUseCase = PutUserRatedRecipeUseCase()

    fun onEvent(detailEvent: DetailEvent) {
        when (detailEvent) {
            is DetailEvent.OnRateSelected -> onRateSelected(detailEvent.rate)
            DetailEvent.OnFetchRecipeDetail -> getRecipeDetails()
            DetailEvent.OnFavoriteClicked -> onFavoriteClicked()
        }
    }

    private fun onFavoriteClicked() {
        viewModelScope.launch {
            recipe.recipeId.let { recipeId ->
                if (getIsFavoriteRecipeUseCase(recipeId)) {
                    removeFromFavoriteRecipeUseCase(recipeId)
                } else {
                    addToFavoriteRecipeUseCase(recipeId)
                }
                state = state.copy(
                    recipeDetailState = state.recipeDetailState.copy(
                        isFavorite = !state.recipeDetailState.isFavorite
                    )
                )
            }

        }
    }

    private fun onRateSelected(rate: Int) {
        viewModelScope.launch {
            putUserRatedRecipesUseCase(recipeId!!, rate)
            state = state.copy(
                recipeDetailState = state.recipeDetailState.copy(
                    ratingBarValue = rate
                )
            )
        }
    }

    private fun getRecipeDetails() {
        viewModelScope.launch {
            showLoadingState()
            recipe = getRecipeDetailUseCase(recipeId!!)
            state = state.copy(
                isLoading = false,
                recipeDetailState = RecipeDetailState(
                    imageUrl = recipe.imageUrl.orEmpty(),
                    name = recipe.name,
                    description = recipe.description,
                    rate = recipe.rating.toString(),
                    isFavorite = getIsFavoriteRecipeUseCase(recipeId!!),
                    ingredients = recipe.ingredients,
                    cookingSteps = recipe.cookingSteps,
                    ratingBarValue = getUserRatedRecipesUseCase(recipeId!!) ?: 0,
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