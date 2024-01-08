package com.crisvillamil.platzirecipes.creation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crisvillamil.platzirecipes.domain.CreateRecipeUseCase
import com.crisvillamil.platzirecipes.model.Difficulty
import kotlinx.coroutines.launch

class CreateRecipeViewModel(
    val createRecipeUseCase: CreateRecipeUseCase
) : ViewModel() {

    var state by mutableStateOf(CreateRecipeUIState())
        private set

    fun onEvent(createRecipeEvent: CreateRecipeEvent) {
        when (createRecipeEvent) {
            is CreateRecipeEvent.OnAddStep -> onAddStep(createRecipeEvent.stepDescription)
            is CreateRecipeEvent.OnRemoveStep -> onRemoveStep(createRecipeEvent.cookingStepIndex)
            is CreateRecipeEvent.OnCreateRecipe -> onCreateRecipe(
                createRecipeEvent.name,
                createRecipeEvent.description,
                createRecipeEvent.ingredients,
                createRecipeEvent.difficulty,
                createRecipeEvent.cookingTime
            )
        }
    }

    private fun onRemoveStep(cookingStepIndex: Int) {
        viewModelScope.launch {
            state = state.copy(
                cookingSteps = state.cookingSteps.toMutableList().also {
                    it.removeAt(cookingStepIndex)
                }
            )
        }
    }

    private fun onAddStep(stepDescription: String) {
        viewModelScope.launch {
            state = state.copy(
                cookingSteps = state.cookingSteps.toMutableList().also {
                    it.add(stepDescription)
                }
            )
        }
    }

    private fun onCreateRecipe(
        name: String,
        description: String,
        ingredients: String,
        difficulty: Difficulty?,
        cookingTime: String,
    ) {
        if (name.isNotEmpty() && ingredients.isNotEmpty() && difficulty != null && cookingTime.isNotEmpty()) {
            viewModelScope.launch {
                state = state.copy(
                    isLoading = true
                )
                createRecipeUseCase(
                    name = name,
                    description = description,
                    imageUrl = null,//TODO:Missing this implementation,
                    rating = null,
                    ingredients = ingredients,
                    cookingSteps = state.cookingSteps,
                    cookingTime = cookingTime,
                    difficulty = difficulty,
                    viewsCount = "0",
                    authorName = "Freddy Vega",//TODO: Hardcoded, missing add TextField to add it,
                    authorImageUrl = "https://freddyvega.com/content/images/size/w2000/2020/08/freddy-vega-grande.jpg"

                )
                state = state.copy(
                    recipeId = null,
                    name = null,
                    description = null,
                    imageUrl = null,
                    ingredients = null,
                    cookingSteps = arrayListOf(),
                    originCountry = null,
                    cookingTime = null,
                    isLoading = false,
                )
            }
        }
    }
}