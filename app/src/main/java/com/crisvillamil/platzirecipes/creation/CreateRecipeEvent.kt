package com.crisvillamil.platzirecipes.creation

import com.crisvillamil.platzirecipes.model.Difficulty

sealed class CreateRecipeEvent {
    data class OnCreateRecipe(
        val name: String,
        val description: String,
        val ingredients: String,
        val difficulty: Difficulty?,
        val cookingTime: String,
    ) : CreateRecipeEvent()

    data class OnAddStep(val stepDescription: String) : CreateRecipeEvent()
    data class OnRemoveStep(val cookingStepIndex: Int) : CreateRecipeEvent()
}
