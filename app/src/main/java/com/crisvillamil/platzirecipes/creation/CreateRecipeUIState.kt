package com.crisvillamil.platzirecipes.creation

import com.crisvillamil.platzirecipes.model.Difficulty


data class CreateRecipeUIState(
    val recipeId: Int? = null,
    val name: String? = null,
    val difficulty: Difficulty? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val ingredients: String? = null,
    val cookingSteps: List<String> = arrayListOf(),
    val originCountry: String? = null,
    val cookingTime: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)